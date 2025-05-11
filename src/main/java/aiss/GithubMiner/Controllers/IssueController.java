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

    @GetMapping("/{owner}/{repo}/issues")
    public List<MinerIssue> getIssues(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return issueService.getIssues(owner, repo, nIssues, maxPages);
    }

    @GetMapping("/issues/{id}")
    public MinerIssue getIssue(@PathVariable String id) {
        return issueService.getIssueById("spring-projects", "spring-framework", Long.parseLong(id));
    }

    @PostMapping("/{owner}/{repo}/issues")
    public String sendIssuesToGitMiner(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @RequestParam(defaultValue = "5") int nIssues,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = issueService.sendIssuesToGitMiner(owner, repo, nIssues, maxPages);
        return saved + " issues enviados a GitMiner correctamente.";
    }
}
