package franroa.feature.cancelOffer;

import franroa.InterviewClient;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.exception.InterviewClientException;
import franroa.helper.IntegrationTestCase;
import franroa.helper.factory.RequestFactory;
import org.junit.Assert;
import org.junit.Test;

public abstract class InterviewClientTest extends IntegrationTestCase {
    protected abstract InterviewClient createClient(String scenario);

    @Test
    public void deleting_the_offer() throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();
        InterviewClient client = createClient("valid");
        OfferResponse offer = client.createOffer(request, "my-correlation-id");

        client.cancelOffer(offer.id);

        try {
            client.getOffer(offer.id);
            Assert.fail("The offer should not be queryable");
        } catch (InterviewClientException exception) {
            int tes = 1;
            return;
        }
    }

    @Test(expected = InterviewClientException.class)
    public void if_there_is_a_connection_error_it_throws_an_exception() throws InterviewClientException {
        createClient("connection-error").cancelOffer(155L);
    }
}
