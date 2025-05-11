package aiss.GithubMiner.transformer;


import aiss.GithubMiner.model.User.User;
import aiss.GithubMiner.model.gitminer.MinerUser;


public class UserTransformer {

    public static MinerUser toGitMinerUser(User githubUser) {
        if (githubUser == null) return null;

        MinerUser result = new MinerUser();

        // ID del usuario: usamos el UUID directamente
       // result.setId(githubUser.getUuid());

        // Username
        result.setUsername(null);

        // Name o display_name
        result.setUsername(githubUser.getName());

        // Avatar URL
        result.setAvatarUrl(githubUser.getAvatarUrl() != null
                ? githubUser.getAvatarUrl()
                : null);

        // Web URL
        result.setWebUrl(githubUser.getHtmlUrl() != null
                ? githubUser.getHtmlUrl()
                : null);


        return result;
    }

    public static MinerUser toGitMinerUser(aiss.GithubMiner.model.Issue.User issueUser) {
        if (issueUser == null) return null;

        MinerUser result = new MinerUser();
        result.setUsername(issueUser.getLogin());
        result.setName(null);
        result.setAvatarUrl(issueUser.getAvatarUrl() != null
                ? issueUser.getAvatarUrl()
                : null);
        result.setWebUrl(issueUser.getHtmlUrl() != null
                ? issueUser.getHtmlUrl()
                : null);

        return result;
    }

    public static MinerUser toGitMinerUser(aiss.GithubMiner.model.Comment.User commentUser) {
        if (commentUser == null) return null;

        MinerUser result = new MinerUser();

        result.setUsername(commentUser.getLogin());
        result.setName(null);

        result.setAvatarUrl(
                commentUser.getAvatarUrl() != null
                        ? commentUser.getAvatarUrl()
                        : null
        );

        result.setWebUrl(
                commentUser.getHtmlUrl() != null
                        ? commentUser.getHtmlUrl()
                        : null
        );

        return result;
    }
}