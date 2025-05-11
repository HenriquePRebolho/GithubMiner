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

    @GetMapping("/{workspace}/projects")
    public List<MinerProject> getProjects(
            @PathVariable String workspace,
            @RequestParam(defaultValue = "github-api") String repoSlug,
            @RequestParam(defaultValue = "5") int nItems,
            @RequestParam(defaultValue = "1") int maxPages
    ) {
        List<MinerCommit> commits = commitService.getCommits(workspace, repoSlug, nItems, maxPages);
        List<MinerIssue> issues = issueService.getIssues(workspace, repoSlug, nItems, maxPages);
        return projectService.getProjects(workspace, commits, issues);
    }

    @PostMapping("/{workspace}/projects")
    public String sendProjectsToGitMiner(
            @PathVariable String workspace,
            @RequestParam(defaultValue = "github-api") String repoSlug,
            @RequestParam(defaultValue = "5") int nItems,
            @RequestParam(defaultValue = "1") int maxPages
    ) {
        List<MinerCommit> commits = commitService.getCommits(workspace, repoSlug, nItems, maxPages);
        List<MinerIssue> issues = issueService.getIssues(workspace, repoSlug, nItems, maxPages);
        int saved = projectService.sendProjectsToGitMiner(workspace, commits, issues);
        return saved + " proyectos enviados a GitMiner correctamente.";
    }
}
