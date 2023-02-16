package pl.piomin.services.beans;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class TestPropertyScopeTests {

    TestPropertyRequiredService service1;

    TestPropertyNotRequiredService service2;

    TestPropertyRequiredValueService service3;

    public TestPropertyScopeTests(TestPropertyRequiredService service1,
                                  TestPropertyNotRequiredService service2,
                                  TestPropertyRequiredValueService service3) {
        this.service1 = service1;
        this.service2 = service2;
        this.service3 = service3;
    }

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

}
