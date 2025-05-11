package aiss.GithubMiner.service;

import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.model.gitminer.MinerProject;
import aiss.GithubMiner.model.project.Project;
import aiss.GithubMiner.transformer.ProjectTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    @Test
    public void testToGitMinerProject() {
        Project project = new Project();
        project.setId(1234);
        project.setName("Proyecto de prueba");
        project.setHtmlUrl("https://github.com/owner/repo");

        MinerProject result = ProjectTransformer.toGitMinerProject(project, List.of(), List.of());

        assertNotNull(result);
        assertEquals("1234", result.getId());
        assertEquals("Proyecto de prueba", result.getName());
        assertEquals("https://github.com/owner/repo", result.getWebUrl());
    }

    @Test
    @DisplayName("Obtener proyectos desde github y mostrarlos")
    public void getProjects() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int nCommits = 5;
        int maxPages = 1;

        List<MinerCommit> commits = commitService.getCommits(owner, repo, nCommits, maxPages);
        List<MinerIssue> issues = issueService.getIssues(owner, repo, nCommits, maxPages);

        List<MinerProject> projects = projectService.getProjects(owner,repo, commits, issues);

        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        projects.forEach(projectService::printProject);
    }

    @Test
    @DisplayName("Enviar proyectos desde github a GitMiner")
    public void sendProjectsToGitMiner_test() {
        String owner = "spring-projects";
        String repo = "spring-framework";
        int nCommits = 5;
        int maxPages = 1;

        List<MinerCommit> commits = commitService.getCommits(owner, repo, nCommits, maxPages);
        List<MinerIssue> issues = issueService.getIssues(owner, repo, nCommits, maxPages);

        int enviados = projectService.sendProjectsToGitMiner(owner,repo, commits, issues);
        assertTrue(enviados > 0);
    }


}
