package adweb.com.awteamestimates.Models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by PoojaGaonkar on 2/2/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "session",
        "loginInfo"
})
public class LoginModel {

    @JsonProperty("session")
    private Session session;
    @JsonProperty("loginInfo")
    private LoginInfo loginInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("session")
    public Session getSession() {
        return session;
    }

    @JsonProperty("session")
    public void setSession(Session session) {
        this.session = session;
    }

    @JsonProperty("loginInfo")
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    @JsonProperty("loginInfo")
    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
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
