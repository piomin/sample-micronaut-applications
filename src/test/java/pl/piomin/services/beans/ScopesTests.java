package pl.piomin.services.beans;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.runtime.context.scope.refresh.RefreshEvent;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@MicronautTest
public class ScopesTests {

    @Inject
    BeginService begin;
    @Inject
    FinishService finish;
    @Inject
    ApplicationContext context;
    @Inject
    RefreshableService refreshable;

    @Test
    public void testThreadLocalScope() {
        final Random r = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                String correlationId = "abc" + r.nextInt(10000);
                begin.start(correlationId);
                Assertions.assertEquals(correlationId, finish.finish());
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }

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

}
