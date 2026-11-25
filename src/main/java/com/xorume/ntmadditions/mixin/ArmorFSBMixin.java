package com.xorume.ntmadditions.mixin;

import com.xorume.ntmadditions.NtmAdditions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Full-set armor with a built-in geiger stays quiet when a counter is already worn as a curio (avoids double clicks). */
@Pseudo
@Mixin(targets = "com.hbm.items.armor.ArmorFSB", remap = false)
public abstract class ArmorFSBMixin {
    @Inject(method = "carriesCounter(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("RETURN"), cancellable = true, remap = false)
    private static void ntmadditions$curioCounter(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()
                && (NtmAdditions.hasCurio(player, "geiger_counter") || NtmAdditions.hasCurio(player, "dosimeter"))) {
            cir.setReturnValue(true);
        }
    }
}
