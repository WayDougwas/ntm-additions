package com.xorume.ntmadditions.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.xorume.ntmadditions.ClientConfig;
import com.xorume.ntmadditions.NtmAdditions;
import dev.golem.core.gui.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * - Config option to hide HBM's visor overlay.
 * - Draws the overlay of a mask worn in the Curios mask slot.
 * - Fixes the overlay writing depth (which hid Jade's tooltip): disables depth test/mask like vanilla's pumpkin overlay.
 */
@Pseudo
@Mixin(targets = "com.hbm.client.ArmorSetBClient", remap = false)
public abstract class HelmetOverlayMixin {
    @Shadow(remap = false)
    private static ResourceLocation overlay(ItemStack stack) { throw new AssertionError(); }

    @Inject(method = "renderHelmetOverlay(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void ntmadditions$start(GuiGraphics g, CallbackInfo ci) {
        if (ClientConfig.HIDE_HELMET_OVERLAY.get()) {
            ci.cancel();
            return;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
    }

    @Inject(method = "renderHelmetOverlay(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("RETURN"), remap = false)
    private static void ntmadditions$end(GuiGraphics g, CallbackInfo ci) {
        try {
            Minecraft mc = Minecraft.getInstance();
            if (ClientConfig.CURIO_MASK_OVERLAY.get() && mc.player != null && !mc.options.hideGui
                    && mc.options.getCameraType().isFirstPerson()
                    && overlay(mc.player.getItemBySlot(EquipmentSlot.HEAD)) == null) {
                ItemStack mask = NtmAdditions.curioMask(mc.player);
                ResourceLocation tex = mask.isEmpty() ? null : overlay(mask);
                if (tex != null) GuiDraw.blitUvFullscreen(g, tex, 0F, 0F, 1F, 1F);
            }
        } finally {
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }
}
