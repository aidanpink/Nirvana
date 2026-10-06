package galena.hats.services;

import java.util.ServiceLoader;

public class CoreServices {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    static <T> T load(Class<T> clazz) {
        var classLoader = CoreServices.class.getClassLoader();
        return ServiceLoader.load(clazz, classLoader)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

}