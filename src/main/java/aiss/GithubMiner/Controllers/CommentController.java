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

    @GetMapping("/{workspace}/{repo_slug}/issues/{issueId}/comments")
    public List<MinerComment> getComments(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @PathVariable Integer issueId,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        return commentService.getComments(workspace, repoSlug, issueId, maxPages);
    }

    @PostMapping("/{workspace}/{repo_slug}/issues/{issueId}/comments")
    public String sendCommentsToGitMiner(
            @PathVariable String workspace,
            @PathVariable("repo_slug") String repoSlug,
            @PathVariable Integer issueId,
            @RequestParam(defaultValue = "2") int maxPages
    ) {
        int saved = commentService.sendCommentsToGitMiner(workspace, repoSlug, issueId, maxPages);
        return saved + " comentarios enviados a GitMiner correctamente.";
    }
}
