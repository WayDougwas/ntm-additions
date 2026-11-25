package com.xorume.ntmadditions;

import com.hbm.capability.HbmLivingProps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Locale;

/**
 * Port of HBM Extended's Lung Diagnostic (ItemLungDiagnostic / ContaminationUtil.printLungDiagnosticData):
 * asbestos lung health, coal (black lung) health, total, and the MKU test.
 * Right-click: yourself. Right-click an entity: that entity.
 */
public class ItemLungTester extends Item {
    private static final ResourceLocation BOOP = ResourceLocation.fromNamespaceAndPath("hbm", "item.tech_boop");

    public ItemLungTester(Properties props) { super(props); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) report(player, player);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide) report(player, target);
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    private static void report(Player viewer, LivingEntity target) {
        // same math as HBM Extended: health = 100 - damage%, total = asbestos * coal / 100
        float asbestos = 100F - ((int) (10000F * HbmLivingProps.getAsbestos(target) / HbmLivingProps.maxAsbestos)) / 100F;
        float coal = 100F - ((int) (10000F * HbmLivingProps.getBlackLung(target) / HbmLivingProps.maxBlacklung)) / 100F;
        asbestos = clamp(asbestos);
        coal = clamp(coal);
        float total = asbestos * coal / 100F;
        int contagion = HbmLivingProps.getContagion(target);

        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getOptional(BOOP).orElse(SoundEvents.UI_BUTTON_CLICK.value());
        viewer.level().playSound(null, viewer.getX(), viewer.getY(), viewer.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);

        MutableComponent title = Component.literal("===== L ")
                .append(Component.translatable("ntmadditions.lung_scanner.title"))
                .append(" L =====");
        if (target != viewer) title.append(" ").append(target.getDisplayName());
        viewer.sendSystemMessage(title.withStyle(ChatFormatting.WHITE));
        viewer.sendSystemMessage(line("ntmadditions.lung_scanner.asbestos", ChatFormatting.WHITE, asbestos));
        viewer.sendSystemMessage(line("ntmadditions.lung_scanner.coal", ChatFormatting.DARK_GRAY, coal));
        viewer.sendSystemMessage(line("ntmadditions.lung_scanner.total", ChatFormatting.GRAY, total));
        viewer.sendSystemMessage(Component.translatable("ntmadditions.lung_scanner.mku").withStyle(ChatFormatting.GRAY)
                .append(contagion > 0
                        ? Component.translatable("ntmadditions.lung_scanner.pos").withStyle(ChatFormatting.DARK_RED)
                        : Component.translatable("ntmadditions.lung_scanner.neg").withStyle(ChatFormatting.DARK_GREEN)));
        if (contagion > 0) {
            viewer.sendSystemMessage(Component.translatable("ntmadditions.lung_scanner.mku_duration").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(" " + duration(contagion)).withStyle(ChatFormatting.RED)));
        }
    }

    private static float clamp(float v) { return Math.max(0F, Math.min(100F, v)); }

    private static MutableComponent line(String key, ChatFormatting labelColor, float health) {
        return Component.translatable(key).withStyle(labelColor)
                .append(Component.literal(String.format(Locale.ROOT, " %6.2f %%", health)).withStyle(color(health)));
    }

    /** Same thresholds idea as HBM Extended's getTextColorLung: green healthy -> dark red dying. */
    private static ChatFormatting color(float health) {
        if (health >= 100F) return ChatFormatting.GREEN;
        if (health >= 90F) return ChatFormatting.DARK_GREEN;
        if (health >= 75F) return ChatFormatting.YELLOW;
        if (health >= 50F) return ChatFormatting.GOLD;
        if (health >= 25F) return ChatFormatting.RED;
        return ChatFormatting.DARK_RED;
    }

    private static String duration(int ticks) {
        long s = ticks / 20L;
        long h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
        return h > 0 ? String.format(Locale.ROOT, "%dh %02dm %02ds", h, m, sec) : String.format(Locale.ROOT, "%dm %02ds", m, sec);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ntmadditions.lung_tester.desc").withStyle(ChatFormatting.GRAY));
    }
}
