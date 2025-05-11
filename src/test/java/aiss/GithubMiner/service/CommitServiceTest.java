package aiss.GithubMiner.service;

import aiss.GithubMiner.model.Commit.Commit;
import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.transformer.CommitTransformer;
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
    public void testToGitMinerCommit() throws Exception {
        // Simulación de un commit mínimo
        Commit commit = new Commit();
        commit.setSha("abc123");
        commit.setCommentsUrl("Mensaje de prueba");

        MinerCommit transformed = CommitTransformer.toGitMinerCommit(commit);


        assertNotNull(transformed);
        assertEquals("abc123", transformed.getId());
        assertEquals("Mensaje de prueba", transformed.getMessage());
    }

    @Test
    @DisplayName("Get commits from GitHub transform them to GitMiner format and print them on the console")
    public void getCommits() {
        String owner = "spring-projects"; // ejemplo real
        String repo = "spring-framework"; // ejemplo real
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
        String owner = "spring-projects";
        String repo = "spring-framework";
        String sha = "6878587a335e99c0b5793c1705c7a264bcc4460d"; // Usa un ID real

        MinerCommit commit = service.getCommitById(owner, repo, sha);
        service.printCommit(commit);
    }

    @Test
    @DisplayName("Enviar commits desde GitHub a GitMiner")
    public void sendCommitsToGitMiner() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int nCommits = 5;
        int maxPages = 1;

        service.sendCommitsToGitMiner(owner, repo, nCommits, maxPages);
    }

    @Test
    void printCommit() {
    }
}