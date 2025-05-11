package aiss.GithubMiner.service;

import aiss.GithubMiner.model.Commit.Commit;
import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.transformer.CommitTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;
    public List<MinerCommit> getCommits(String owner, String repo, int nCommits, int maxPages) {
        List<MinerCommit> result = new ArrayList<>();

        for (int page = 1; page <= maxPages; page++) {
            String uri = String.format(
                    "https://api.github.com/repos/%s/%s/commits?page=%d&per_page=%d",
                    owner, repo, nCommits, page);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    JsonNode.class
            );

            try {
                JsonNode valuesNode = response.getBody().get("values");
                ObjectMapper mapper = new ObjectMapper();
                Commit[] commits = mapper.treeToValue(valuesNode, Commit[].class);

                if (commits != null) {
                    for (Commit commit : commits) {
                        result.add(CommitTransformer.toGitMinerCommit(commit));
                    }
                }

            } catch (JsonProcessingException e) {
                System.err.println("Error parsing JSON from gitHub: " + e.getMessage());
                e.printStackTrace(); // Opcional: comentar si no quieres stacktrace
            }
        }

        return result;
    }

    public MinerCommit getCommitById(String owner, String repo, String sha) {
        String uri = String.format("https://api.github.com/repos/%s/%s/commit/%s",
                owner, repo, sha);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Commit> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Commit.class
        );

        return CommitTransformer.toGitMinerCommit(response.getBody());
    }

    // Metodo que nos piden para POST commits desde github a git miner
    public int sendCommitsToGitMiner(String owner, String repo, int nCommits, int maxPages) {
        List<MinerCommit> commits = getCommits(owner, repo, nCommits, maxPages);
        String gitMinerUrl = "http://localhost:8080/gitminer/commits"; // URL real del endpoint de GitMiner

        int sent = 0;
        for (MinerCommit commit : commits) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MinerCommit> request = new HttpEntity<>(commit, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(gitMinerUrl, request, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("✔ Commit enviado correctamente: " + commit.getTitle());
                    sent++;
                } else {
                    System.err.println("✖ Error al enviar commit " + commit.getId() + ": " + response.getStatusCode());
                }

            } catch (Exception e) {
                System.err.println("⚠ Error al enviar commit " + commit.getId() + ": " + e.getMessage());
            }
        }
        return sent;
    }


    public void printCommit(MinerCommit commit) {
        if (commit != null) {
            System.out.println(" COMMIT [" + commit.getId() + "]");
            System.out.println("    - Title: " + commit.getTitle());
            System.out.println("    - Message: " + commit.getMessage());
            System.out.println("    - Author Name: " + commit.getAuthor_name());
            System.out.println("    - Author Email: " + commit.getAuthor_email());
            System.out.println("    - Authored Date: " + commit.getAuthored_date());
            System.out.println("    - Web URL: " + commit.getWeb_url());
        } else {
            System.out.println("COMMIT [NULL]");
        }
    }

}
