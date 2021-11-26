package me.aquavit.liquidsense.modules.combat;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "FastBow", description = "Turns your bow into a machine gun.", category = ModuleCategory.COMBAT)
public class FastBow extends Module
{
    public final IntegerValue packetsValue = new IntegerValue("Packets", 20, 3, 20);

    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (!mc.thePlayer.isUsingItem()) {
            return;
        }
        if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
            float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : mc.thePlayer.rotationYaw;
            float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : mc.thePlayer.rotationPitch;
            for (int i = 0; i < packetsValue.get(); ++i) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
            }
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.thePlayer.itemInUseCount = mc.thePlayer.inventory.getCurrentItem().getMaxItemUseDuration() - 1;
        }
    }
}
