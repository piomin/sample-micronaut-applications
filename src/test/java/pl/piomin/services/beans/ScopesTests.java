package pl.piomin.services.beans;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.runtime.context.scope.refresh.RefreshEvent;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@MicronautTest
public class ScopesTests {

    @Inject
    BeginService begin;
    @Inject
    FinishService finish;

    @Test
    public void testThreadLocalScope() {
        final Random r = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                String correlationId = "abc" + r.nextInt(100);
                begin.start(correlationId);
                Assertions.assertEquals(correlationId, finish.finish());
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }

    @Inject
    ApplicationContext context;
    @Inject
    RefreshableService refreshable;

    @Test
    public void testRefreshableScope() {
        String testProperty = refreshable.getTestProperty();
        Assertions.assertEquals("hello", testProperty);
        context.getEnvironment().addPropertySource(PropertySource.of(CollectionUtils.mapOf("test.property", "hi")));
        context.publishEvent(new RefreshEvent());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testProperty = refreshable.getTestProperty();
        Assertions.assertEquals("hi", testProperty);
    }

    @Inject
    TestPropertyRequiredService service1;
    @Inject
    TestPropertyNotRequiredService service2;
    @Inject
    TestPropertyRequiredValueService service3;

    @Test
    public void testPropertyRequired() {
        String testProperty = service1.getTestProperty();
        Assertions.assertNotNull(testProperty);
        Assertions.assertEquals("hello", testProperty);
    }

    @Test
    public void testPropertyNotRequired() {
        String testProperty = service2.getTestProperty();
        Assertions.assertNotNull(testProperty);
        Assertions.assertEquals("None", testProperty);
    }

    @Test
    public void testPropertyValueRequired() {
        String testProperty = service3.getTestProperty();
        Assertions.assertNotNull(testProperty);
        Assertions.assertEquals("hello", testProperty);
    }

    @Inject
    ClientService client;
    @Inject
    @Named("client2")
    ClientService client2;
    @Inject
    @Named("client3")
    ClientService client3;

    @Test
    public void testClient() {
        ClientService[] clients = new ClientService[] {client, client2, client3};
        for (int i = 0; i < 10; i++) {
            clients[i%3].connect();
        }
    }
}
