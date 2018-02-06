
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
    "currentEstimatedIssue"
})
public class ProjectModel {

    @JsonProperty("currentEstimatedIssue")
    private List<CurrentEstimatedIssue> currentEstimatedIssue = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currentEstimatedIssue")
    public List<CurrentEstimatedIssue> getCurrentEstimatedIssue() {
        return currentEstimatedIssue;
    }

    @JsonProperty("currentEstimatedIssue")
    public void setCurrentEstimatedIssue(List<CurrentEstimatedIssue> currentEstimatedIssue) {
        this.currentEstimatedIssue = currentEstimatedIssue;
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
