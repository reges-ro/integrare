package ro.anre.reges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ro.anre.reges.dto.UserDTO;

import java.util.Map;
import java.util.Objects;

@Component
public class Utils {
    @Autowired
    RestTemplate restTemplate;

    public UserDTO getUserPassFromUser(){
        String user = System.getProperty("user");
        String pass = System.getProperty("password");

        return new UserDTO(user, pass);
    }

    public String login(UserDTO user) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "reges-api");
        map.add("client_secret", "FjtrYvDTGZKiyHGdSWymOvxhqifTJ7Em");
        map.add("username", user.getUser());
        map.add("password", user.getPassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Map> token = restTemplate
                .exchange(SpringBootConsoleApplication.loginDomain + "/realms/API/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        Map.class);


        return (String) Objects.requireNonNull(token.getBody()).get("access_token");
    }
}
