package franroa.feature.fetchOneOffer;

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
    public void it_provides_the_feature_to_fake_offers() throws InterviewClientException {
        Timestamp expiresAt = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 5 * 60 * 1000));
        FakeClient client = (FakeClient) createClient("");
        OfferResponse offer = client.fakeOffer("My Incredible Offer", 123L, Currency.EUR, expiresAt);

        OfferResponse response = client.getOffer(offer.id);

        assertThat(response.id).isEqualTo(offer.id);
        assertThat(response.name).isEqualTo("My Incredible Offer");
        assertThat(response.price).isEqualTo(123L);
        assertThat(response.currency).isEqualTo(Currency.EUR.toString());
        assertThat(response.expires_at).isEqualTo(expiresAt.toString());
    }

    @Test
    public void it_provides_a_method_to_check_if_fetching_a_merchant_was_called_properly() throws InterviewClientException {
        Timestamp expiresAt = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 5 * 60 * 1000));
        FakeClient client = (FakeClient) createClient("");
        OfferResponse offer = client.fakeOffer("My Incredible Offer", 123L, Currency.EUR, expiresAt);

        OfferResponse response = client.getOffer(offer.id);

        assertThat(client.getCaughtFetchOfferCalls()).hasSize(1);
        assertThat(client.getCaughtFetchOfferCalls()).contains(offer.id);
    }
}
