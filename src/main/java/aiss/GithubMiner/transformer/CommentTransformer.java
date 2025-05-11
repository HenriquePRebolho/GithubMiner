package aiss.GithubMiner.transformer;

import aiss.GithubMiner.model.Comment.Comment;
import aiss.GithubMiner.model.gitminer.MinerComment;

public class CommentTransformer {

    public static MinerComment toGitMinerComment(Comment githubComment) {
        if (githubComment == null) return null;

        MinerComment result = new MinerComment();

        // ID: Github lo da como Integer -> convertir a String
        result.setId(Long.valueOf(githubComment.getId()));

        // Body: body
        result.setBody(
                githubComment.getBody() != null ? githubComment.getBody() : null
        );

        result.setAuthor(UserTransformer.toGitMinerUser(githubComment.getUser()));

        // Fechas
        result.setCreatedAt(githubComment.getCreatedAt());
        result.setUpdatedAt(
                githubComment.getUpdatedAt() != null ? githubComment.getUpdatedAt() : null
        );

        return result;
    }
}