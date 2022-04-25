package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;

import java.io.IOException;

public interface MemberFileRepository {
    void saveOnFile(MemberDao memberDao) throws IOException;
    MemberDao findByIdOnFile(Long memberId);
    void updateOnFile(MemberDao memberDao);
    void deleteOnFile(Long id) throws IOException;
}
