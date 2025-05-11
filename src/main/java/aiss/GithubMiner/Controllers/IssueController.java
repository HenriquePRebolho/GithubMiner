package aiss.GithubMiner.Controllers;

import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class IssueController {

    @Autowired
    IssueService issueService;

    @GetMapping("/{workspace}/{repo_slug}/issues")
    public List<MinerIssue> getIssues(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return issueService.getIssues(workspace, repoSlug, nIssues, maxPages);
    }

    @GetMapping("/issues/{id}")
    public MinerIssue getIssue(@PathVariable String id) {
        return issueService.getIssueById("gentlero", "github-api", Long.parseLong(id));
    }

    @PostMapping("/{workspace}/{repo_slug}/issues")
    public String sendIssuesToGitMiner(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = issueService.sendIssuesToGitMiner(workspace, repoSlug, nIssues, maxPages);
        return saved + " issues enviados a GitMiner correctamente.";
    }
}
