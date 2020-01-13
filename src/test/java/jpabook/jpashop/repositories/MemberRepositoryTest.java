package jpabook.jpashop.repositories;

import jpabook.jpashop.entities.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // 테스트 종료후 롤백
    @Rollback(false) // 롤백시키고 싶지 않을때 사용
    public void save () throws Exception {
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(saveId);
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        // 같은 트랙잭션내 (같은 영속성 컨텍스트) 에서 식별자가 같다면 같은 엔티티임을 보장한다.
        // select 쿼리 자체가 나가지않음
        System.out.println("(findMember == member) = " + (findMember == member));
    }
}
