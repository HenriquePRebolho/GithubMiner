package aiss.GithubMiner.transformer;


import aiss.GithubMiner.model.Commit.Commit;
import aiss.GithubMiner.model.gitminer.MinerCommit;

public class CommitTransformer {

    public static MinerCommit toGitMinerCommit(Commit source) {
        MinerCommit target = new MinerCommit();

        target.setId(source.getSha());

        target.setTitle(null);

        target.setMessage(source.getCommit().getMessage());

        if (source.getAuthor() != null) {
            target.setAuthor_name(source.getAuthor().getLogin());
        }

        if (source.getAuthor() != null) {
            target.setAuthor_email(extractEmail(source.getAuthor().toString()));
        }

        target.setAuthored_date(source.getCommit().getCommitter().getDate());

        if (source.getHtmlUrl() != null) {
            target.setWeb_url(source.getHtmlUrl());
        }

        return target;
    }

    private static String extractEmail(String raw) {
        int start = raw.indexOf("<");
        int end = raw.indexOf(">");
        if (start != -1 && end != -1 && end > start) {
            return raw.substring(start + 1, end);
        }
        return null;
    }
}
