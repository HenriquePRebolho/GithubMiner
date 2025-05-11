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
    CommitService commitService;



    @GetMapping("/{workspace}/{repo_slug}")
    public List<MinerCommit> getCommitsFromFixedRoute(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return commitService.getCommits(workspace, repoSlug, nCommits, maxPages);
    }

    @GetMapping("/commits/{id}")
    public MinerCommit getCommit(@PathVariable String id) {
        return commitService.getCommitById("gentlero", "github-api", id);
    }

    @PostMapping("/{workspace}/{repo_slug}")
    public String sendCommitsToGitMiner(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @RequestParam(defaultValue = "5") int nCommits,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = commitService.sendCommitsToGitMiner(workspace, repoSlug, nCommits, maxPages);
        return saved + " commits enviados a GitMiner correctamente.";
    }
}
