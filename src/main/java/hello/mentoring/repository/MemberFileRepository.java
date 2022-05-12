package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;

import java.io.IOException;

public interface MemberFileRepository {
    void saveOnFile(MemberDao memberDao) throws IOException;
    MemberDao findByNameOnFile(MemberDao memberDao);
    void updateOnFile(MemberDao memberDao) throws IOException;
    void deleteOnFile(MemberDao memberDao) throws IOException;
}
