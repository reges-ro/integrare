package ro.anre.reges;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.anre.reges.dto.MessageResult;
import ro.anre.reges.dto.UserDTO;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ReceiveMessages {
    @Value("${props.receive-folder-name}")
    private String receiveFolderName;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    XmlMapper mapper;
    @Autowired
    Utils utils;

//    @Autowired
//    XmlMapper xmlMapper;


    public void receive() throws IOException, InterruptedException {
        String outPath = "./" + receiveFolderName + "/";
        int maxMessage = Integer.parseInt(System.getProperty("max-message"));
        boolean own = false;


        if(this.checkRequirements(outPath)){
            UserDTO user = this.utils.getUserPassFromUser();

            String token = this.utils.login(user);
            this.getMessage(token, outPath, 0, maxMessage, own);
        }
    }

    public void getMessage(String token, String outPath, int count, int maxMessage, boolean own) throws IOException, InterruptedException {
//        ResponseEntity<String> response = restTemplate
//                .exchange(SpringBootConsoleApplication.apiDomain + "/api/Status/PollMessage",
//                        HttpMethod.POST,
//                        setPayload(token),
//                        String.class);
//
        String url = SpringBootConsoleApplication.apiDomain + "/api/Status/PollMessage";

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();

        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/xml")
                .header("Accept", "application/xml")
                .header("Authorization", "Bearer " + token) // added this
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());



        if(response.statusCode() == 200 ){
            MessageResult resp = mapper.readValue(response.body(), MessageResult.class);
            Files.write(Paths.get(outPath + "response-" + resp.getHeader().getMessageId() + ".xml"), response.body().getBytes());

            System.out.println("Scris: " + ++count + "  " + resp.getHeader().getMessageId());

            if(maxMessage > count){
                Thread.sleep(1000);
                getMessage(token, outPath, count, maxMessage, own);
            }else{
                System.out.println("Limita max-message a fost atinsa , rulati iar");
            }

        }else{
            System.out.println("Nu exista mesaje pentru consum in acest moment");
        }
    }




    private HttpEntity<String> setPayload(String token){
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.setContentType(MediaType.APPLICATION_XML);
        authHeader.setBearerAuth(token);

        return new HttpEntity<>(authHeader);
    }

    public boolean checkRequirements(String outPath){
        File outDir = new File(outPath);
        if (!outDir.exists() || !outDir.isDirectory()) {
            System.out.println("Folderul Receive nu exista langa jar");
            return false;
        }

        return true;
    }
}
