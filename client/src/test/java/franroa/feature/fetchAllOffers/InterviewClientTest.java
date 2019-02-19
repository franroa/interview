package franroa.feature.fetchAllOffers;

import franroa.InterviewClient;
import franroa.api.OfferListResponse;
import franroa.api.OfferRequest;
import franroa.exception.InterviewClientException;
import franroa.helper.IntegrationTestCase;
import franroa.helper.factory.RequestFactory;
import org.junit.Test;


import static org.assertj.core.api.Java6Assertions.assertThat;

public abstract class InterviewClientTest extends IntegrationTestCase {
    protected abstract InterviewClient createClient(String scenario);

    @Test
    public void fetching_all_offers() throws InterviewClientException {
        OfferRequest requestOne = RequestFactory.offer();
        OfferRequest requestTwo = RequestFactory.offer();
        InterviewClient client = createClient("valid");
        client.createOffer(requestOne, "my-correlation-id");
        client.createOffer(requestTwo, "my-correlation-id-2");

        OfferListResponse response = client.getAllOffers();

        assertThat(response.offers.size()).isGreaterThan(1);
    }

    @Test
    public void if_there_is_a_connection_error_it_throws_an_exception() throws InterviewClientException {
        try {
            createClient("connection-error").getAllOffers();
        } catch (InterviewClientException exception) {

            assertThat(exception.getCause().getMessage()).isEqualTo("java.net.ConnectException: Connection refused (Connection refused)");
        }
    }
}
