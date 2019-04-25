package pl.piomin.services.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserToken {

    private String username;
    @JsonProperty("access_token")
    private String accessToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
