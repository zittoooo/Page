package hello.mentoring.repository;

import hello.mentoring.dao.MemberDao;

import java.io.IOException;

public interface MemberFileRepository {
    void saveOnFile(MemberDao memberDao) throws IOException;
    MemberDao findByNameOnFile(MemberDao memberDao);
    void updateOnFile(MemberDao findDao, MemberDao updateDao) throws IOException;
    void deleteOnFile(MemberDao memberDao) throws IOException;
    void deleteOnFileByName(MemberDao memberDao) throws IOException;
    void deleteOnFileById(MemberDao memberDao) throws IOException;
}
