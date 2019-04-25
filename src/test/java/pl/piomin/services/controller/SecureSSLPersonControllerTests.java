package pl.piomin.services.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.piomin.services.model.Gender;
import pl.piomin.services.model.Person;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;

@MicronautTest(propertySources = "classpath:ssl.yml")
public class SecureSSLPersonControllerTests {

	@Inject
	EmbeddedServer server;

	@Test
	public void testAdd() throws MalformedURLException {
		HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Smith");
		person.setAge(33);
		person.setGender(Gender.MALE);
		person = client.toBlocking()
				.retrieve(HttpRequest.POST("/secure/persons", person).basicAuth("smith", "smith123"), Person.class);
		Assertions.assertNotNull(person);
		Assertions.assertEquals(Integer.valueOf(1), person.getId());
	}

	@Test
	public void testAddFailed() throws MalformedURLException {
		HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Smith");
		person.setAge(33);
		person.setGender(Gender.MALE);
		Assertions.assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().retrieve(HttpRequest.POST("/secure/persons", person).basicAuth("scott", "scott123"), Person.class),
				"Forbidden");
	}

	@Test
	public void testFindById() throws MalformedURLException {
		HttpClient client = HttpClient.create(new URL(server.getScheme() + "://" + server.getHost() + ":" + server.getPort()));
		Person person = client.toBlocking()
				.retrieve(HttpRequest.GET("/secure/persons/1").basicAuth("scott", "scott123"), Person.class);
		Assertions.assertNotNull(person);
	}
}
