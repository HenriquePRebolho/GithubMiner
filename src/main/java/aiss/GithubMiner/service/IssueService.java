package aiss.GithubMiner.service;
import aiss.GithubMiner.model.Issue.Issue;
import aiss.GithubMiner.model.gitminer.MinerIssue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;
    public List<MinerIssue> getIssues(String workspace, String repoSlug, int nIssues, int maxPages) {
        List<MinerIssue> result = new ArrayList<>();

        for (int page = 1; page <= maxPages; page++) {
            String uri = String.format(
                    "https://api.bitbucket.org/2.0/repositories/%s/%s/issues?pagelen=%d&page=%d",
                    workspace, repoSlug, nIssues, page);

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
                Issue[] issues = mapper.treeToValue(valuesNode, Issue[].class);

                if (issues != null) {
                    for (Issue issue : issues) {
                        result.add(IssueTransformer.toGitMinerIssue(issue));
                    }
                }

            } catch (JsonProcessingException e) {
                System.err.println("Error parsing JSON from Bitbucket: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return result;
    }

    public MinerIssue getIssueById(String workspace, String repoSlug, String issueId) {
        String uri = String.format("https://api.bitbucket.org/2.0/repositories/%s/%s/issues/%s",
                workspace, repoSlug, issueId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Issue> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Issue.class
        );

        return IssueTransformer.toGitMinerIssue(response.getBody());
    }

    public int sendIssuesToGitMiner(String workspace, String repoSlug, int nIssues, int maxPages) {
        List<MinerIssue> issues = getIssues(workspace, repoSlug, nIssues, maxPages);
        String gitMinerUrl = "http://localhost:8080/gitminer/issues"; // URL real del endpoint de GitMiner

        int sent = 0;
        for (MinerIssue issue : issues) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MinerIssue> request = new HttpEntity<>(issue, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(gitMinerUrl, request, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Issue enviada correctamente: " + issue.getAuthor());
                    sent++;
                } else {
                    System.err.println(" Error al enviar issue " + issue.getId() + ": " + response.getStatusCode());
                }

            } catch (Exception e) {
                System.err.println(" Error al enviar issue " + issue.getId() + ": " + e.getMessage());
            }
        }
        return sent;
    }

    public void printIssue(MinerIssue issue) {
        if (issue != null) {
            System.out.println(" ISSUE [" + issue.getId() + "]");
            System.out.println("    - Title: " + issue.getTitle());
            System.out.println("    - Description: " + issue.getDescription());
            System.out.println("    - State: " + issue.getState());
            System.out.println("    - CreatedAt: " + issue.getCreatedAt());
            System.out.println("    - UpdatedAt: " + issue.getUpdatedAt());
            System.out.println("    - ClosedAt: " + issue.getClosedAt());
            System.out.println("    - Votes: " + issue.getVotes());
        } else {
            System.out.println("ISSUE [NULL]");
        }
    }
}
