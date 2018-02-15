
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
    "10002",
    "10102"
})
public class Roles {

    @JsonProperty("10002")
    private String _10002;
    @JsonProperty("10102")
    private String _10102;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("10002")
    public String get10002() {
        return _10002;
    }

    @JsonProperty("10002")
    public void set10002(String _10002) {
        this._10002 = _10002;
    }

    @JsonProperty("10102")
    public String get10102() {
        return _10102;
    }

    @JsonProperty("10102")
    public void set10102(String _10102) {
        this._10102 = _10102;
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
