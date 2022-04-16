package hello.mentoring.controller;

import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepository;
import hello.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.mentoring.model.Member;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/basic/members")
public class memberController {

    private final MemberService memberService;

    @Autowired
    public memberController(MemberService memberService) {
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
        UploadFile uploadFile = memberService.storeFile(form.getAttachFile());
        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);

        Member savedMember = memberService.save(member);
        redirectAttributes.addAttribute("memberId", savedMember.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/members/{memberId}";
    }

    //회원 수정
    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);

        MemberForm form = new MemberForm();
        form.setId(member.getId());
        form.setMemberName(member.getMemberName());
        form.setAddress(member.getAddress());
//        form.setUploadFileName(member.getAttachFile().getUploadFileName());
//        form.setAttachFile(member.getAttachFile());
        model.addAttribute("form", form);
        return "editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @ModelAttribute MemberForm form) throws IOException {
        UploadFile uploadFile = memberService.storeFile(form.getAttachFile());

        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);
        Member updateMember = memberService.update(memberId, member);
        return "redirect:/basic/members/{memberId}";
    }
}
