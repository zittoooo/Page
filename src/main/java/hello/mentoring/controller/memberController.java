package hello.mentoring.controller;

import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.repository.FileStore;
import hello.mentoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class memberController {

    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

    //회원 목록 출력
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);

        return "members";
    }

    // 회원 조회
    @GetMapping("/{memberId}")
    public String member(@PathVariable long memberId, Model model) {
        Member member = memberRepository.findById(memberId);
        model.addAttribute("member", member);
        System.out.println("조회: " +  member.getAttachFile().getUploadFileName());
        return "member";
    }

    // 회원 삭제
    @PostMapping("/{memberId}")
    public String deleteMember(@PathVariable long memberId, Model model) {
        memberRepository.delete(memberId);
        List<Member> members = memberRepository.findAll();
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
        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());

        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);

        Member savedMember = memberRepository.save(member);
        redirectAttributes.addAttribute("memberId", savedMember.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/members/{memberId}";
    }

    //회원 수정
    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        Member member = memberRepository.findById(memberId);

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
        System.out.println("수정 후 " + form.getAttachFile());
        UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());

        Member member = new Member();
        member.setMemberName(form.getMemberName());
        member.setAddress(form.getAddress());
        member.setAttachFile(uploadFile);
        Member updateMember = memberRepository.update(memberId, member);

        return "redirect:/basic/members/{memberId}";
    }



}
