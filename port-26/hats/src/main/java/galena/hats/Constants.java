package galena.hats;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

    public static final String MOD_ID = "galena_hats";

    public static final Logger LOGGER = LogManager.getLogger("Galena Hats");

    public static Identifier createId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}
