package galena.hats.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.hats.HatConfigScreen;
import galena.hats.HatType;
import galena.hats.storage.ClientConfigStorage;
import java.util.List;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinCustomizationScreenMixin extends OptionsSubScreen {

    private SkinCustomizationScreenMixin(Screen screen, Options options, Component title) {
        super(screen, options, title);
    }

    @WrapOperation(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall(Ljava/util/List;)V"
            )
    )
    public void init(OptionsList instance, List<AbstractWidget> buttons, Operation<Void> original) {
        var uuid = ClientConfigStorage.getUUID().orElse(null);

        if (uuid != null && minecraft != null) {
            var allowed = HatType.allowed(uuid);
            if (!allowed.isEmpty()) {
                var button = Button.builder(
                        Component.translatable(HatConfigScreen.TRANSLATION_KEY),
                        $ -> minecraft.setScreen(new HatConfigScreen(this, allowed))
                ).build();
                buttons.add(button);
            }
        }

        original.call(instance, buttons);
    }

}
