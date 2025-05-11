package aiss.GithubMiner.Controllers;

import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.model.gitminer.MinerProject;
import aiss.GithubMiner.service.CommitService;
import aiss.GithubMiner.service.IssueService;
import aiss.GithubMiner.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    CommitService commitService;

    @Autowired
    IssueService issueService;

    @GetMapping("/{owner}/projects")
    public List<MinerProject> getProjects(
            @PathVariable String owner,
            @RequestParam(defaultValue = "spring-framework") String repo,
            @RequestParam(defaultValue = "5") int nItems,
            @RequestParam(defaultValue = "1") int maxPages
    ) {
        List<MinerCommit> commits = commitService.getCommits(owner, repo, nItems, maxPages);
        List<MinerIssue> issues = issueService.getIssues(owner, repo, nItems, maxPages);
        return projectService.getProjects(owner, repo, commits, issues);
    }

    @PostMapping("/{owner}/projects")
    public String sendProjectsToGitMiner(
            @PathVariable String owner,
            @RequestParam(defaultValue = "spring-framework") String repo,
            @RequestParam(defaultValue = "5") int nItems,
            @RequestParam(defaultValue = "1") int maxPages
    ) {
        List<MinerCommit> commits = commitService.getCommits(owner, repo, nItems, maxPages);
        List<MinerIssue> issues = issueService.getIssues(owner, repo, nItems, maxPages);
        int saved = projectService.sendProjectsToGitMiner(owner, repo, commits, issues);
        return saved + " proyectos enviados a GitMiner correctamente.";
    }
}
