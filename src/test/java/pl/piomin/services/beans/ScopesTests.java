package pl.piomin.services.beans;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.context.scope.refresh.RefreshEvent;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
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
        System.setProperty("test.property", "hi");
        context.publishEvent(new RefreshEvent());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testProperty = refreshable.getTestProperty();
        Assertions.assertEquals("hi", testProperty);
    }
}
