package pl.piomin.services.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import jakarta.inject.Inject;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.client.PersonReactiveClient;
import pl.piomin.services.model.Gender;
import pl.piomin.services.model.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@MicronautTest
public class PersonReactiveControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReactiveControllerTests.class);

    @Inject
    EmbeddedServer server;

    @Test
    public void testAdd() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        final Person person = new Person(null, "Name100", "Surname100", 22, Gender.MALE);
        Rx3HttpClient client = Rx3HttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Single<Person> s = client.retrieve(HttpRequest.POST("/persons/reactive", person), Person.class).firstOrError();
        s.subscribe(person1 -> {
            LOGGER.info("Retrieved: {}", person1);
            waiter.assertNotNull(person1);
            waiter.assertNotNull(person1.getId());
            waiter.resume();
        });
        waiter.await(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testFindById() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Rx3HttpClient client = Rx3HttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        Maybe<Person> s = client.retrieve(HttpRequest.GET("/persons/reactive/1"), Person.class).firstElement();
        s.subscribe(person1 -> {
            LOGGER.info("Retrieved: {}", person1);
            waiter.assertNotNull(person1);
            waiter.assertEquals(1, person1.getId());
            waiter.resume();
        });
        waiter.await(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testFindAllStream() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Rx3StreamingHttpClient client = Rx3StreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.jsonStream(HttpRequest.GET("/persons/reactive/stream"), Person.class)
                .subscribe(s -> {
                    LOGGER.info("Client: {}", s);
                    waiter.assertNotNull(s);
                    waiter.resume();
                });
        waiter.await(3000, TimeUnit.MILLISECONDS, 9);
    }

    @Test
    public void testFindAllStreamDelayed() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Rx3StreamingHttpClient client = Rx3StreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.jsonStream(HttpRequest.GET("/persons/reactive/stream/callable/delayed"), Person.class)
                .subscribe(s -> {
                    LOGGER.info("Client: {}", s);
                    waiter.assertNotNull(s);
                    waiter.resume();
                });
        waiter.await(3000, TimeUnit.MILLISECONDS, 9);
    }

    @Test
    public void testFindAllStreamWithCallable() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Rx3StreamingHttpClient client = Rx3StreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.jsonStream(HttpRequest.GET("/persons/reactive/stream/callable"), Person.class)
            .subscribe(new Subscriber<Person>() {

                Subscription s;

                @Override
                public void onSubscribe(Subscription subscription) {
                    subscription.request(1);
                    s = subscription;
                }

                @Override
                public void onNext(Person person) {
                    LOGGER.info("Client: {}", person);
                    waiter.assertNotNull(person);
                    waiter.resume();
                    s.request(1);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
        waiter.await(3000, TimeUnit.MILLISECONDS, 9);
    }

    @Inject
    PersonReactiveClient client;

    @Test
    public void testAddDeclarative() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        final Person person = new Person(null, "Name100", "Surname100", 22, Gender.MALE);
        Single<Person> s = client.add(person);
        s.subscribe(person1 -> {
            LOGGER.info("Retrieved: {}", person1);
            waiter.assertNotNull(person1);
            waiter.assertNotNull(person1.getId());
            waiter.resume();
        });
        waiter.await(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testFindByIdDeclarative() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Maybe<Person> s = client.findById(1);
        s.subscribe(person1 -> {
            LOGGER.info("Retrieved: {}", person1);
            waiter.assertNotNull(person1);
            waiter.assertEquals(1, person1.getId());
            waiter.resume();
        });
        waiter.await(3000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testFindAllStreamDeclarative() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Flowable<Person> persons = client.findAllStream();
        persons.subscribe(s -> {
            LOGGER.info("Client: {}", s);
            waiter.assertNotNull(s);
            waiter.resume();
        });
        waiter.await(3000, TimeUnit.MILLISECONDS, 9);
    }

}
