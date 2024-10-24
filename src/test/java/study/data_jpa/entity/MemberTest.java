package study.data_jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = "+member);
            System.out.println("-> member.team"+member.getTeam());
        }

        Member memberA = em.createQuery("select m from Member m where username =: username",Member.class)
                .setParameter("username","member1")
                .getSingleResult();
        teamB = em.createQuery("select t from Team t where name =: name", Team.class)
                .setParameter("name", "teamB")
                .getSingleResult();
        teamA = em.createQuery("select t from Team t where name =: name", Team.class)
                .setParameter("name", "teamA")
                .getSingleResult();
        memberA.changeTeam(teamB);

        List<Member> members2 = teamA.getMembers();
        for (Member member : members2) {
            System.out.println("after changedTeam teamA members =>"+member);
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member = new Member("member1");
        memberRepository.save(member);//@PrePersist 발생

        Thread.sleep(100);

        em.flush(); //@PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.createdDate = "+findMember.getCreatedDate());
//        System.out.println("findMember.updatedDate = "+findMember.getUpdatedDate());
        System.out.println("findMember.lastModifiedDate = "+findMember.getLastModifiedDate());
    }

}