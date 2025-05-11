package aiss.GithubMiner.service;

import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.model.gitminer.MinerProject;
import aiss.GithubMiner.model.project.Project;
import aiss.GithubMiner.transformer.ProjectTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
    public List<MinerProject> getProjects(String owner,String repoName,
                                          List<MinerCommit> allCommits,
                                          List<MinerIssue> allIssues) {
        List<MinerProject> result = new ArrayList<>();

        String uri = String.format("https://githup.com/repos/%s/%s", owner , repoName);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    uri, HttpMethod.GET, requestEntity, JsonNode.class
            );

            JsonNode valuesNode = response.getBody().get("values");
            ObjectMapper mapper = new ObjectMapper();
            Project[] projects = mapper.treeToValue(valuesNode, Project[].class);

            if (projects != null) {
                for (Project p : projects) {
                    // Filtra commits e issues que pertenecen a este proyecto
                    List<MinerCommit> projectCommits = allCommits.stream()
                            .filter(c -> c.getId().equals(p.getId())) // o getId()
                            .toList();

                    List<MinerIssue> projectIssues = allIssues.stream()
                            .filter(i -> i.getId().equals(p.getId()))
                            .toList();

                    result.add(ProjectTransformer.toGitMinerProject(p, projectCommits, projectIssues));
                }
            }

        } catch (JsonProcessingException e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Error en la llamada a gitHub: " + e.getMessage());
        }

        return result;
    }
    public int sendProjectsToGitMiner(String owner ,String repoName,List<MinerCommit> commits, List<MinerIssue> issues) {
        List<MinerProject> projects = getProjects(owner,repoName,commits, issues );
        String gitMinerUrl = "http://localhost:8080/gitminer/projects"; // Ajusta si tu endpoint es diferente

        int sent = 0;
        for (MinerProject project : projects) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MinerProject> request = new HttpEntity<>(project, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(gitMinerUrl, request, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Proyecto enviado correctamente: " + project.getId());
                    sent++;
                } else {
                    System.err.println("Error al enviar proyecto " + project.getId() + ": " + response.getStatusCode());
                }

            } catch (Exception e) {
                System.err.println("Error al enviar proyecto " + project.getId() + ": " + e.getMessage());
            }
        }

        return sent;
    }

    public void printProject(MinerProject project) {
        if (project != null) {
            System.out.println(" PROJECT [" + project.getId() + "]");
            System.out.println("    - Name: " + project.getName());
            System.out.println("    - Web URL: " + project.getWebUrl());
        } else {
            System.out.println("PROJECT [NULL]");
        }
    }
}
