package com.xorume.ntmadditions;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

/** Client-only: config button in the Mods menu, Geiger click muting and its toggle key. */
final class ClientSetup {
    private ClientSetup() {}

    private static final KeyMapping TOGGLE_GEIGER = new KeyMapping(
            "key.ntmadditions.toggle_geiger", InputConstants.UNKNOWN.getValue(), "key.categories.ntmadditions");

    static void init(IEventBus modBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modBus.addListener(RegisterKeyMappingsEvent.class, e -> e.register(TOGGLE_GEIGER));
        NeoForge.EVENT_BUS.addListener(PlaySoundEvent.class, ClientSetup::onSound);
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, e -> onTick());
        NeoForge.EVENT_BUS.addListener(ScreenEvent.Init.Post.class, ClientSetup::onScreenInit);
    }

    /** Adds "Geiger counter: ON/OFF" and "Geiger block: ON/OFF" to the bottom of Music & Sounds. */
    private static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof SoundOptionsScreen)) return;
        for (GuiEventListener child : event.getListenersList()) {
            if (child instanceof OptionsList list) {
                list.addSmall(toggle("ntmadditions.sound.geiger", ClientConfig.MUTE_GEIGER),
                        toggle("ntmadditions.sound.geigerBlock", ClientConfig.MUTE_GEIGER_BLOCK));
                return;
            }
        }
    }

    /** ON = sound plays, so the button shows the inverse of the mute setting. */
    private static OptionInstance<Boolean> toggle(String key, ModConfigSpec.BooleanValue mute) {
        return OptionInstance.createBoolean(key,
                OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
                !mute.get(), on -> { mute.set(!on); mute.save(); });
    }

    /** HBM's Geiger clicks are hbm:item.geiger1..6, played by the server: drop them here if muted. */
    private static void onSound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();
        if (sound == null) return;
        ResourceLocation id = sound.getLocation();
        if (!"hbm".equals(id.getNamespace()) || !id.getPath().startsWith("item.geiger")) return;
        boolean block = sound.getSource() == SoundSource.BLOCKS; // the Geiger Counter block; items/armor use PLAYERS
        if (block ? ClientConfig.MUTE_GEIGER_BLOCK.get() : ClientConfig.MUTE_GEIGER.get()) event.setSound(null);
    }

    private static void onTick() {
        while (TOGGLE_GEIGER.consumeClick()) {
            boolean muted = !ClientConfig.MUTE_GEIGER.get();
            ClientConfig.MUTE_GEIGER.set(muted);
            ClientConfig.MUTE_GEIGER.save();
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) mc.player.displayClientMessage(
                    Component.translatable(muted ? "ntmadditions.geiger.muted" : "ntmadditions.geiger.unmuted"), true);
        }
    }
}
