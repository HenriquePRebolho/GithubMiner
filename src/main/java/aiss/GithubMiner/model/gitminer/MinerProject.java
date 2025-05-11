package aiss.GithubMiner.model.gitminer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity

public class MinerProject {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("webUrl")
    private String webUrl;

    @Transient
    @JsonProperty("commits")
    private List<MinerCommit> commits;

    @Transient
    @JsonProperty("issues")
    private List<MinerIssue> issues;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("webUrl")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("webUrl")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    public List<MinerCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<MinerCommit> commits) {
        this.commits = commits;
    }

    public List<MinerIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<MinerIssue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MinerProject.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id=");
        sb.append((this.id == null) ? "<null>" : this.id).append(',');
        sb.append("name=");
        sb.append((this.name == null) ? "<null>" : this.name).append(',');
        sb.append("webUrl=");
        sb.append((this.webUrl == null) ? "<null>" : this.webUrl).append(',');
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setCharAt(sb.length() - 1, ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
