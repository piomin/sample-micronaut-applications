package pl.piomin.services.beans;

import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;
import pl.piomin.services.config.ClientConfig;

@Factory
public class ClientFactory {

    @EachBean(ClientConfig.class)
    ClientService client(ClientConfig config) {
        String url = config.getUrl();
        return new ClientService(url);
    }
}
