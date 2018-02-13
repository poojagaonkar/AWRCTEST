
package adweb.com.awteamestimates.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "projectKey",
    "projectName",
    "issueKey",
    "issueTitle",
    "avatar",
    "teamEstimationsRolesData"
})
public class CurrentEstimatedIssue {

    @JsonProperty("projectKey")
    private String projectKey;
    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("issueKey")
    private String issueKey;
    @JsonProperty("issueTitle")
    private String issueTitle;
    @JsonProperty("avatar")
    private Integer avatar;
    @JsonProperty("teamEstimationsRolesData")
    private List<TeamEstimationsRolesDatum> teamEstimationsRolesData = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("projectKey")
    public String getProjectKey() {
        return projectKey;
    }

    @JsonProperty("projectKey")
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    @JsonProperty("projectName")
    public String getProjectName() {
        return projectName;
    }

    @JsonProperty("projectName")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @JsonProperty("issueKey")
    public String getIssueKey() {
        return issueKey;
    }

    @JsonProperty("issueKey")
    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    @JsonProperty("issueTitle")
    public String getIssueTitle() {
        return issueTitle;
    }

    @JsonProperty("issueTitle")
    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    @JsonProperty("avatar")
    public Integer getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("teamEstimationsRolesData")
    public List<TeamEstimationsRolesDatum> getTeamEstimationsRolesData() {
        return teamEstimationsRolesData;
    }

    @JsonProperty("teamEstimationsRolesData")
    public void setTeamEstimationsRolesData(List<TeamEstimationsRolesDatum> teamEstimationsRolesData) {
        this.teamEstimationsRolesData = teamEstimationsRolesData;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
