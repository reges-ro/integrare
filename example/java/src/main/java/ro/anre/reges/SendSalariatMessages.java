package ro.anre.reges;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import ro.anre.reges.dto.MessageResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import ro.anre.reges.dto.UserDTO;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Component
public class SendSalariatMessages {

    @Value("${props.in-salariat-folder-name}")
    private String inFolderName;

    @Value("${props.out-folder-name}")
    private String outFolderName;
    @Autowired
    Utils utils;

    @Autowired
    XmlMapper mapper;
//
//
//    @Autowired
//    RestTemplate restTemplate;

    public void start() throws IOException, URISyntaxException, InterruptedException {
        String inPath ="./" + inFolderName;
        String outPath = "./" + outFolderName + "/";

        if(this.checkRequirements(inPath, outPath)){
            File dir = new File(inPath);

            File [] files = dir.listFiles((d, name) -> name.endsWith(".xml"));

            if (files != null) {
                UserDTO user = this.utils.getUserPassFromUser();
                String token = this.utils.login(user);
                System.out.println("Total fisiere:" + files.length);
                int count = 0;
                for (File file: files) {
                    String xml = Files.readString(file.toPath(), Charset.defaultCharset());

                    MessageResponse resp = this.sendMessage(token, xml);

                    mapper.writeValue(new File(outPath + "response-sal-" + resp.getHeader().getMessageId() +".xml"), resp);

                    moveFile(file, new File(outPath + file.getName()));
                    System.out.println("Am finalizat:  " + resp.getResponseId());
                    System.out.println("Am procesat:   " + ++count + " din " +files.length);

                }
            }
        }

    }

    public  void moveFile(File src, File dest) throws IOException {
        Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public boolean checkRequirements(String inPath, String outPath){
        File inDir = new File(inPath);
        if (!inDir.exists() || !inDir.isDirectory()) {
            System.out.println("Folderul IN nu exista langa jar");
            return false;
        }

        File outDir = new File(outPath);
        if (!outDir.exists() || !outDir.isDirectory()) {
            System.out.println("Folderul OUT nu exista langa jar");
            return false;
        }

        return true;
    }

    public MessageResponse sendMessage(String token, String xml) throws IOException, URISyntaxException, InterruptedException {
//        RestTemplate restTemplate = new RestTemplate();
//        URI uri = new URI(SpringBootConsoleApplication.apiDomain + "/api/Salariat");
//
//        HttpEntity<String> httpEntity = setPayload(token, xml);
//
//        ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);


        String url = SpringBootConsoleApplication.apiDomain + "/api/Salariat";

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/xml")
                .header("Accept", "application/xml")
                .header("Authorization", "Bearer " + token) // added this
                .POST(HttpRequest.BodyPublishers.ofString(xml))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return mapper.readValue(httpResponse.body(), MessageResponse.class);

    }

    private HttpEntity<String> setPayload(String token, String payload){
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.setContentType(MediaType.APPLICATION_XML);
        authHeader.setBearerAuth(token);

        return new HttpEntity<>(payload, authHeader);
    }

}
