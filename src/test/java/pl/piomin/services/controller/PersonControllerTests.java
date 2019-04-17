package pl.piomin.services.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.piomin.services.model.Gender;
import pl.piomin.services.model.Person;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;

@MicronautTest
public class PersonControllerTests {

    @Inject
    EmbeddedServer server;

    @Test
    public void testAdd() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(33);
        person.setGender(Gender.MALE);
        person = client.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(Integer.valueOf(1), person.getId());
    }

    @Test
    public void testFindById() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/1"), Person.class);
        Assertions.assertNotNull(person);
    }

    @Test
    public void testFindByIdNotValid() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/10000"), Person.class);
        Assertions.assertNull(person);
    }

}
