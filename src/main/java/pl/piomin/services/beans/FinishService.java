package pl.piomin.services.beans;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FinishService {

    @Inject
    MiddleService service;

    public String finish() {
        return service.getCorrelationId();
    }

}
