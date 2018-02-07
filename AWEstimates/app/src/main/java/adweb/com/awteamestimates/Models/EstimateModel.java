
package adweb.com.awteamestimates.Models;

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
    "userName",
    "issueKey",
    "teamEstimate",
    "Success"
})
public class EstimateModel {

    @JsonProperty("userName")
    private String userName;
    @JsonProperty("issueKey")
    private String issueKey;
    @JsonProperty("teamEstimate")
    private String teamEstimate;
    @JsonProperty("Success")
    private Integer success;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("issueKey")
    public String getIssueKey() {
        return issueKey;
    }

    @JsonProperty("issueKey")
    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    @JsonProperty("teamEstimate")
    public String getTeamEstimate() {
        return teamEstimate;
    }

    @JsonProperty("teamEstimate")
    public void setTeamEstimate(String teamEstimate) {
        this.teamEstimate = teamEstimate;
    }

    @JsonProperty("Success")
    public Integer getSuccess() {
        return success;
    }

    @JsonProperty("Success")
    public void setSuccess(Integer success) {
        this.success = success;
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
