package welbervs.mc.ambercraft.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import welbervs.mc.ambercraft.api.network.NetworkManager;

/**
 * {@code Level#markAndNotifyBlock} calls {@code onBlockStateChange} for every successful block change (players, pistons,
 * explosions, commands, other mods...), regardless of the update flags. It's the most reliable place to detect when a
 * block that may expose network components is placed or removed.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin
{
    @Inject(method = "onBlockStateChange", at = @At("HEAD"))
    private void ambercraft$onBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState, CallbackInfo ci)
    {
        NetworkManager.onBlockStateChanged((ServerLevel) (Object) this, pos, oldState, newState);
    }
}
