package com.xorume.ntmadditions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

@Mod(NtmAdditions.MODID)
public final class NtmAdditions {
    public static final String MODID = "ntmadditions";
    /** Curios slot that holds gas masks. */
    public static final String MASK_SLOT = "mask";
    /** HBM items whose inventory tick should also run from a Curios slot. */
    private static final String[] TICKING = {"geiger_counter", "dosimeter", "pollution_detector"};

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredHolder<Item, ItemLungTester> LUNG_TESTER =
            ITEMS.register("lung_tester", () -> new ItemLungTester(new Item.Properties().stacksTo(1)));

    public NtmAdditions(IEventBus modBus, ModContainer container) {
        ITEMS.register(modBus);
        modBus.addListener(NtmAdditions::setup);
        modBus.addListener(NtmAdditions::creativeTab);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        if (FMLEnvironment.dist.isClient()) ClientSetup.init(container);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (String id : TICKING) {
                Item item = hbm(id);
                if (item != Items.AIR) CuriosApi.registerCurio(item, TickingCurio.INSTANCE);
            }
        });
    }

    private static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.withDefaultNamespace("tools_and_utilities"))) event.accept(LUNG_TESTER.get());
    }

    public static Item hbm(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("hbm", id));
    }

    /** Runs the item's normal inventory tick (geiger clicks, pollution readout...) while it sits in a Curios slot. */
    private static final class TickingCurio implements ICurioItem {
        static final TickingCurio INSTANCE = new TickingCurio();

        @Override
        public void curioTick(SlotContext ctx, ItemStack stack) {
            LivingEntity e = ctx.entity();
            if (e != null && !e.level().isClientSide) stack.inventoryTick(e.level(), e, -1, false);
        }
    }

    /** True if the entity has the given HBM item equipped in any Curios slot. */
    public static boolean hasCurio(LivingEntity entity, String hbmId) {
        Item item = hbm(hbmId);
        if (item == Items.AIR) return false;
        return CuriosApi.getCuriosInventory(entity).map(h -> h.isEquipped(item)).orElse(false);
    }

    /** The mask worn in the Curios "mask" slot, or EMPTY. */
    public static ItemStack curioMask(LivingEntity entity) {
        if (entity == null) return ItemStack.EMPTY;
        return CuriosApi.getCuriosInventory(entity)
                .flatMap(h -> h.findFirstCurio(s -> !s.isEmpty(), MASK_SLOT))
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }
}
