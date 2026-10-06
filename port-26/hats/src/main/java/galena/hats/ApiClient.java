package galena.hats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import galena.hats.services.CoreServices;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiClient {

    private static final String BASE_URL = CoreServices.PLATFORM.isDev()
            ? "http://localhost:8080/api/"
            : "https://api.galena.wiki/api/";

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    private static final Gson GSON = new GsonBuilder()
            .setLenient()
            .create();

    public static CompletableFuture<Optional<SupporterData>> fetchSupporterData(UUID uuid) {
        URI uri;
        try {
            uri = new URI(BASE_URL + uuid.toString());
        } catch (URISyntaxException ex) {
            return CompletableFuture.failedFuture(ex);
        }

        var request = HttpRequest.newBuilder(uri)
                .GET()
                .timeout(TIMEOUT)
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(ApiClient::handleResponse);
    }

    private static Optional<SupporterData> handleResponse(HttpResponse<String> response) {
        var status = response.statusCode();

        if (status == 404) {
            return Optional.empty();
        }

        if (status != 200) {
            throw new IllegalStateException("API access failed with code " + status);
        }

        var json = GSON.fromJson(response.body(), JsonObject.class);
        var flags = json.getAsJsonArray("flags")
                .asList()
                .stream()
                .map(JsonElement::getAsString)
                .toList();

        var rank = json.get("rank").getAsInt();

        return Optional.of(new SupporterData(flags, rank));
    }

}
