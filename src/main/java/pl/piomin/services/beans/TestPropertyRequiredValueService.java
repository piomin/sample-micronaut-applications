package pl.piomin.services.beans;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;

@Prototype
@Requires(property = "test.property", value = "hello", defaultValue = "Hi!")
public class TestPropertyRequiredValueService {

    @Property(name = "test.property")
    String testProperty;

    public String getTestProperty() {
        return testProperty;
    }

}
