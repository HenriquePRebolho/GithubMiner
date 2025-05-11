package aiss.GithubMiner.transformer;


import aiss.GithubMiner.model.gitminer.MinerCommit;
import aiss.GithubMiner.model.gitminer.MinerIssue;
import aiss.GithubMiner.model.gitminer.MinerProject;
import aiss.GithubMiner.model.project.Project;

import java.util.List;

public class ProjectTransformer {

    public static MinerProject toGitMinerProject(Project source,
                                                 List<MinerCommit> githubCommits,
                                                 List<MinerIssue> githubIssues) {
        MinerProject target = new MinerProject();
        // Usa el UUID del proyecto como ID
        target.setName(source.getName());

        if (source.getHtmlUrl() != null) {
            target.setWebUrl(source.getHtmlUrl());
        }

        if (githubCommits != null) {
            target.setCommits(githubCommits);  // ya están en formato MinerCommit
        }

        if (githubIssues != null) {
            target.setIssues(githubIssues);  // ya están en formato MinerIssue
        }

        return target;
    }
}
