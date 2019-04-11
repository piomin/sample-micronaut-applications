package pl.piomin.services.beans;

import io.micronaut.context.annotation.Property;
import io.micronaut.runtime.context.scope.Refreshable;

import javax.annotation.PostConstruct;

@Refreshable
public class RefreshableService {

    @Property(name = "test.property")
    String testProperty;

    @PostConstruct
    public void init() {
        System.out.println("Property: " + testProperty);
    }

    public String getTestProperty() {
        return testProperty;
    }

}
