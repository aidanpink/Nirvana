package galena.hats.services;

import galena.hats.HatType;
import java.util.Optional;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public interface IRenderHelper {

    Optional<HatType> getRendererHatType(HumanoidRenderState state);

}
