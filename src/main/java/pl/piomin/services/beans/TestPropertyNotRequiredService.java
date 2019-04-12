package pl.piomin.services.beans;

import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;

@Prototype
@Requires(missingProperty = "test.property2")
@Replaces(bean = TestPropertyRequiredValueService.class)
public class TestPropertyNotRequiredService {

    public String getTestProperty() {
        return "None";
    }
    
}
