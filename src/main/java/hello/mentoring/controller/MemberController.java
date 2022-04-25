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
import java.sql.SQLOutput;
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
    // findAll 하는데 repository를 바로 열어도 되는데 왜 서비스를 나눴는지?
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberService.findAll();  // 왜 List를 썻는지?
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
        List<Member> members = memberService.deleteMember(memberId);
        model.addAttribute(members);
        return "redirect:/basic/members";
    }

    // 왜 url을 이렇게 했으?
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
        // 파일 업로드와 DB 저장 중에 뭘 먼저 해야 하는지? -> 문제가 생겼을 때 복구 비용 최소화 할 수 있도록
       Long savedId = memberService.save(form);
        // 저장하다가 실패하면?
        redirectAttributes.addAttribute("memberId", savedId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/members/{memberId}";
    }

    //회원 수정
    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model) {
        MemberForm form = memberService.findMemberConvertForm(memberId);
        model.addAttribute("form", form);
        return "editForm";
    }

    @PostMapping("/{memberId}/edit")
    public String edit(@PathVariable Long memberId, @ModelAttribute MemberForm form) throws IOException {
        Member member = memberService.updateMember(memberId, form);
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
