package aiss.GithubMiner.service;

import aiss.GithubMiner.model.gitminer.MinerCommit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommitServiceTest {
    @Autowired
    CommitService service;

    @Test
    @DisplayName("Get commits from GitHub transform them to GitMiner format and print them on the console")
    public void getCommits() {
        String owner = "gentlero"; // ejemplo real
        String repo = "Github-api"; // ejemplo real
        int nCommits = 5;
        int maxPages = 1;

        List<MinerCommit> commits = service.getCommits(owner, repo, nCommits, maxPages);

        assertNotNull(commits);
        assertFalse(commits.isEmpty());

        // Imprimir los commits transformados a formato GitMiner
        commits.forEach(service::printCommit);
    }

    @Test
    @DisplayName("Get commit by commit id")
    public void getCommitById() {
        String owner = "gentlero";
        String repo = "Github-api";
        String commitId = "67a0362b29f34c45251ce88c5851756fb30a65cc"; // Usa un ID real

        MinerCommit commit = service.getCommitById(owner, repo, commitId);
        service.printCommit(commit);
    }

    @Test
    @DisplayName("Enviar commits desde Github a GitMiner")
    public void sendCommitsToGitMiner() {
        String owner = "gentlero";
        String repo = "gitHub-api";
        int nCommits = 5;
        int maxPages = 1;

        service.sendCommitsToGitMiner(owner, repo, nCommits, maxPages);
    }

    @Test
    void printCommit() {
    }
}