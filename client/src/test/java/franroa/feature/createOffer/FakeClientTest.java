package franroa.feature.createOffer;

import franroa.FakeClient;
import franroa.InterviewClient;
import franroa.api.OfferRequest;
import franroa.exception.InterviewClientException;
import franroa.helper.factory.RequestFactory;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;


public class FakeClientTest extends InterviewClientTest {
    @Override
    protected boolean shouldStartApp() {
        return false;
    }

    @Override
    protected InterviewClient createClient(String scenario) {
        return createFakeClient(scenario);
    }

    @Test
    public void it_provides_a_method_to_get_the_caught_request() throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();
        FakeClient client = new FakeClient();

        client.createOffer(request, "my-correlation-id");

        assertThat(client.getCaughtRequest()).isSameAs(request);
    }

    @Test
    public void it_provides_a_method_to_get_the_caught_correlation_id() throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();
        FakeClient client = new FakeClient();

        client.createOffer(request, "my-correlation-id");

        assertThat(client.getCaughtCorrelationId()).isEqualTo("my-correlation-id");
    }
}
