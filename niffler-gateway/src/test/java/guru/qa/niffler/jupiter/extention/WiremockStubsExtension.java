package guru.qa.niffler.jupiter.extention;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import guru.qa.niffler.jupiter.annotation.WiremockStubs;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WiremockStubsExtension implements BeforeTestExecutionCallback, AfterEachCallback {

    private final String basePath = "src/test/resources/wiremock/stubs/";
    private final WireMockServer wiremock = new WireMockServer(
        new WireMockConfiguration()
            .port(8093)
            .globalTemplating(true)
    );

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), WiremockStubs.class)
            .ifPresent(wiremockStubs -> {
                wiremock.start();
                for (String path : wiremockStubs.paths()) {
                    try {
                        String jsonContent =
                            new String(Files.readAllBytes(Paths.get(basePath + path)),StandardCharsets.UTF_8);
                        System.out.println(jsonContent);
                        StubMapping stubMapping = Json.read(jsonContent, StubMapping.class);
                        wiremock.addStubMapping(stubMapping);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        wiremock.shutdown();
    }
}
