package hello.mentoring.controller;

import hello.mentoring.model.MemberForm;
import hello.mentoring.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.mentoring.model.Member;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //회원 목록 출력
    @GetMapping
    public String members(Model model) {
        List<Member> memberList = memberService.findAll();
        model.addAttribute("members", memberList);
        return "members";
    }

    @PostMapping
    public String multicheck(@RequestParam String ids, Model model) {
        List<String> IDs = new ArrayList<String>(Arrays.asList(ids.split(",")));
        for (String id : IDs) {
            memberService.deleteMember(Long.parseLong(id));
        }
//        List<Member> memberList = memberService.findAll();
//        model.addAttribute("members", memberList);
        return "redirect:/basic/members";
    }


    // 회원 조회
    @GetMapping("/{memberId}")
    public String member(@PathVariable long memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "member";
    }

    // 회원 삭제
    @PostMapping("/{memberId}")
    public String deleteMember(@PathVariable long memberId, Model model) {
        List<Member> members = memberService.deleteMember(memberId);
        model.addAttribute(members);
        return "redirect:/basic/members";
    }

    // 왜 url을 이렇게 했으?
    // 회원 추가
    @GetMapping("/add")
    public String addForm() {
        return "addForm";
    }

    @PostMapping("/add")
    public String saveMember(@ModelAttribute MemberForm form, RedirectAttributes redirectAttributes) throws IOException {
        if (checkValidation(form) == false) {
            return "addForm";
        }
       Long savedId = memberService.save(form);
        // 저장하다가 실패하면?
        redirectAttributes.addAttribute("memberId", savedId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/members/{memberId}";
    }

    //회원 수정
    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        MemberForm form = memberService.findMember2Form(memberId);
        model.addAttribute("form", form);
        return "editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @ModelAttribute MemberForm form) throws IOException {
        Member member = memberService.updateMember(memberId, form);
        return "redirect:/basic/members/{memberId}";
    }

    // 입력 안한 것 있을 때
    private Boolean checkValidation(MemberForm form) {
        if (form.getMemberName().isEmpty() || form.getAddress().isEmpty() || form.getAttachFile().isEmpty()) {
            System.out.println("정확히 입력해 주세요.");
            return false;
        }
        return true;
    }

}
