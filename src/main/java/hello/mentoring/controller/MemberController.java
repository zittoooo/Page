package hello.mentoring.controller;

import hello.mentoring.dto.FileDto;
import hello.mentoring.model.MemberForm;
import hello.mentoring.model.UploadFile;
import hello.mentoring.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hello.mentoring.model.Member;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
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
  
    // 파일 다운로드
    @GetMapping("/attach/{memberId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long memberId) throws MalformedURLException {
        Member member = memberService.findById(memberId);
        FileDto fileDto = memberService.makeResourceContentDisposition(member);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, fileDto.getContentDisposition())
                .body(fileDto.getResource());
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
        // 파일 업로드와 DB 저장 중에 뭘 먼저 해야 하는지? -> 문제가 생겼을 때 복구 비용 최소화 할 수 있도록
        Member member = memberService.makeMember(form);
        Member savedMember = memberService.save(member);
        // 저장하다가 실패하면?
        redirectAttributes.addAttribute("memberId", savedMember.getId());
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
