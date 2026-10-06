package galena.hats;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum HatPart implements StringRepresentable {
    ARROW,
    PLANT,
    EARS,
    RIM;

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
