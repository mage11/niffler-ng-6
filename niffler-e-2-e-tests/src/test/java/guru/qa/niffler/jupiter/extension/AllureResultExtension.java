package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AllureResultExtension implements SuiteExtension {

    private final String allureDockerApi = System.getenv("ALLURE_DOCKER_API");
    private final String executionType = System.getenv("EXECUTION_TYPE");
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    private static final Logger LOG = LoggerFactory.getLogger(AllureResultExtension.class);

    @Override
    public void afterSuite() {
        LOG.info("allureDockerApi: " + allureDockerApi);
        LOG.info("executionType: " + executionType);
        if(!allureDockerApi.equals("") && allureDockerApi != null) {
            sendAllureResults();
        }
    }

    private void sendAllureResults() {
        String resultsDirectory = "/niffler/niffler-e-2-e-tests/build/allure-results";

        File resultsDir = new File(resultsDirectory);
        File[] files = resultsDir.listFiles();
        if (files == null) {
            System.out.println("No files found in the directory.");
            return;
        }

        List<Result> results = new ArrayList<>();
        for (File file : files) {
            Result result = new Result();
            String filePath = file.getAbsolutePath();
            System.out.println(filePath);

            if (file.isFile()) {
                try {
                    byte[] content = Files.readAllBytes(file.toPath());
                    if (content.length > 0) {
                        String base64Content = Base64.getEncoder().encodeToString(content);
                        result.setFileName(file.getName());
                        result.setContentBase64(base64Content);
                        results.add(result);
                    } else {
                        System.out.println("Empty File skipped: " + filePath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Directory skipped: " + filePath);
            }
        }

        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(
                OBJECT_MAPPER.writeValueAsString(new RequestBodyWrapper(results)),
                MediaType.parse("application/json")
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //SEND-RESULTS
        LOG.info("SEND-RESULTS");
        final String sendResultApi = "/allure-docker-service/send-results";
        Request sendResultsRequest = new Request.Builder()
            .url(allureDockerApi + sendResultApi + "?project_id=" + "niffler" + "&force_project_creation=true")
            .post(requestBody)
            .build();

        try {
            Response response =  HTTP_CLIENT.newCall(sendResultsRequest).execute();
            LOG.info("response: " + response.body().string());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        //GENERATE-REPORT
        LOG.info("GENERATE-REPORT");
        String executionName = "docker_execution";
        String executionFrom = "from_docker";
        Request generateReportRequest = new Request.Builder()
            .url(allureDockerApi + "/allure-docker-service/generate-report?project_id=" + "niffler" +
                "&execution_name=" + executionName +
                "&execution_from=" + executionFrom +
                "&execution_type=" + executionType)
            .get()
            .build();

        try {
            Response response = HTTP_CLIENT.newCall(generateReportRequest).execute();
            LOG.info("response: " + response.body().string());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class Result {
        @JsonProperty("file_name")
        private String fileName;
        @JsonProperty("content_base64")
        private String contentBase64;

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setContentBase64(String contentBase64) {
            this.contentBase64 = contentBase64;
        }
    }

    static class RequestBodyWrapper {
        private List<Result> results;

        public RequestBodyWrapper(List<Result> results) {
            this.results = results;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }
    }
}
