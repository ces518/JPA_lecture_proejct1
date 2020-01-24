package jpabook.jpashop.service;

import jpabook.jpashop.entities.Member;
import jpabook.jpashop.repositories.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * 단위 테스트 보다는 외부 DB에서 잘 동작하는지 확인하기 위해
 * 통합 테스트로 진행한다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    //@Rollback(false)
    public void 회원가입 () throws Exception {
        // given
        Member member = new Member();
        member.setName("PARK");

        // when
        Long joinedId = memberService.join(member);

        // then
        assertThat(member).isEqualTo(memberRepository.find(joinedId));
        // join 을 통해서 생성된 멤버와, memberRepository를 통해 찾아온 member가 같아야한다.
        // JPA 는 같은 트랜잭션내에서 같은 식별자를 가진 엔티티는 동일성을 보장한다.
    }

    // try - catch 방식보다 깔끔한 예외발생 테스트기능
    // expected 를 활용한다.
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외 () throws Exception {
        // given
        Member member = new Member();
        member.setName("PARK");

        Member member2 = new Member();
        member2.setName("PARK");

        // when
        memberService.join(member);
        // 같은 이름으로 가입을 시도했기 때문에 예외가 발생해야 한다.
        // 예외 발생후 아래 라인으로 이동하면 안된다.
        memberService.join(member2);

        /*
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            // try-catch 를 활용해 예외가 발생하면 메소드를 종료시켜버린다.
            // 하지만 이러한 방식은 코드가 지저분해진다.
            return;
        }
        */

        // then
        /**
         * 만약 여기 코드라인까지 오게된다면 성공이라고 뜨게 된다.
         * 그럼 잘못된 테스트 코드이다.
         * 이를 위해 junit에서 fail 이라는 메소드를 제공함.
         * 테스트를 실패시킨다.
         */
        fail("예외가 발생해야함");
    }
}