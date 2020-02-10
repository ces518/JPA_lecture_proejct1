package jpabook.jpashop.api;

import jpabook.jpashop.entities.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2020-02-07
 * Time: 21:08
 **/
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1 (@RequestBody @Valid Member member) {
        // 엔티티를 그대로 사용해선 안된다
        // api 스펙을위한 DTO를 사용해야한다.
        // > 엔티티가 수정되면 api스펙이 변경된다.
        // > 엔티티를 그대로 사용했을때 발생한 장애 케이스가 매우 많다.
        // 엔티티를 api 파라메터로 절대 쓰지말것
        // 외부에 노출해서도 안된다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2 (@RequestBody @Valid CreateMemberRequest request) {
        // DTO를 사용한 케이스
        // 엔티티가 수정되어도 api에 영향을 받지않는다.

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2 (@PathVariable Long id,
                                                @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }
}
