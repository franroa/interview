package franroa;

import franroa.container.Container;
import franroa.exceptions.mappers.ResourceNotFoundExceptionMapper;
import franroa.helper.TestResponse;
import franroa.queue.Queue;
import franroa.queue.QueueFactory;
import franroa.resources.OfferResource;
import franroa.resources.PingResource;
import franroa.testing.helper.TestRequest;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.logging.LogFactory;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;


import javax.validation.Validation;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class TestCase {
    protected static OfferConfiguration config;
    private static boolean migrated = false;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new OfferResource())
            .addResource(new PingResource())
            .addResource(new ResourceNotFoundExceptionMapper())
            .build();

    @Before
    public void setUp() throws Exception {
        loadConfiguration();
        openDatabaseConnection();
        migrate();
        Container.instance(OfferConfiguration.class, config);
        Container.instance(Queue.class, new QueueFactory(config.getQueueConfiguration()).create());
        Base.openTransaction();
    }

    @After
    public void tearDown() throws Exception {
        Base.rollbackTransaction();
        closeDatabaseConnection();
        Container.clear();
    }

    private static void openDatabaseConnection() {
//        Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost:54322/interviewDB-test", "sa", "sa"); // TODO -> test in local
        Base.open("org.postgresql.Driver", "jdbc:postgresql://172.17.0.3:5432/interviewDB-test", "sa", "sa"); // TODO -> test in jenkins
    }

    private static void loadConfiguration() {
        if (config == null) {
            try {
                config = (OfferConfiguration) (new YamlConfigurationFactory(OfferConfiguration.class, Validation.buildDefaultValidatorFactory().getValidator(), Jackson.newObjectMapper(), "dw")).build(new File(ResourceHelpers.resourceFilePath("config.yml")));
            } catch (IOException | ConfigurationException var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    private static void closeDatabaseConnection() {
        Base.close();
    }

    private static void migrate() throws LiquibaseException, SQLException {
        if (!migrated) {
            LogFactory.getInstance().getLog().setLogLevel(LogLevel.WARNING);
            Liquibase liquibase = new Liquibase("changelog/master.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(Base.connection()));
            liquibase.dropAll();
            liquibase.update("");
            Base.connection().setAutoCommit(true);
            migrated = true;
        }
    }

    protected <T> TestResponse post(String target, TestRequest request) {
        return new TestResponse(requestTo(target).post(Entity.json(request.getJson())));
    }

    protected <T> TestResponse get(String target) {
        return new TestResponse(requestTo(target).get());
    }

    protected <T> TestResponse delete(String target) {
        return new TestResponse(requestTo(target).delete());
    }

    private Invocation.Builder requestTo(String target) {
        return resources.client().target(target).request();
    }
}
