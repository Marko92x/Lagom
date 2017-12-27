package mare.countries.impl.entity;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import java.util.Optional;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * Created by marko on 27.12.17..
 */
public interface CountryCommand extends Jsonable {

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class InsertContry implements CountryCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        final String name;

        @JsonCreator
        public InsertContry (String name) {
            this.name = Preconditions.checkNotNull(name, "ERROR!!!");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InsertContry that = (InsertContry) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("InsertContry").add("name", name).toString();
        }
    }

    @SuppressWarnings("serial")
    @Immutable
    @JsonDeserialize
    final class Country implements CountryCommand, PersistentEntity.ReplyType<Message> {
        final String name;
        final Optional<String> organization;

        @JsonCreator
        public Country(String name, Optional<String> organization) {
            this.name = Preconditions.checkNotNull(name, "name");
            this.organization = Preconditions.checkNotNull(organization, "organization");
        }

        @Override
        public boolean equals(@Nullable Object another) {
            if (this == another)
                return true;
            return another instanceof Country && equalTo((Country) another);
        }

        private boolean equalTo(Country another) {
            return name.equals(another.name) && organization.equals(another.organization);
        }

        @Override
        public int hashCode() {
            int h = 31;
            h = h * 17 + name.hashCode();
            h = h * 17 + organization.hashCode();
            return h;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("Hello").add("name", name).add("organization", organization).toString();
        }
    }
}
