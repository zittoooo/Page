<!-- 회원목록 출력 -->
```mermaid
sequenceDiagram
    participant MemberController as C
    participant View as V
    participant MemberService as S 
    participant MemberRepository as R 


    Note left of MemberController: 회원 추가
    alt
    View->>+MemberController:form
    MemberController->>+MemberService:makeMember(form)
    MemberService->>+FileStore:storeFile(multipartFile)
    FileStore->>-MemberService: UploadFile
    MemberService->>MemberController:Member
    MemberController->>+MemberService:save(Member)
    MemberService->>+MemberRepository:save(Member)
    MemberRepository->>+DB:em.persist(MemberDao)
    MemberRepository->>-MemberService:Member
    MemberService->>-MemberController:Member
    MemberController->>-View:MemberId, status
    end


    Note left of MemberController: 회원 목록 출력
    alt
    MemberController->>+MemberService: findAll()
    MemberService->>+MemberRepository:findAll();
    MemberRepository->>+DB: select m from MemberDao m
    DB->>-MemberRepository: List<memberDao>
    MemberRepository->>-MemberService: List<Member>
    MemberService->>-MemberController: List<Member>
    MemberController->>View:List<Member>
    end


    Note left of MemberController: 회원 상세 조회
    alt
    View->MemberController:memberId
    MemberController->>+MemberService:findById(memberId)
    MemberService->>+MemberRepository:findById(memberId)
    MemberRepository->>+DB:em.find()    
    DB->>-MemberRepository:MemberDao
    MemberRepository->>-MemberService:Member
    MemberService->>-MemberController: Member
    MemberController->>View:Member
    end





```

```mermaid
sequenceDiagram
    Note left of MemberController: 회원 수정
    alt
    View->>+MemberController:MemberId
    MemberController->>+MemberService:findMemberConvertForm(memberId)
    MemberService->>+MemberRepository:findById(memberId)
    MemberRepository->>+DB:findById(memberId)
    DB->>-MemberRepository:MemberDao
    MemberRepository->>-MemberService:Member
    MemberService->>-MemberController:MemberForm
    MemberController->>View:MemberForm


    View->>+MemberController:MemberId, MemberForm
    MemberController->>+MemberService:updateMember(memberId, MemberForm)
    MemberService->>+MemberRepository:findById(memberId)
    MemberRepository->>+DB:em.find()
    DB->>-MemberRepository:MemberDao
    MemberRepository->>-MemberService:Member
    MemberService->>+FileStore:storeFile(form.attachFile)
    FileStore->>-MemberService:UploadFile
    MemberService->>-MemberController:Member
    MemberController->>+MemberService:updateRepository(memberId, Member)
    MemberService->>+MemberRepository:update(memberId, Member)
    MemberRepository->>+DB:findByIdDao(memberId)
    DB->>-MemberRepository:MemberDao
    MemberRepository->>-MemberService: Member
    MemberService->>-MemberController: Member
    end

```

    
    

    
    
    
    MemberRepository->>-DB:MemberDao
    
    

<!-- ```mermaid
sequenceDiagram
    Note left of MemberController: 회원 조회
    alt
    

    end
``` -->

<!--  -->
