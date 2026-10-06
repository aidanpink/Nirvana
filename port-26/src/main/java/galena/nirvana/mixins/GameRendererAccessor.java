package galena.nirvana.mixins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
 @Accessor Minecraft getMinecraft();
 @Accessor Identifier getPostEffectId();
 @Accessor CrossFrameResourcePool getResourcePool();
}
