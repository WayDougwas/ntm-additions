package com.xorume.ntmadditions.mixin;

import com.hbm.api.item.IGasMask;
import com.hbm.handler.ArmorModHandler;
import com.hbm.handler.ArmorUtil;
import com.xorume.ntmadditions.NtmAdditions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Wears down the filter of the Curios mask when the helmet itself is not doing the filtering. */
@Mixin(value = ArmorUtil.class, remap = false)
public abstract class ArmorUtilMixin {
    @Inject(method = "damageGasMaskFilter", at = @At("HEAD"), cancellable = true, remap = false)
    private static void ntmadditions$damageCurioFilter(LivingEntity e, int damage, CallbackInfo ci) {
        ItemStack mask = NtmAdditions.curioMask(e);
        if (mask.isEmpty() || !(mask.getItem() instanceof IGasMask curioMask) || curioMask.getFilter(mask).isEmpty()) return;

        ItemStack helmet = e.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof IGasMask hm && !hm.getFilter(helmet).isEmpty()) return; // helmet filters
        if (ArmorModHandler.hasMods(helmet)) {
            ItemStack mod = ArmorModHandler.pryMod(helmet, ArmorModHandler.helmet_only);
            if (mod.getItem() instanceof IGasMask mm && !mm.getFilter(mod).isEmpty()) return; // attachment filters
        }
        curioMask.damageFilter(mask, damage);
        ci.cancel();
    }
}
