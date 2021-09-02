package com.engineersbox.expandedfusion.core.registration.provider.shim.element;

import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockDeferredRegistryShim extends RegistryShim<Block> {

    private static final Logger LOGGER = LogManager.getLogger(BlockDeferredRegistryShim.class);

    @Inject
    public BlockDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends Block> BlockRegistryObject<T> registerNoItem(final String name, final Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    public <T extends Block> BlockRegistryObject<T> register(final String name,
                                                             final Supplier<T> block) {
        return register(name, block, this::defaultItem);
    }

    public <T extends Block> BlockRegistryObject<T> register(final String name,
                                                             final Supplier<T> block,
                                                             final Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        final BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    public BlockRegistryObject<FlowingFluidBlock> registerFluid(final String name, final Supplier<FlowingFluid> fluid) {
        return registerNoItem(name, () ->
                new FlowingFluidBlock(fluid, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
    }

    public <T extends Block> Supplier<BlockItem> defaultItem(final BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(Registration.getTabGroup(this.modID)));
    }

    @Nullable
    public ITextComponent checkForMissingLootTables(final PlayerEntity player) {
        // Checks for missing block loot tables, but only in dev
        if (!(player.world instanceof ServerWorld)) return null;

        final LootTableManager lootTableManager = ((ServerWorld) player.world).getServer().getLootTableManager();
        final Collection<String> missing = new ArrayList<>();

        synchronized (missing) {
            ForgeRegistries.BLOCKS.getValues().parallelStream().forEach((block) -> {
                final ResourceLocation lootTable = block.getLootTable();
                // The AirBlock check filters out removed blocks
                if (lootTable.getNamespace().equals(this.modID)
                        && !(block instanceof AirBlock)
                        && !lootTableManager.getLootTableKeys().contains(lootTable)) {
                    LOGGER.error("Missing block loot table '{}' for {}", lootTable, block.getRegistryName());
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
