package com.xorume.ntmadditions.mixin;

import com.hbm.util.ArmorRegistry;
import com.hbm.util.ArmorRegistry.HazardClass;
import com.xorume.ntmadditions.NtmAdditions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A gas mask in the Curios "mask" slot counts as head protection, combined with whatever the helmet gives. */
@Mixin(value = ArmorRegistry.class, remap = false)
public abstract class ArmorRegistryMixin {

    private static List<HazardClass> ntmadditions$combined(LivingEntity e) {
        ItemStack mask = NtmAdditions.curioMask(e);
        if (mask.isEmpty()) return null;
        List<HazardClass> list = new ArrayList<>(ArmorRegistry.getProtectionFromItem(mask));
        ItemStack head = e.getItemBySlot(EquipmentSlot.HEAD);
        if (!head.isEmpty()) list.addAll(ArmorRegistry.getProtectionFromItem(head));
        return list;
    }

    @Inject(method = "hasProtection", at = @At("RETURN"), cancellable = true, remap = false)
    private static void ntmadditions$has(LivingEntity e, EquipmentSlot slot, HazardClass c, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || slot != EquipmentSlot.HEAD) return;
        List<HazardClass> l = ntmadditions$combined(e);
        if (l != null && l.contains(c)) cir.setReturnValue(true);
    }

    @Inject(method = "hasAllProtection", at = @At("RETURN"), cancellable = true, remap = false)
    private static void ntmadditions$all(LivingEntity e, EquipmentSlot slot, HazardClass[] c, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || slot != EquipmentSlot.HEAD) return;
        List<HazardClass> l = ntmadditions$combined(e);
        if (l != null && l.containsAll(Arrays.asList(c))) cir.setReturnValue(true);
    }

    @Inject(method = "hasAnyProtection", at = @At("RETURN"), cancellable = true, remap = false)
    private static void ntmadditions$any(LivingEntity e, EquipmentSlot slot, HazardClass[] c, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || slot != EquipmentSlot.HEAD) return;
        List<HazardClass> l = ntmadditions$combined(e);
        if (l == null) return;
        for (HazardClass h : c) {
            if (l.contains(h)) { cir.setReturnValue(true); return; }
        }
    }
}
