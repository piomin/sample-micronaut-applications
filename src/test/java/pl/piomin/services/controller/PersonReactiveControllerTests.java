package pl.piomin.services.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Gender;
import pl.piomin.services.model.Person;

@MicronautTest
public class PersonReactiveControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReactiveControllerTests.class);

    @Inject
    EmbeddedServer server;

    @Test
    public void testAdd() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        final Person person = new Person(null, "Name100", "Surname100", 22, Gender.MALE);
        RxHttpClient client = RxHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
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
        RxHttpClient client = RxHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
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
        RxStreamingHttpClient client = RxStreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.jsonStream(HttpRequest.GET("/persons/reactive/stream"), Person.class)
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
        RxStreamingHttpClient client = RxStreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
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

}
