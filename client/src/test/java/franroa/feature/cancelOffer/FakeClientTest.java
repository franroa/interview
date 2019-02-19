package franroa.feature.cancelOffer;

import franroa.FakeClient;
import franroa.InterviewClient;
import franroa.api.OfferResponse;
import franroa.api.enums.Currency;
import franroa.exception.InterviewClientException;
import org.junit.Test;


import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;

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
    public void it_provides_a_method_to_check_if_cancelling_a_merchant_was_called_properly() throws InterviewClientException {
        Timestamp expiresAt = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 5 * 60 * 1000));
        FakeClient client = (FakeClient) createClient("");
        OfferResponse offer = client.fakeOffer("My Incredible Offer", 123L, Currency.EUR, expiresAt);

        client.cancelOffer(offer.id);

        assertThat(client.getCaughtCancelOfferCalls()).hasSize(1);
        assertThat(client.getCaughtCancelOfferCalls()).contains(0L);
    }
}
