package aiss.GithubMiner.service;
import aiss.GithubMiner.model.Comment.User;
import aiss.GithubMiner.model.gitminer.MinerUser;
import aiss.GithubMiner.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class UserService {

    @Autowired
    RestTemplate restTemplate;
    public MinerUser getAuthenticatedUser(String username, String appPassword) {
        String uri = "https://api.github.com/user";

        // Basic Auth
        String auth = username + ":" + appPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<User> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    User.class
            );

            return UserTransformer.toGitMinerUser(response.getBody());

        } catch (Exception e) {
            System.err.println("Error al obtener el usuario autenticado: " + e.getMessage());
            return null;
        }
    }

    // Enviar el usuario autenticado a GitMiner
    public boolean sendUserToGitMiner(MinerUser user) {
        String gitMinerUrl = "http://localhost:8080/gitminer/users";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MinerUser> request = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(gitMinerUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Usuario enviado correctamente: " + user.getName());
                return true;
            } else {
                System.err.println("Error al enviar usuario: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error al enviar usuario: " + e.getMessage());
            return false;
        }
    }
    //PARA EL CONTROLADOR
    public boolean sendUserToGitMiner(String username, String token) {
        MinerUser user = getAuthenticatedUser(username, token);
        if (user == null) {
            System.err.println(" No se pudo obtener el usuario desde Github");
            return false;
        }
        return sendUserToGitMiner(user);
    }

    public void printUser(MinerUser user) {
        if (user != null) {
            System.out.println(" USER [" + user.getId() + "]");
            System.out.println("    - Username: " + user.getUsername());
            System.out.println("    - Name: " + user.getName());
            System.out.println("    - Avatar: " + user.getAvatarUrl());
            System.out.println("    - Web URL: " + user.getWebUrl());
        } else {
            System.out.println("USER [NULL]");
        }
    }
}
