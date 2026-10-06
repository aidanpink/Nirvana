package galena.hats.storage;

import galena.hats.ConfigData;
import galena.hats.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class ConfigStorage {

    private static final Map<UUID, ConfigData> others = new HashMap<>();

    public static void receive(UUID other, ConfigData data) {
        Constants.LOGGER.debug("Received remote config for player {}", other);
        synchronized (others) {
            others.put(other, data);
        }
    }

    public static Optional<ConfigData> getConfig(Player player) {
        if (player instanceof LocalPlayer) {
            return Optional.ofNullable(ClientConfigStorage.getLocalConfig());
        } else {
            return Optional.ofNullable(others.get(player.getUUID()));
        }
    }

    static Map<UUID, ConfigData> getConfigs() {
        return others;
    }

    static void clear() {
        others.clear();
    }

}
