package galena.nirvana.world.item;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.NirvanaConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.sounds.SoundEvents;
/** Armor behavior is provided by 26.1 components; the head model is an item model. */
public class DeerStalkerItem extends Item implements ArmorLike {
 public DeerStalkerItem(Properties properties) {
  super(properties.enchantable(15).repairable(NirvanaItems.HEMP_CLOTH.get())
   .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER).build())
   .attributes(ItemAttributeModifiers.builder().add(Attributes.ARMOR,new AttributeModifier(NirvanaConstants.createId("armor"),1,AttributeModifier.Operation.ADD_VALUE),EquipmentSlotGroup.HEAD).build()));
 }
}
