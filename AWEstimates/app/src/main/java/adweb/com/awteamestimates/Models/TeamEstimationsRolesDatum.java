
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
    "userKey",
    "estimateLong",
    "estimateFormatted"
})
public class TeamEstimationsRolesDatum {

    @JsonProperty("userKey")
    private String userKey;
    @JsonProperty("estimateLong")
    private Integer estimateLong;
    @JsonProperty("estimateFormatted")
    private String estimateFormatted;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userKey")
    public String getUserKey() {
        return userKey;
    }

    @JsonProperty("userKey")
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @JsonProperty("estimateLong")
    public Integer getEstimateLong() {
        return estimateLong;
    }

    @JsonProperty("estimateLong")
    public void setEstimateLong(Integer estimateLong) {
        this.estimateLong = estimateLong;
    }

    @JsonProperty("estimateFormatted")
    public String getEstimateFormatted() {
        return estimateFormatted;
    }

    @JsonProperty("estimateFormatted")
    public void setEstimateFormatted(String estimateFormatted) {
        this.estimateFormatted = estimateFormatted;
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
