package mare.countries.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import mare.countries.api.CountryService;

/**
 * Created by marko on 26.12.17..
 */
public class CountryModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        bindServices(serviceBinding(CountryService.class, CountryServiceImpl.class));
    }
}
