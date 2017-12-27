package mare.countries.impl.entity;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by marko on 27.12.17..
 */
public class ContryEntity extends PersistentEntity<CountryCommand, CountryEvent, CountryState> {

    @Override
    public Behavior initialBehavior(Optional<CountryState> optional) {
        BehaviorBuilder b = newBehaviorBuilder(
                optional.orElse(new CountryState("Tunguzija", LocalDateTime.now().toString())));

        b.setCommandHandler(CountryCommand.InsertContry.class, (cdm, ctx) ->
                ctx.thenPersist(new CountryEvent.CountryChanged(entityId(), cdm.name),
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(CountryEvent.CountryChanged.class,
                evt -> new CountryState(evt.name, LocalDateTime.now().toString()));

        b.setReadOnlyCommandHandler(CountryCommand.Country.class,
                (cmd, ctx) -> ctx.reply(new Message(200, "ReadOnlyComand")));

        return b.build();

    }
}
