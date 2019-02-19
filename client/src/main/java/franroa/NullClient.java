package franroa;


import franroa.api.OfferListResponse;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.exception.InterviewClientException;


public class NullClient implements InterviewClient {
    @Override
    public boolean ping() {
        return false;
    }

    @Override
    public OfferResponse createOffer(OfferRequest request, String correlationId) throws InterviewClientException {
        return new OfferResponse();
    }

    @Override
    public OfferListResponse getAllOffers() throws InterviewClientException {
        return new OfferListResponse();
    }

    @Override
    public OfferResponse getOffer(Long offerId) throws InterviewClientException {
        return new OfferResponse();
    }

    @Override
    public void cancelOffer(Long offerId) throws InterviewClientException {}
}
