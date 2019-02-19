package franroa;

import franroa.api.OfferListResponse;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.config.InterviewClientConfiguration;
import franroa.exception.InterviewClientException;
import franroa.simplehttp.SimpleHttp;


public class HttpClient implements InterviewClient {
    SimpleHttp client;

    public HttpClient(InterviewClientConfiguration config) {
        client = new SimpleHttp(config.host, "correlation-id", "key");
    }

    @Override
    public boolean ping() {
        try {
            return client.get("ping").getStatus() == 200;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    @Override
    public OfferResponse createOffer(OfferRequest request, String correlationId) throws InterviewClientException {
        try {
            return client.post("v1/offers", request).asDto(OfferResponse.class);
        } catch (RuntimeException exception) {
            throw new InterviewClientException();
        }
    }

    @Override
    public OfferListResponse getAllOffers() throws InterviewClientException {
        try {
            return client.get("v1/offers/").asDto(OfferListResponse.class);
        } catch (RuntimeException exception) {
            throw new InterviewClientException();
        }
    }

    @Override
    public OfferResponse getOffer(Long offerId) throws InterviewClientException {
        try {
            return client.get("v1/offers/" + offerId).asDto(OfferResponse.class);
        } catch (RuntimeException exception) {
            throw new InterviewClientException();
        }
    }

    @Override
    public void cancelOffer(Long offerId) throws InterviewClientException {
        try {
            client.delete("v1/offers/" + offerId);
        } catch (RuntimeException exception) {
            throw new InterviewClientException();
        }
    }
}
