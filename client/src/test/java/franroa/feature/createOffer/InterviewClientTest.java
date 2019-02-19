package franroa.feature.createOffer;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import franroa.InterviewClient;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.exception.InterviewClientException;
import franroa.helper.IntegrationTestCase;
import franroa.helper.factory.RequestFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public abstract class InterviewClientTest extends IntegrationTestCase {
    protected abstract InterviewClient createClient(String scenario);

    @Test
    public void if_a_request_succeeds__the_stored_offer_is_returned() {
        OfferRequest request = RequestFactory.offer();

        try {
            OfferResponse response = createClient("valid").createOffer(request, "my-correlation-id");

            assertThat(response.id).isNotNull();
            assertThat(response.name).isEqualTo(request.name);
            assertThat(response.currency).isEqualTo(request.currency);
            assertThat(response.price).isEqualTo(request.price);
            assertThat(response.expires_at).isEqualTo(request.expires_at);
        } catch (InterviewClientException e) {
            Assert.fail("InterviewClientException was thrown, but shouldn't");
        }
    }

    @Test
    public void if_there_is_a_connection_error_it_throws_an_exception() throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();

        try {
            createClient("connection-error").createOffer(request, "my-correlation-id");
        } catch (InterviewClientException exception) {

            assertThat(exception.getCause().getMessage()).isEqualTo("java.net.ConnectException: Connection refused (Connection refused)");
        }
    }

    @Test(expected = InterviewClientException.class)
    @DataProvider(value = {"name", "price", "currency", "expires_at"})
    public void some_fields_are_required(String field) throws InterviewClientException {
        OfferRequest request = RequestFactory.offer();

        try {
            OfferRequest.class.getField(field).set(request, null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Assert.fail("Could not set required field to null");
        }

        createClient("invalid").createOffer(request, "my-correlation-id");
    }
}
