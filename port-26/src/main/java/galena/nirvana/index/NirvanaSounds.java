package galena.nirvana.index;

import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.registration.RegistryEntry;
import galena.nirvana.NirvanaConstants;
import galena.nirvana.platform.Services;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class NirvanaSounds {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final RegistryEntry<SoundEvent, SoundEvent> JAM_SOUND = REGISTRATE
            .sound("music.disc.jam")

            .register();

    public static final ResourceKey<JukeboxSong> JAM_KEY = ResourceKey.create(Registries.JUKEBOX_SONG, NirvanaConstants.createId("jam"));

    public static final RegistryEntry<SoundEvent, SoundEvent> BONG = REGISTRATE
            .sound("item.use.bong")

            .register();

    public static final RegistryEntry<SoundEvent, SoundEvent> SMOKING = REGISTRATE
            .sound("item.use.smoking")

            .register();

    public static final RegistryEntry<SoundEvent, SoundEvent> HERBAL_SALVE = REGISTRATE
            .sound("item.use.herbal_salve")

            .register();

    public static void register() {
        // loads this class
    }

}
