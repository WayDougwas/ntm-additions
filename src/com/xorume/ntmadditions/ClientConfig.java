package com.xorume.ntmadditions;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue HIDE_HELMET_OVERLAY;
    public static final ModConfigSpec.BooleanValue CURIO_MASK_OVERLAY;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        HIDE_HELMET_OVERLAY = b
                .comment("Hide HBM's fullscreen visor / gas mask overlay completely (helmet slot and Curios mask slot).")
                .translation("ntmadditions.configuration.hideHelmetOverlay")
                .define("hideHelmetOverlay", false);
        CURIO_MASK_OVERLAY = b
                .comment("Show the visor overlay of a gas mask worn in the Curios mask slot (only when the helmet has no overlay of its own).")
                .translation("ntmadditions.configuration.curioMaskOverlay")
                .define("curioMaskOverlay", true);
        SPEC = b.build();
    }

    private ClientConfig() {}
}
