package hello.mentoring.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.mentoring.dao.MemberDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MemberFileRepositoryImpl implements MemberFileRepository {
    @Value("${file.dir}")
    private String fileDir;

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String filePath = fileDir + "fileDB";

    private File prepareFileRead(String file) {
        try {
            fileReader = new FileReader(fileDir + file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(fileReader);
        File openFile = new File(fileDir + file);
        return openFile;
    }

    private BufferedWriter prepareFileWrite(String file) {
        try {
            fileWriter = new FileWriter(fileDir + file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(fileWriter);
        return bufferedWriter;
    }

    @Override
    public void saveOnFile(MemberDao memberDao) throws IOException {
        bufferedWriter = prepareFileWrite("fileDB");
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(memberDao);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            bufferedWriter.write(jsonStr);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
//            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(jsonStr);
        }
    }

    @Override
    public MemberDao findByNameOnFile(MemberDao dao) {
        Optional<String> out = null;
        // 파일에서 해당 id를 가진 줄 찾기
        try {
            File file = prepareFileRead("fileDB");
            out = Files.lines(file.toPath())
                    .filter(line -> line.contains("\"memberName\":" + "\""+dao.getMemberName()+"\""))
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
    public void updateOnFile(MemberDao memberDao) throws IOException {
        deleteOnFile(memberDao);
        saveOnFile(memberDao);
    }

    @Override
    public void deleteOnFile(MemberDao dao) throws IOException {
            File file = prepareFileRead("fileDB");
            List<String> out = Files.lines(file.toPath())
                    .filter(line->!line.contains("\"memberName\":" + "\""+dao.getMemberName()+"\""))
                    .collect(Collectors.toList());
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
