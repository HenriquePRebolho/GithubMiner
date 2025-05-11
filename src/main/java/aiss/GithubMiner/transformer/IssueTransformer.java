package aiss.GithubMiner.transformer;


import aiss.GithubMiner.model.Issue.Issue;
import aiss.GithubMiner.model.gitminer.MinerIssue;

public class IssueTransformer {

    public static MinerIssue toGitMinerIssue(Issue githubIssue) {
        if (githubIssue == null) return null;

        MinerIssue result = new MinerIssue();

        // Title y Description (content.raw)
        result.setTitle(githubIssue.getTitle());
        result.setDescription(githubIssue.getBody() != null ? githubIssue.getBody().toString() : null);

        // Estado (open/closed/etc.)
        result.setState(githubIssue.getState());

        // Fechas
        result.setCreatedAt(githubIssue.getCreatedAt());
        result.setUpdatedAt(githubIssue.getUpdatedAt());

        // Fecha de cierre (solo si el estado es "closed")
        result.setClosedAt("closed".equalsIgnoreCase(githubIssue.getState()) ? githubIssue.getUpdatedAt() : null);

        // Etiquetas vacías (Bitbucket no proporciona)
        result.setLabels(new java.util.ArrayList<>());

        // Votos
        result.setVotes(null);

        // Autor del issue
        result.setAuthor(UserTransformer.toGitMinerUser(githubIssue.getUser()));

        return result;
    }
}
