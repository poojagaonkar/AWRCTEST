
/**
 * Created by PoojaGaonkar on 2/2/2018.
 */

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
        "loginCount",
        "previousLoginTime"
})
public class LoginInfo {


    @JsonProperty("loginCount")
    private Integer loginCount;
    @JsonProperty("previousLoginTime")
    private String previousLoginTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("loginCount")
    public Integer getLoginCount() {
        return loginCount;
    }

    @JsonProperty("loginCount")
    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @JsonProperty("previousLoginTime")
    public String getPreviousLoginTime() {
        return previousLoginTime;
    }

    @JsonProperty("previousLoginTime")
    public void setPreviousLoginTime(String previousLoginTime) {
        this.previousLoginTime = previousLoginTime;
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

