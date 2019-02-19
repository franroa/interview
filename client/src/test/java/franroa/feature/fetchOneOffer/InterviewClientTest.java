package franroa.feature.fetchOneOffer;

import franroa.InterviewClient;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.exception.InterviewClientException;
import franroa.helper.IntegrationTestCase;
import franroa.helper.factory.RequestFactory;
import org.junit.Assert;
import org.junit.Test;


import static org.assertj.core.api.Java6Assertions.assertThat;

public abstract class InterviewClientTest extends IntegrationTestCase {
    protected abstract InterviewClient createClient(String scenario);

    @Test
    public void fetching_the_offer() throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();
        InterviewClient client = createClient("valid");
        OfferResponse offer = client.createOffer(request, "my-correlation-id");

        OfferResponse response = client.getOffer(offer.id);

        assertThat(response.id).isNotNull();
        assertThat(response.name).isEqualTo(request.name);
        assertThat(response.price).isEqualTo(request.price);
        assertThat(response.currency).isEqualTo(request.currency);
        assertThat(response.expires_at).isEqualTo(request.expires_at);
    }

    @Test
    public void if_the_offer_does_not_exists_it_throws_an_exception() {
        InterviewClient client = createClient("valid");
        Long invalidOfferId = 2345243L;

        try {
            client.getOffer(invalidOfferId);
            Assert.fail("The offer should not be queryable");

        } catch (InterviewClientException exception) {
            assertThat(exception.getMessage()).isEqualTo(
                    "franroa.simplehttp.UnableToReadEntityException: The entity could not be read"
            );
        }
    }

    @Test
    public void if_there_is_a_connection_error_it_throws_an_exception() throws InterviewClientException {
        try {
            createClient("connection-error").getOffer(155L);
        } catch (InterviewClientException exception) {

            assertThat(exception.getCause().getMessage()).isEqualTo("java.net.ConnectException: Connection refused (Connection refused)");
        }
    }
}
