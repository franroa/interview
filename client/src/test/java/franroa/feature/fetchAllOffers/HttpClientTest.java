package franroa.feature.fetchAllOffers;


import franroa.InterviewClient;

public class HttpClientTest extends InterviewClientTest {
    @Override
    protected InterviewClient createClient(String scenario) {
        return createHttpClient(scenario);
    }
}
