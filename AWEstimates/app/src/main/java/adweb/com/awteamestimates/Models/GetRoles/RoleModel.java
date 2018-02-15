
package adweb.com.awteamestimates.Models.GetRoles;

import java.util.HashMap;
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
    "issueName",
    "roles"
})
public class RoleModel {

    @JsonProperty("projectKey")
    private String projectKey;
    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("issueKey")
    private String issueKey;
    @JsonProperty("issueName")
    private String issueName;
    @JsonProperty("roles")
    private HashMap<String ,String> roles;
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

    @JsonProperty("issueName")
    public String getIssueName() {
        return issueName;
    }

    @JsonProperty("issueName")
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    @JsonProperty("roles")
    public HashMap<String ,String> getRoles() {
        return roles;
    }

    @JsonProperty("roles")
    public void setRoles(HashMap<String ,String> roles) {
        this.roles = roles;
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
