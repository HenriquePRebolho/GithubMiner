package aiss.GithubMiner.Controllers;

import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class CommitController {

    @Autowired
    CommitService service;



    @GetMapping("/{owner}/{repo}")
    public List<MinerCommit> getCommitsFromFixedRoute(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return service.getCommits(owner, repo, nCommits, maxPages);
    }

    @GetMapping("/commits/{id}")
    public MinerCommit getCommit(@PathVariable String id) {
        return service.getCommitById("spring-projects", "spring-framework", id);
    }

    @PostMapping("/{owner}/{repo}")
    public String sendCommitsToGitMiner(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = service.sendCommitsToGitMiner(owner, repo, nCommits, maxPages);
        return saved + " commits enviados a GitMiner correctamente.";
    }
}
