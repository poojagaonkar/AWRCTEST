
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
    "currentTeamEstimations",
    "currentTeamAvg",
    "currentTeamMax",
    "currentTeamMin"
})
public class MoreDetailModel {

    @JsonProperty("currentTeamEstimations")
    private List<CurrentTeamEstimation> currentTeamEstimations = null;
    @JsonProperty("currentTeamAvg")
    private CurrentTeamAvg currentTeamAvg;
    @JsonProperty("currentTeamMax")
    private CurrentTeamMax currentTeamMax;
    @JsonProperty("currentTeamMin")
    private CurrentTeamMin currentTeamMin;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("currentTeamEstimations")
    public List<CurrentTeamEstimation> getCurrentTeamEstimations() {
        return currentTeamEstimations;
    }

    @JsonProperty("currentTeamEstimations")
    public void setCurrentTeamEstimations(List<CurrentTeamEstimation> currentTeamEstimations) {
        this.currentTeamEstimations = currentTeamEstimations;
    }

    @JsonProperty("currentTeamAvg")
    public CurrentTeamAvg getCurrentTeamAvg() {
        return currentTeamAvg;
    }

    @JsonProperty("currentTeamAvg")
    public void setCurrentTeamAvg(CurrentTeamAvg currentTeamAvg) {
        this.currentTeamAvg = currentTeamAvg;
    }

    @JsonProperty("currentTeamMax")
    public CurrentTeamMax getCurrentTeamMax() {
        return currentTeamMax;
    }

    @JsonProperty("currentTeamMax")
    public void setCurrentTeamMax(CurrentTeamMax currentTeamMax) {
        this.currentTeamMax = currentTeamMax;
    }

    @JsonProperty("currentTeamMin")
    public CurrentTeamMin getCurrentTeamMin() {
        return currentTeamMin;
    }

    @JsonProperty("currentTeamMin")
    public void setCurrentTeamMin(CurrentTeamMin currentTeamMin) {
        this.currentTeamMin = currentTeamMin;
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
