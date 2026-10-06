package galena.nirvana.client;
import galena.nirvana.NirvanaConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.resources.Identifier;
public class PeaceShader {
 public static final Identifier ID = NirvanaConstants.createId("peace");
 public static PostChain load(Minecraft minecraft){return minecraft.getShaderManager().getPostChain(ID,LevelTargetBundle.MAIN_TARGETS);}
}
