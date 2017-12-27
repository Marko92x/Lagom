package mare.countries.impl.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Created by marko on 27.12.17..
 */
public interface CountryEvent extends Jsonable, AggregateEvent<CountryEvent> {

    AggregateEventTag<CountryEvent> TAG = AggregateEventTag.of(CountryEvent.class);

    @Override
    default AggregateEventTagger<CountryEvent> aggregateTag() {
        return TAG;
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class CountryChanged implements CountryEvent {
        public final String id;
        public final String name;

        @JsonCreator
        public CountryChanged(String id, String name) {
            this.id = Preconditions.checkNotNull(id, "ERROR!!!");
            this.name = Preconditions.checkNotNull(name, "ERROR!!!");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CountryChanged that = (CountryChanged) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("id", id)
                    .add("name", name)
                    .toString();
        }
    }
}
