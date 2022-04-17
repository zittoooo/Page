package hello.mentoring.controller;

import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.mentoring.model.Member;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

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
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members";
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
        memberService.deleteMember(memberId);
        List<Member> members = memberService.findAll();
        model.addAttribute(members);

        return "redirect:/basic/members";
    }

    // 회원 추가
    @GetMapping("/add")
    public String addForm(@ModelAttribute MemberForm form) {
        return "addForm";
    }

    @PostMapping("/add")
    public String saveMember(@ModelAttribute MemberForm form, RedirectAttributes redirectAttributes) throws IOException {
        if (checkValidation(form) == false) {
            return "addForm";
        }
        UploadFile uploadFile = memberService.storeFile(form.getAttachFile());
        Member member = memberService.makeMember(form, uploadFile);
        Member savedMember = memberService.save(member);
        redirectAttributes.addAttribute("memberId", savedMember.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/members/{memberId}";
    }

    //회원 수정
    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        MemberForm form = memberService.makeForm(member);
        model.addAttribute("form", form);
        return "editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @ModelAttribute MemberForm form) throws IOException {
        if (checkValidation(form) == false) {
            return "redirect:/basic/members/{memberId}/edit";
        }

        UploadFile uploadFile = memberService.storeFile(form.getAttachFile());
        Member member = memberService.updateMember(form, uploadFile);
        Member updateMember = memberService.updateRepository(memberId, member);
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
