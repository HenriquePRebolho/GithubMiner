package aiss.GithubMiner.service;

import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.transformer.IssueTransformer;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import aiss.GithubMiner.model.Issue.Issue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IssueServiceTest {
    @Autowired
    IssueService issueService;

    public void testToGitMinerIssue() {
        Issue issue = new Issue();
        issue.setId(123L);
        issue.setNumber(456); // este es el número visible en GitHub
        issue.setTitle("Test Issue");
        issue.setState("open");
        issue.setBody("Contenido de prueba");
        issue.setCreatedAt("2025-05-11T10:00:00Z");
        issue.setUpdatedAt("2025-05-11T12:00:00Z");

        MinerIssue transformed = IssueTransformer.toGitMinerIssue(issue);

        assertNotNull(transformed);
        assertEquals("123", transformed.getId());
        assertEquals("Test Issue", transformed.getTitle());
        assertEquals("open", transformed.getState());
    }

    @Test
    @DisplayName("Get issues from Github, transform to GitMiner format and print them")
    public void getIssues() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int nIssues = 5;
        int maxPages = 1;

        List<MinerIssue> issues = issueService.getIssues(owner, repo, nIssues, maxPages);

        assertNotNull(issues);
        assertFalse(issues.isEmpty());

        issues.forEach(issueService::printIssue);
    }

    @Test
    public void getIssueById() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        long issueId = 1; // Debes usar un ID real aquí

        MinerIssue issue = issueService.getIssueById(owner, repo, issueId);
        assertNotNull(issue);
        issueService.printIssue(issue);
    }

    @Test
    @DisplayName("Send issues from Github to GitMiner")
    public void sendIssuesToGitMiner() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int nIssues = 5;
        int maxPages = 1;

        int sent = issueService.sendIssuesToGitMiner(owner, repo, nIssues, maxPages);
        System.out.println("Issues enviados: " + sent);
        assertTrue(sent > 0);
    }
}

