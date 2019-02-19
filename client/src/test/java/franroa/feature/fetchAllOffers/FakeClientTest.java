package franroa.feature.fetchAllOffers;

import franroa.FakeClient;
import franroa.InterviewClient;
import franroa.api.OfferListResponse;
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
    public void it_provides_the_feature_to_fake_many_offers() throws InterviewClientException {
        Timestamp expiresAtOne = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 5 * 60 * 1000));
        Timestamp expiresAtTwo = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 60 * 1000));
        Timestamp expiresAtThree = Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 520 * 60 * 1000));
        FakeClient client = (FakeClient) createClient("");
        OfferResponse offerOne = client.fakeOffer("My Incredible Offer", 123L, Currency.EUR, expiresAtOne);
        OfferResponse offerTwo = client.fakeOffer("Televisor", 400L, Currency.EUR, expiresAtTwo);
        OfferResponse offerThree = client.fakeOffer("HeadPhones", 12L, Currency.USD, expiresAtThree);

        OfferListResponse response = client.getAllOffers();

        assertThat(response.offers.get(0).id).isEqualTo(offerOne.id);
        assertThat(response.offers.get(0).name).isEqualTo("My Incredible Offer");
        assertThat(response.offers.get(0).price).isEqualTo(123L);
        assertThat(response.offers.get(0).currency).isEqualTo(Currency.EUR.toString());
        assertThat(response.offers.get(0).expires_at).isEqualTo(expiresAtOne.toString());
        assertThat(response.offers.get(1).id).isEqualTo(offerTwo.id);
        assertThat(response.offers.get(1).name).isEqualTo("Televisor");
        assertThat(response.offers.get(1).price).isEqualTo(400L);
        assertThat(response.offers.get(1).currency).isEqualTo(Currency.EUR.toString());
        assertThat(response.offers.get(1).expires_at).isEqualTo(expiresAtTwo.toString());
        assertThat(response.offers.get(2).id).isEqualTo(offerThree.id);
        assertThat(response.offers.get(2).name).isEqualTo("HeadPhones");
        assertThat(response.offers.get(2).price).isEqualTo(12L);
        assertThat(response.offers.get(2).currency).isEqualTo(Currency.USD.toString());
        assertThat(response.offers.get(2).expires_at).isEqualTo(expiresAtThree.toString());
    }

    @Test
    public void it_provides_a_method_to_check_if_fetching_a_merchant_was_called_properly() throws InterviewClientException {
        FakeClient client = (FakeClient) createClient("");

        client.getAllOffers();
        client.getAllOffers();

        assertThat(client.getNrCaughtFetchAllOfferCalls()).isEqualTo(2);
    }
}
