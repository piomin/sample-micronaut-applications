package pl.piomin.services.secure;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.convert.format.MapFormat;

import java.util.Map;

@ConfigurationProperties("credentials")
public class UsersStore {

    @MapFormat
    Map<String, String> users;

    public String getUserPassword(String username) {
        return users.get(username);
    }

}
