package galena.hats.fabric.services;

import galena.hats.Constants;
import galena.hats.HatType;
import galena.hats.services.IRenderHelper;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class FabricRenderHelper implements IRenderHelper {

    public static final RenderStateDataKey<Optional<HatType>> CONTEXT_KEY =
            RenderStateDataKey.create(Constants.createId("hat_type")::toString);

    @Override
    public Optional<HatType> getRendererHatType(HumanoidRenderState state) {
        return state.getDataOrDefault(CONTEXT_KEY, Optional.empty());
    }

}
