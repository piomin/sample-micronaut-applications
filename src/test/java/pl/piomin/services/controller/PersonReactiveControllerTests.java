package pl.piomin.services.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Person;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;

@MicronautTest
public class PersonReactiveControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReactiveControllerTests.class);

    @Inject
    EmbeddedServer server;

    @Test
    public void testFindAll() throws MalformedURLException {
        RxHttpClient client = RxHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.retrieve(HttpRequest.GET("/persons/reactive"), Person.class)
                .subscribe(s -> LOGGER.info("Client: {}", s));
    }

}
