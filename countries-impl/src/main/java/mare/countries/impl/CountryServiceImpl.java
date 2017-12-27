package mare.countries.impl;

import akka.Done;
import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import mare.countries.api.Country;
import mare.countries.api.CountryService;
import mare.countries.impl.entity.ContryEntity;
import mare.countries.impl.entity.CountryCommand;
import mare.countries.impl.readside.CountriesController;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by marko on 26.12.17..
 */
public class CountryServiceImpl implements CountryService {

    private final PersistentEntityRegistry persistentEntities;
    private final CountriesController countriesController;

    @Inject
    public CountryServiceImpl(PersistentEntityRegistry persistentEntities, CountriesController countriesController) {
        this.persistentEntities = persistentEntities;
        this.countriesController = countriesController;
        persistentEntities.register(ContryEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, PSequence<Country>> getCountry() {
        return request -> countriesController.all();
    }

    @Override
    public ServiceCall<Country, Done> insertCountry() {
        return request -> {
            UUID uuid = UUID.randomUUID();
            PersistentEntityRef<CountryCommand> ref  = persistentEntities.refFor(ContryEntity.class, uuid.toString());
            System.out.println("#############################################");
            return ref.ask(new CountryCommand.InsertContry(request.getName()));
        };
    }
}
