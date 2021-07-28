package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.elements.block.fusionControlComputer.FusionControlComputer;
import com.engineersbox.expandedfusion.register.registry.BlockRegistryObject;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    // Mineral and metal solid blocks
//    public static final BlockRegistryObject<Block> NOBIUM_BLOCK = register("nobium_block", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> TITANIUM_BLOCK = register("titanium_block", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> TIN_BLOCK = register("tin_block", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> COPPER_BLOCK = register("copper_block", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> BERYLLIUM_BLOCK = register("beryllium_block", () -> new Block(Block.Properties.create(Material.IRON)));

    // Composite Machine Structure ModBlocks
    public static final BlockRegistryObject<Block> NOBIUM_TITANIUM_COIL = register("nobium_titanium_coil", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> NOBIUM_TIN_COIL = Registration.BLOCKS.register("nobium_tin_coil", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> COPPER_BERYLLIUM_BONDED_SHIELDING = Registration.BLOCKS.register("copper_beryllium_bonded_shielding", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> DIVERTOR_CASSETTE_ASSEMBLY = Registration.BLOCKS.register("divertor_cassette_assembly", () -> new Block(Block.Properties.create(Material.IRON)));
//    public static final BlockRegistryObject<Block> CRYOSTAT_PRESSURE_CHAMBER_WALL = Registration.BLOCKS.register("cryostat_pressure_chamber_wall", () -> new Block(Block.Properties.create(Material.IRON)));

    // Machines
    public static final BlockRegistryObject<Block> FUSION_CONTROL_COMPUTER = register("fusion_control_computer", FusionControlComputer::new);

    private ModBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(final FMLClientSetupEvent event) {
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(final String name, final Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(final String name, final Supplier<T> block) {
        return register(name, block, ModBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(final String name, final Supplier<T> block, final Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        final BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static BlockRegistryObject<FlowingFluidBlock> registerFluid(final String name, final Supplier<FlowingFluid> fluid) {
        return registerNoItem(name, () ->
                new FlowingFluidBlock(fluid, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(final BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(Registration.CREATIVE_TAB_ITEM_GROUP));
    }

    @Nullable
    public static ITextComponent checkForMissingLootTables(final PlayerEntity player) {
        // Checks for missing block loot tables, but only in dev
        if (!(player.world instanceof ServerWorld)) return null;

        final LootTableManager lootTableManager = ((ServerWorld) player.world).getServer().getLootTableManager();
        final Collection<String> missing = new ArrayList<>();

        synchronized (missing) {
            ForgeRegistries.BLOCKS.getValues().parallelStream().forEach((block) -> {
                final ResourceLocation lootTable = block.getLootTable();
                // The AirBlock check filters out removed blocks
                if (lootTable.getNamespace().equals(ExpandedFusion.MOD_ID)
                        && !(block instanceof AirBlock)
                        && !lootTableManager.getLootTableKeys().contains(lootTable)) {
                    ExpandedFusion.LOGGER.error("Missing block loot table '{}' for {}", lootTable, block.getRegistryName());
                    missing.add(lootTable.toString());
                }
            });
        }

        if (!missing.isEmpty()) {
            String list = String.join(", ", missing);
            return new StringTextComponent("The following block loot tables are missing: " + list).mergeStyle(TextFormatting.RED);
        }

        return null;
    }
}
