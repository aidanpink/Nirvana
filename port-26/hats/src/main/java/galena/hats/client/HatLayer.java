package galena.hats.client;

import com.mojang.blaze3d.vertex.PoseStack;
import galena.hats.Constants;
import galena.hats.services.CommonServices;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class HatLayer<T extends HumanoidRenderState, M extends EntityModel<? super T>> extends RenderLayer<T, M> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.createId("hat"), "main");

    private final HatModel<T> model;

    public HatLayer(RenderLayerParent<T, M> parent, ModelPart model) {
        super(parent);
        this.model = new HatModel<>(model);
    }

    public static LayerDefinition createLayerDefinition() {
        var deformation = CubeDeformation.NONE;
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();

        var arrow = partDefinition.addOrReplaceChild("arrow", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-7.0F, -5.0F, -1.0F, 18.0F, 5.0F, 0.0F, deformation),
                PartPose.offset(-2.0F, -10.0F, 1.0F)
        );

        arrow.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-7.0F, -5.0F, -1.5F, 17.0F, 5.0F, 0.0F, deformation),
                PartPose.offsetAndRotation(0.0F, -1.0F, -3.5F, -1.5708F, 0.0F, 0.0F)
        );

        partDefinition.addOrReplaceChild("main", CubeListBuilder.create()
                        .texOffs(0, 36)
                        .addBox(-5.0F, -10.0F, -5.0F, 10.0F, 2.0F, 10.0F, deformation)
                        .texOffs(16, 22)
                        .addBox(-4.0F, -16.0F, -4.0F, 8.0F, 6.0F, 8.0F, deformation),
                PartPose.ZERO
        );

        var plant = partDefinition.addOrReplaceChild("plant", CubeListBuilder.create().texOffs(-8, 21).addBox(-7.0F, 0.0F, -1.0F, 8.0F, 0.0F, 8.0F, deformation), PartPose.offset(3.0F, -14.9F, -3.0F));
        plant.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-16, 5).addBox(-16.0F, 0.0F, -1.0F, 17.0F, 0.0F, 16.0F, deformation), PartPose.offsetAndRotation(8.0F, -11.0F, 11.0F, 0.0F, -1.5708F, -0.7854F));
        plant.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-16, 5).addBox(-16.0F, 0.0F, -1.0F, 17.0F, 0.0F, 16.0F, deformation), PartPose.offsetAndRotation(-14.0F, -11.0F, -5.0F, 0.0F, 1.5708F, 0.7854F));
        plant.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-16, 5).addBox(-16.0F, 0.0F, -1.0F, 17.0F, 0.0F, 16.0F, deformation), PartPose.offsetAndRotation(-19.0F, -1.0F, -5.0F, 0.0F, 1.5708F, 0.0F));
        plant.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-16, 5).addBox(-16.0F, 0.0F, -1.0F, 17.0F, 0.0F, 16.0F, deformation), PartPose.offsetAndRotation(13.0F, -1.0F, 11.0F, 0.0F, -1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("rim", CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-5.0F, -11.0F, -5.0F, 10.0F, 1.0F, 10.0F, deformation),
                PartPose.ZERO
        );

        partDefinition.addOrReplaceChild("ears", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-9.0F, -20.0F, 0.0F, 18.0F, 8.0F, 0.0F, deformation),
                PartPose.ZERO
        );

        return LayerDefinition.create(meshDefinition, 48, 48);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int lightCoords, T state, float yRot, float xRot) {
        var type = CommonServices.RENDER_HELPER.getRendererHatType(state).orElse(null);
        if (type == null) return;

        poseStack.pushPose();

        poseStack.translate(0F, 0F, 0F);

        model.setupAnim(state);
        model.setupVisibleParts(type.parts);
        var renderType = RenderTypes.entityTranslucent(type.texture);
        nodeCollector.submitModel(model, state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        poseStack.popPose();
    }
}
