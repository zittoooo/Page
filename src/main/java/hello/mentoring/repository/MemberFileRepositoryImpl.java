package hello.mentoring.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import org.apache.tomcat.util.json.JSONParser;

import java.io.*;

public class MemberFileRepositoryImpl implements MemberFileRepository {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveOnFile(MemberDao memberDao) throws IOException {
        fileWriter = new FileWriter("/Users/jiho/Documents/Spring/file/" + memberDao.getId(), true);
        bufferedWriter = new BufferedWriter(fileWriter);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(memberDao);
        try {
            bufferedWriter.write(jsonStr);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MemberDao findByIdOnFile(Long memberId) {
        return null;
    }

    @Override
    public void updateOnFile(MemberDao memberDao) {

    }

    @Override
    public void deleteOnFile(Long id) {

    }
}
