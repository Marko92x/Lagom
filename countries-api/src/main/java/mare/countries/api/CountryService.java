package mare.countries.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import mare.countries.api.Country;
import org.pcollections.PSequence;


import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by marko on 26.12.17..
 */
public interface CountryService extends Service {

    ServiceCall<NotUsed, PSequence<Country>> getCountry();

    ServiceCall<Country, Done> insertCountry();

    @Override
    default  Descriptor descriptor() {
        return named("countries").withCalls(
                pathCall("/countries", this::getCountry),
                pathCall("/countries", this::insertCountry)
        ).withAutoAcl(true);
    }
}
