package franroa.helper;

import com.google.common.io.Resources;
import franroa.FakeClient;
import franroa.HttpClient;
import franroa.config.InterviewClientConfiguration;
import franroa.config.SupportedClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public abstract class IntegrationTestCase {
    private Process interview;

    @Before
    public void setUp() {
        cleanup();
        copyApplicationJar();
        startApplication();
    }

    @After
    public void tearDown() {
        cleanup();
    }

    protected HttpClient createHttpClient(String scenario) {
        InterviewClientConfiguration config = new InterviewClientConfiguration();

        if (scenario.equals("connection-error")) {
            config.host = "http://localhost:8228";
        } else {
            config.host = "http://localhost:8585";
        }

        config.client = SupportedClients.HTTP;

        return new HttpClient(config);
    }

    protected FakeClient createFakeClient(String scenario) {
        FakeClient client = new FakeClient();

        if (scenario.equals("connection-error")) {
            client.fakeConnectionError = true;
        }

        return client;
    }

    private void startApplication() {
        if (!shouldStartApp()) return;

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "application.jar", "server", "config.yml");
        pb.directory(new File(resourceFilePath("interviewclient")));
        try {
            interview = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(interview.getInputStream()));

            while (!reader.readLine().contains("org.eclipse.jetty.server.Server: Started")) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyApplicationJar() {
        if (!shouldStartApp()) return;

        try {
            String interviewVersion = "0.0.1";
            String source = String.format(
                    "%s/.m2/repository/net/franroa/interview/interview-application/%s/interview-application-%s.jar",
                    System.getProperty("user.home"),
                    interviewVersion,
                    interviewVersion
            );
            String destination = resourceFilePath("interviewclient") + "/application.jar";

            Files.copy(new File(source).toPath(), new File(destination).toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            Assert.fail("Could not copy interview-application jar from Maven repository: " + e.getMessage());
        }
    }

    private void cleanup() {
        if (interview != null && interview.isAlive()) {
            interview.destroyForcibly();
        }
    }

    protected boolean shouldStartApp() {
        return true;
    }

    public static String resourceFilePath(String resourceClassPathLocation) {
        try {
            return (new File(Resources.getResource(resourceClassPathLocation).toURI())).getAbsolutePath();
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }
}
