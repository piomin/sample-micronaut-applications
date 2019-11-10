package pl.piomin.services.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.piomin.services.model.Person;

import javax.inject.Inject;
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
    public void testFindAll() throws MalformedURLException, TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
//        RxHttpClient client = RxHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        RxStreamingHttpClient client = RxStreamingHttpClient.create(new URL("http://" + server.getHost() + ":" + server.getPort()));
        client.jsonStream(HttpRequest.GET("/persons/reactive"), Person.class)
        //        client.retrieve(HttpRequest.GET("/persons/reactive"), Person.class)
//                .doOnComplete(() -> waiter.resume())
                .subscribe(s -> {
                    LOGGER.info("Client: {}", s);
                    waiter.assertNotNull(s);
                    waiter.resume();
                });
//        .subscribe(new Subscriber<Person>() {
//
//            Subscription s;
//            @Override
//            public void onSubscribe(Subscription subscription) {
//                subscription.request(1);
//                s = subscription;
//            }
//
//            @Override
//            public void onNext(Person person) {
//                LOGGER.info("Client: {}", person);
//                waiter.assertNotNull(person);
//                waiter.resume();
//                s.request(1);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        waiter.await(3000, TimeUnit.MILLISECONDS, 9);
    }

}
