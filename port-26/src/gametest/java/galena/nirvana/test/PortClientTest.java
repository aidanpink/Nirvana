package galena.nirvana.test;
import galena.nirvana.index.*;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.core.BlockPos;
public class PortClientTest implements FabricClientGameTest {
    private static void check(boolean value,String message) { if(!value) throw new AssertionError(message); }
    @Override public void runTest(ClientGameTestContext context) {
        try(TestSingleplayerContext world=context.worldBuilder().create()) {
            world.getClientLevel().waitForChunksRender();
            world.getServer().runOnServer(server -> {
                var level=server.overworld();
                var player=server.getPlayerList().getPlayers().getFirst();
                var water=new ItemStack(Items.POTION);
                water.set(DataComponents.POTION_CONTENTS,new PotionContents(Potions.WATER));
                var bong=server.potionBrewing().mix(NirvanaItems.WEED.asStack(),water);
                check(bong.is(NirvanaItems.BONG.get()), "Water + weed must brew a bong");
                for(String id:new String[]{"herbal_salve","suspicious_pipe"}) {
                    var recipe=server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,Identifier.fromNamespaceAndPath("nirvana",id)));
                    check(recipe.isPresent(),"Missing custom recipe: "+id);
                }
                int slot=0;
                for(var item:BuiltInRegistries.ITEM) {
                    if(BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("nirvana")) {
                        var stack=new ItemStack(item);
                        check(!stack.isEmpty(),"Item cannot create stack: "+item);
                        if(slot<36)player.getInventory().setItem(slot++,stack);
                    }
                }
                player.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD,NirvanaItems.DEERSTALKER.asStack());
                var origin=player.blockPosition().offset(-4,0,4);
                int i=0;
                for(var block:BuiltInRegistries.BLOCK) {
                    if(BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("nirvana")) {
                        var pos=origin.offset(i%10,0,i/10);
                        level.setBlock(pos.below(),net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(),3);
                        level.setBlock(pos,block.defaultBlockState(),3);i++;
                    }
                }
                var reefer=NirvanaEntities.REEFER.create(level);
                check(reefer!=null,"Reefer must create");
                var skullDrops=new java.util.ArrayList<ItemStack>();
                reefer.dropFromLootTable(level,level.damageSources().generic(),false,net.minecraft.world.level.storage.loot.BuiltInLootTables.CHARGED_CREEPER,skullDrops::add);
                check(skullDrops.stream().anyMatch(stack->stack.is(NirvanaItems.REEFER_HEAD.get())),"Charged creeper must drop a Reefer head");
                reefer.setPos(player.getX()+2,player.getY(),player.getZ()+2);reefer.setNoAi(true);level.addFreshEntity(reefer);
                var cart=NirvanaEntities.THC_MINECART.create(level);
                cart.setPos(player.getX()-2,player.getY(),player.getZ()+2);level.addFreshEntity(cart);
                player.addEffect(new MobEffectInstance(NirvanaEffects.PEACE,400,10));
            });
            context.waitTicks(30);
            context.runOnClient(client -> check(galena.nirvana.client.PeaceShader.load(client)!=null,"Peace shader must load"));
            context.takeScreenshot("nirvana-26-peace-and-content");
            world.getServer().runCommand("effect clear @a");
            world.getServer().runOnServer(server -> {
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();
                int slot=0;
                for(var item:BuiltInRegistries.ITEM)if(BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("nirvana") && !(item instanceof BlockItem))player.getInventory().setItem(slot++,new ItemStack(item));
                var tnt=new galena.nirvana.world.entity.PrimedThc(level,player.getX()+3,player.getY(),player.getZ(),player);
                tnt.setFuse(2);level.addFreshEntity(tnt);
            });
            context.waitTicks(10);
            world.getServer().runOnServer(server -> {
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();
                check(!level.getEntitiesOfClass(net.minecraft.world.entity.AreaEffectCloud.class,player.getBoundingBox().inflate(10)).isEmpty(),"THC must explode into an effect cloud");
            });
            context.runOnClient(client -> client.setScreen(new net.minecraft.client.gui.screens.inventory.InventoryScreen(client.player)));
            context.waitTicks(5);
            context.takeScreenshot("nirvana-26-items");
            context.runOnClient(client -> client.setScreen(null));
        }
    }
}
