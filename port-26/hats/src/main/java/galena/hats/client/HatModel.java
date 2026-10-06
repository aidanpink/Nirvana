package galena.hats.client;

import galena.hats.HatPart;
import java.util.Collection;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class HatModel<T extends HumanoidRenderState> extends EntityModel<T> {

    public HatModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T state) {
        super.setupAnim(state);

        // Copied from EntityModel
        root.xRot = state.xRot * ((float) Math.PI / 180F);
        root.yRot = state.yRot * ((float) Math.PI / 180F);
        if (state.isFallFlying) {
            root.xRot = (-(float) Math.PI / 4F);
        } else if (state.swimAmount > 0.0F) {
            root.xRot = Mth.rotLerpRad(state.swimAmount, root.xRot, (-(float) Math.PI / 4F));
        }
    }

    public void setupVisibleParts(Collection<HatPart> parts) {
        for (var part : HatPart.values()) {
            var modelPart = root.getChild(part.getSerializedName());
            modelPart.visible = parts.contains(part);
        }
    }
}
