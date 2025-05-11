package aiss.GithubMiner.Controllers;

import aiss.GithubMiner.model.gitminer.MinerComment;
import aiss.GithubMiner.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/{owner}/{repo}/issues/{issueId}/comments")
    public List<MinerComment> getComments(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @PathVariable Integer issueId,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return commentService.getComments(owner, repo, issueId, maxPages);
    }

    @PostMapping("/{owner}/{repo}/issues/{issueId}/comments")
    public String sendCommentsToGitMiner(
            @PathVariable String owner,
            @PathVariable("repo") String repo,
            @PathVariable Integer issueId,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = commentService.sendCommentsToGitMiner(owner, repo, issueId, maxPages);
        return saved + " comentarios enviados a GitMiner correctamente.";
    }
}
