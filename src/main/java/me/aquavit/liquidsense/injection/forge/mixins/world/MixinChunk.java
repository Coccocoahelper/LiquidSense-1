/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.aquavit.liquidsense.injection.forge.mixins.world;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.ProphuntESP;
import me.aquavit.liquidsense.utils.render.MiniMapRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunk {

    @Shadow
    @Final
    public int xPosition;

    @Shadow
    @Final
    public int zPosition;

    @Inject(method = "onChunkUnload", at = @At("HEAD"))
    private void injectFillChunk(CallbackInfo ci) {
        MiniMapRegister.INSTANCE.unloadChunk(this.xPosition, this.zPosition);
    }

    @Inject(method = "fillChunk", at = @At("RETURN"))
    private void injectFillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_, CallbackInfo ci) {
        MiniMapRegister.INSTANCE.updateChunk((Chunk) ((Object) this));
    }

    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void setProphuntBlock(BlockPos pos, IBlockState state, final CallbackInfoReturnable callbackInfo) {
        final ProphuntESP prophuntESP = (ProphuntESP) LiquidSense.moduleManager.getModule(ProphuntESP.class);

        if(prophuntESP.getState()) {
            synchronized(prophuntESP.blocks) {
                prophuntESP.blocks.put(pos, System.currentTimeMillis());
            }
        }
    }
}
