package hello.mentoring.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hello.mentoring.dao.MemberDao;
import hello.mentoring.model.Member;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class MemberFileRepositoryImpl implements MemberFileRepository {
    @Value("${file.dir}")
    private String fileDir;

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void saveOnFile(MemberDao memberDao) throws IOException {

        fileWriter = new FileWriter(fileDir + "fileDB", true);
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
    public void deleteOnFile(Long id){
        try {
            fileReader = new FileReader(fileDir + "fileDB");
            bufferedReader = new BufferedReader(fileReader);
            File file = new File(fileDir + "fileDB");
            List<String> out = Files.lines(file.toPath())
                    .filter(line-> !line.contains("{\"id\":" + id))
                    .collect(Collectors.toList());
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
