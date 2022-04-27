package hello.mentoring.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class MemberFileRepositoryImpl implements MemberFileRepository {
    @Value("${file.dir}")
    private String fileDir;

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String filePath = fileDir + "fileDB";

    private File prepareFileRead(String file) throws FileNotFoundException {
        fileReader = new FileReader(fileDir + file);
        bufferedReader = new BufferedReader(fileReader);
        File openFile = new File(fileDir + file);
        return openFile;
    }

    private BufferedWriter prepareFileWrite(String file) throws IOException {
        fileWriter = new FileWriter(fileDir + file, true);
        bufferedWriter = new BufferedWriter(fileWriter);
        return bufferedWriter;
    }

    @Override
    public void saveOnFile(MemberDao memberDao) throws IOException {
        bufferedWriter = prepareFileWrite("fileDB");
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
        Optional<String> out = null;
        // 파일에서 해당 id를 가진 줄 찾기
        try {
            File file = prepareFileRead("fileDB");
            out = Files.lines(file.toPath())
                    .filter(line -> line.contains("{\"id\":" + memberId))
                    .findAny();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        MemberDao memberDao = null;
        try {
            memberDao = mapper.readValue(out.get(), MemberDao.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return memberDao;
    }

    @Override
    public void updateOnFile(MemberDao memberDao) {
        try {
            deleteOnFile(memberDao.getId());
            saveOnFile(memberDao);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOnFile(Long id){
        try {
            File file = prepareFileRead("fileDB");
            List<String> out = Files.lines(file.toPath())
                    .filter(line-> !line.contains("{\"id\":" + id))
                    .collect(Collectors.toList());
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
