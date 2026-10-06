package galena.hats.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import galena.hats.ConfigData;
import galena.hats.Constants;
import galena.hats.HatType;
import galena.hats.HatsApi;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.CommonServices;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;

public class ClientConfigStorage {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .create();

    private static ConfigData local = load();

    private static Path getPath() throws IOException {
        var minecraft = Minecraft.getInstance();
        var configFolder = minecraft.gameDirectory.toPath().resolve("config");
        Files.createDirectories(configFolder);
        return configFolder.resolve(Constants.MOD_ID + ".json");
    }

    private static ConfigData load() {
        try {
            if (!Files.exists(getPath())) return ConfigData.DEFAULT;
        } catch (IOException ex) {
            Constants.LOGGER.error("Failed to locate config", ex);
            return ConfigData.DEFAULT;
        }
        try (var reader = new FileReader(getPath().toFile())) {
            var json = GSON.fromJson(reader, JsonObject.class);
            return ConfigData.CODEC.parse(JsonOps.INSTANCE, json)
                    .result()
                    .orElseThrow(() -> new IllegalStateException("could not decode config data"));
        } catch (IOException | IllegalStateException | JsonSyntaxException ex) {
            Constants.LOGGER.error("Failed to load config");
            Constants.LOGGER.trace(ex);
            return ConfigData.DEFAULT;
        }
    }

    private static void save() {
        try {
            var path = getPath();
            var bytes = ConfigData.CODEC.encodeStart(JsonOps.INSTANCE, local)
                    .result()
                    .map(GSON::toJson)
                    .map(it -> it.getBytes(StandardCharsets.UTF_8))
                    .orElseThrow(() -> new IllegalStateException("could not encode config data"));

            Files.write(path, bytes);
        } catch (IOException | IllegalStateException | JsonSyntaxException ex) {
            Constants.LOGGER.error("Failed to save config", ex);
        }
    }

    private static void setLocalConfig(ConfigData data) {
        local = data;
        if (Minecraft.getInstance().getConnection() != null) {
            broadcastConfigUnchecked();
        }
        save();
    }

    private static void broadcastConfigUnchecked() {
        Constants.LOGGER.debug("Broadcasting local config to all other players");
        CommonServices.NETWORK.broadcastConfig(new ServerboundConfigMessage(local));
    }

    public static void broadcastConfig() {
        validate().thenAccept(ClientConfigStorage::setLocalConfig);
    }

    private static void modify(UnaryOperator<ConfigData> modifier) {
        setLocalConfig(modifier.apply(local));
    }

    public static void setHatType(HatType value) {
        modify(it -> new ConfigData(value, it.enabled()));
    }

    public static void setEnabled(boolean value) {
        modify(it -> new ConfigData(it.type(), value));
    }

    public static Optional<UUID> getUUID() {
        var minecraft = Minecraft.getInstance();
        var user = minecraft.getUser();
        return Optional.ofNullable(user.getProfileId());
    }

    public static ConfigData getLocalConfig() {
        return local;
    }

    private static CompletableFuture<ConfigData> validate() {
        var uuid = getUUID().orElse(null);
        if (uuid != null) {
            Constants.LOGGER.debug("Validating local config");
            return HatsApi.getAsyncSupporterData(uuid)
                    .thenApply(HatType::allowed)
                    .exceptionally($ -> Collections.emptyList())
                    .thenApply(ClientConfigStorage::processAllowedHats);
        }

        return CompletableFuture.failedFuture(new IllegalStateException("Tried to validate config before player was set"));
    }

    private static ConfigData processAllowedHats(List<HatType> allowed) {
        var local = getLocalConfig();

        if (allowed.isEmpty()) {
            Constants.LOGGER.debug("No hats allowed, disabling config");
            return new ConfigData(local.type(), false);
        }
        if (!allowed.contains(local.type())) {
            Constants.LOGGER.debug("Selected hat not allowed, disabling config");
            return new ConfigData(allowed.get(0), false);
        }

        Constants.LOGGER.debug("Config valid");

        return local;
    }

}
