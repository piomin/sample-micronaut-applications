package pl.piomin.services.controller;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.piomin.services.client.PersonClient;
import pl.piomin.services.model.Gender;
import pl.piomin.services.model.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@MicronautTest
public class PersonControllerTests {

    EmbeddedServer server;
    HttpClient client;
    PersonClient personClient;

    public PersonControllerTests(EmbeddedServer server, @Client("/") HttpClient client, PersonClient personClient) {
        this.server = server;
        this.client = client;
        this.personClient = personClient;
    }

    @Test
    public void testAdd() {
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
    public void testAddNotValid() {
        final Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAge(-1);
        person.setGender(Gender.MALE);

        Assertions.assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(HttpRequest.POST("/persons", person), Person.class),
                "person.age: must be greater than or equal to 0");
    }

    @Test
    public void testFindById() {
        Person person = client.toBlocking().retrieve(HttpRequest.GET("/persons/1"), Person.class);
        Assertions.assertNotNull(person);
    }

    @Test
    public void testFindAll() {
        Person[] persons = client.toBlocking().retrieve(HttpRequest.GET("/persons").header("X-API-VERSION", "1"), Person[].class);
        Assertions.assertEquals(1, persons.length);
    }

    @Test
    public void testFindAllV2() {
        List<Person> persons = personClient.findAllV2(10, 0);
        Assertions.assertEquals(1, persons.size());
    }

}
