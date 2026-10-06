package galena.hats;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HatsApi {

    private static final Map<UUID, SupporterData> data = new HashMap<>();

    public static boolean isLoaded(UUID uuid) {
        synchronized (data) {
            return data.containsKey(uuid);
        }
    }

    static void clear() {
        synchronized (data) {
            data.clear();
        }
    }

    private static CompletableFuture<Optional<SupporterData>> getAsyncOptionalSupporterData(UUID uuid) {
        if (isLoaded(uuid)) return CompletableFuture.completedFuture(getLoadedData(uuid));
        return load(uuid);
    }

    public static CompletableFuture<SupporterData> getAsyncSupporterData(UUID uuid) {
        return getAsyncOptionalSupporterData(uuid).thenApply(optional -> {
           if(optional.isEmpty()) throw new HatNotAllowedException();
           return optional.get();
        });
    }

    public static Optional<SupporterData> getSupporterData(UUID uuid) {
        if (!isLoaded(uuid)) load(uuid);
        return getLoadedData(uuid);
    }

    private static Optional<SupporterData> getLoadedData(UUID uuid) {
        synchronized (data) {
            return Optional.ofNullable(data.get(uuid));
        }
    }

    private static CompletableFuture<Optional<SupporterData>> load(UUID uuid) {
        return ApiClient.fetchSupporterData(uuid).thenApply((value) -> {
            synchronized (data) {
                data.put(uuid, value.orElse(null));
            }
            return value;
        });
    }

}
