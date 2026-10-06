package galena.hats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import galena.hats.services.CoreServices;
import galena.hats.storage.ServerConfigStorage;
import net.minecraft.commands.CommandSourceStack;

public class HatsCommand {

    private static <T> void  register(CommandDispatcher<T> dispatcher, Command<T> refreshCommand) {
        if(!CoreServices.PLATFORM.isDev()) return;

        dispatcher.register(LiteralArgumentBuilder.<T>literal("galena-hats")
                .then(LiteralArgumentBuilder.<T>literal("refresh").executes(refreshCommand))
        );
    }

    public static void registerServer(CommandDispatcher<CommandSourceStack> dispatcher) {
        register(dispatcher, context ->  {
            ServerConfigStorage.clear();
            HatsApi.clear();
            return 1;
        });
    }

    public static <T> void registerClient(CommandDispatcher<T> dispatcher) {
        register(dispatcher, context ->  {
            HatsApi.clear();
            return 1;
        });
    }

}
