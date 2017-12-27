package mare.countries.impl.readside;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaReadSide;
import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import mare.countries.api.Country;
import mare.countries.impl.Countries;
import mare.countries.impl.entity.CountryEvent;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by marko on 27.12.17..
 */
@Singleton
public class CountriesController {

    private static final String SELECT_ALL_QUERY = "Select c From Countries c";

    private final JpaSession jpaSession;

    @Inject
    CountriesController(JpaSession jpaSession, ReadSide readSide) {
        this.jpaSession = jpaSession;
        readSide.register(CountriesRecordWriter.class);
    }

    public CompletionStage<PSequence<Country>> all() {
        return jpaSession.withTransaction(this::selectAllCountries)
                .thenApply(TreePVector::from);
    }

    private List<Country> selectAllCountries(EntityManager entityManager) {
        List<Countries> c =  entityManager
                .createQuery(SELECT_ALL_QUERY, Countries.class)
                .getResultList();
        List<Country> cy = new ArrayList<>();
        for (Countries countries : c) {
            cy.add(new Country(countries.getCountryName()));
        }
        return cy;
    }

    static class CountriesRecordWriter extends ReadSideProcessor<CountryEvent> {

        private final JpaReadSide jpaReadSide;

        @Inject
        CountriesRecordWriter (JpaReadSide jpaReadSide) {
            this.jpaReadSide = jpaReadSide;
        }

        @Override
        public ReadSideHandler<CountryEvent> buildHandler() {
            return jpaReadSide.<CountryEvent>builder("CountriesRecordWriter")
                    .setEventHandler(CountryEvent.CountryChanged.class, this::processCountryChanged)
                    .build();
        }

        private void processCountryChanged(EntityManager entityManager, CountryEvent.CountryChanged countryChanged) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            CountriesRecord record = entityManager.find(CountriesRecord.class, countryChanged.id);
            if (record == null) {
                record = new CountriesRecord();
                record.setId(15689);
                entityManager.persist(record);
            }
            record.setCountryName(countryChanged.name);
        }

        @Override
        public PSequence<AggregateEventTag<CountryEvent>> aggregateTags() {
            return TreePVector.singleton(CountryEvent.TAG);
        }
    }
}
