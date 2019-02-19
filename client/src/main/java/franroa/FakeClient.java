package franroa;


import franroa.api.OfferListResponse;
import franroa.api.OfferRequest;
import franroa.api.OfferResponse;
import franroa.api.enums.Currency;
import franroa.exception.InterviewClientException;
import franroa.simplehttp.UnableToReadEntityException;


import javax.validation.Validation;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class FakeClient implements InterviewClient {
    private OfferRequest caughtRequest;
    private String caughtCorrelationId;
    private OfferListResponse fakeOffers = new OfferListResponse();
    public boolean fakeConnectionError = false;
    private List<Long> caughtFetchOfferCalls = new ArrayList<>();
    private int caughtFetchAllOfferCalls;
    private List<Long> caughtCancelOfferCalls = new ArrayList<>();

    @Override
    public boolean ping() {
        return !fakeConnectionError;
    }

    @Override
    public OfferResponse createOffer(OfferRequest request, String correlationId) throws InterviewClientException {
        guard(request);

        caughtRequest = request;
        caughtCorrelationId = correlationId;

        OfferResponse response = new OfferResponse();
        response.id = (long) fakeOffers.offers.size();
        response.currency = request.currency;
        response.name = request.name;
        response.price = request.price;
        response.expires_at = request.expires_at;

        fakeOffers.offers.add(response);

        return response;
    }

    @Override
    public OfferListResponse getAllOffers() throws InterviewClientException {
        caughtFetchAllOfferCalls++;

        guard();

        return fakeOffers;
    }

    @Override
    public OfferResponse getOffer(Long offerId) throws InterviewClientException {
        caughtFetchOfferCalls.add(offerId);

        guard();

        if (fakeOffers.offers.size() == 0) {
            throw new InterviewClientException(new UnableToReadEntityException("The entity could not be read"));
        }

        return fakeOffers.offers.get(Integer.valueOf(String.valueOf(offerId)));
    }

    @Override
    public void cancelOffer(Long offerId) throws InterviewClientException {
        caughtCancelOfferCalls.add(offerId);

        guard();

        try {
            fakeOffers.offers.remove(fakeOffers.offers.get(Integer.valueOf(String.valueOf(offerId))));
        } catch (Exception e) {
            throw new InterviewClientException(e);
        }
    }

    public OfferRequest getCaughtRequest() {
        return caughtRequest;
    }

    public String getCaughtCorrelationId() {
        return caughtCorrelationId;
    }

    private void guard() throws InterviewClientException {
        if (fakeConnectionError) {
            throw new InterviewClientException(new ConnectException("java.net.ConnectException: Connection refused (Connection refused)"));
        }
    }

    private void guard(Object request) throws InterviewClientException {
        guard();

        if (!Validation.buildDefaultValidatorFactory().getValidator().validate(request).isEmpty()) {
            throw new InterviewClientException();
        }
    }

    public OfferResponse fakeOffer(String name, long price, Currency currency, Timestamp expiresAt) {
        OfferResponse offer = new OfferResponse();

        offer.id = (long) fakeOffers.offers.size();
        offer.name = name;
        offer.price = price;
        offer.currency = currency.toString();
        offer.expires_at = expiresAt.toString();

        fakeOffers.offers.add(offer);

        return offer;
    }

    public List<Long> getCaughtFetchOfferCalls() {
        return caughtFetchOfferCalls;
    }

    public int getNrCaughtFetchAllOfferCalls() {
        return caughtFetchAllOfferCalls;
    }

    public List<Long> getCaughtCancelOfferCalls() {
        return caughtCancelOfferCalls;
    }
}
