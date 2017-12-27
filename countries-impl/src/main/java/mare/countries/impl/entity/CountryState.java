package mare.countries.impl.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;

import javax.annotation.Nullable;

/**
 * Created by marko on 27.12.17..
 */
public final class CountryState implements CompressedJsonable {


    public final String name;
    public final String timestamp;

    @JsonCreator
    CountryState(String name, String timestamp) {
        this.name = Preconditions.checkNotNull(name, "ERROR!!!");
        this.timestamp = Preconditions.checkNotNull(timestamp, "ERROR!!!");
    }

    @Override
    public boolean equals(@Nullable Object another) {
        if (this == another)
            return true;
        return another instanceof CountryState && equalTo((CountryState) another);
    }

    private boolean equalTo(CountryState another) {
        return name.equals(another.name) && timestamp.equals(another.timestamp);
    }

    @Override
    public int hashCode() {
        int h = 31;
        h = h * 17 + name.hashCode();
        h = h * 17 + timestamp.hashCode();
        return h;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("HelloState").add("name", name).add("timestamp", timestamp).toString();
    }
}
