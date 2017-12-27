package mare.it;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.lightbend.lagom.javadsl.client.integration.LagomClientFactory;
import mare.countries.api.GreetingMessage;
import mare.countries.api.HelloService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StreamIT {

    private static final String SERVICE_LOCATOR_URI = "http://localhost:8000";

    private static LagomClientFactory clientFactory;
    private static HelloService helloService;
    private static ActorSystem system;
    private static Materializer mat;

    @BeforeClass
    public static void setup() {
        clientFactory = LagomClientFactory.create("integration-test", StreamIT.class.getClassLoader());
        // One of the clients can use the service locator, the other can use the service gateway, to test them both.
        helloService = clientFactory.createDevClient(HelloService.class, URI.create(SERVICE_LOCATOR_URI));

        system = ActorSystem.create();
        mat = ActorMaterializer.create(system);
    }

    @Test
    public void helloWorld() throws Exception {
        String answer = await(helloService.hello("foo").invoke());
        assertEquals("Hello, foo!", answer);
        await(helloService.useGreeting("bar").invoke(new GreetingMessage("Hi")));
        String answer2 = await(helloService.hello("bar").invoke());
        assertEquals("Hi, bar!", answer2);
    }

    private <T> T await(CompletionStage<T> future) throws Exception {
        return future.toCompletableFuture().get(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        if (clientFactory != null) {
            clientFactory.close();
        }
        if (system != null) {
            system.terminate();
        }
    }




}
