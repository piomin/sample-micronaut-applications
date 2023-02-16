package pl.piomin.services.beans;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FinishService {

    @Inject
    MiddleService service;

    public String finish() {
        return service.getCorrelationId();
    }

}
