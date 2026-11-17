package com.xorume.ntmadditions.mixin;

import com.xorume.ntmadditions.NtmAdditions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Shows HBM's radiation HUD when the geiger counter is in a Curios slot. */
@Pseudo
@Mixin(targets = "com.hbm.client.RadCounterOverlay", remap = false)
public abstract class RadCounterOverlayMixin {
    @Inject(method = "carriesCounter(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("RETURN"), cancellable = true, remap = false)
    private static void ntmadditions$curioCounter(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && NtmAdditions.hasCurio(player, "geiger_counter")) cir.setReturnValue(true);
    }
}
