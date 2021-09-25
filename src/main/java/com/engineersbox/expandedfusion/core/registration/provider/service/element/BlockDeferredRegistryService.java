package com.engineersbox.expandedfusion.core.registration.provider.service.element;

import com.engineersbox.expandedfusion.core.reflection.FieldAccessor;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BlockDeferredRegistryService extends RegistryService<Block> {

    private static final Logger LOGGER = LogManager.getLogger(BlockDeferredRegistryService.class);
    final Registration registration;

    @Inject
    public BlockDeferredRegistryService(@Named("modId") final String modID,
                                        final Registration registration) {
        this.modID = modID;
        this.registration = registration;
    }

    public AbstractBlock.Properties assemblePropertiesFromAnnotation(final BlockProperties properties) {
        AbstractBlock.Properties blockProps = createInitialBlockProps(properties);
        if (properties.doesNotBlockMovement()) {
            blockProps = blockProps.doesNotBlockMovement();
        }
        if (properties.notSolid()) {
            blockProps = blockProps.notSolid();
        }
        blockProps = blockProps.harvestLevel(properties.harvestLevel());
        final Optional<String> harvestTool = getFieldAndOrLog(properties.harvestTool(), "harvestTool");
        if (harvestTool.isPresent()) {
            blockProps = blockProps.harvestTool(ToolType.get(harvestTool.get()));
        }
        blockProps = blockProps.slipperiness(properties.slipperiness())
                .speedFactor(properties.speedFactor())
                .jumpFactor(properties.jumpFactor())
                .sound(FieldAccessor.getStaticFieldValue(
                        properties.soundType(),
                        SoundType.class
                )).setLightLevel((final BlockState bs) -> properties.lightLevel());
        final Optional<Float> hardness = getFieldAndOrLog(ArrayUtils.toObject(properties.hardness()), "hardness");
        final Optional<Float> resistance = getFieldAndOrLog(ArrayUtils.toObject(properties.resistance()), "resistance");
        if (hardness.isPresent() && resistance.isPresent()) {
            blockProps = blockProps.hardnessAndResistance(hardness.get(), resistance.get());
        } else if (hardness.isPresent()) {
            blockProps = blockProps.hardnessAndResistance(hardness.get());
        } else if (resistance.isPresent()) {
            blockProps = blockProps.hardnessAndResistance(resistance.get());
        }
        if (properties.tickRandomly()) {
            blockProps = blockProps.tickRandomly();
        }
        if (properties.variableOpacity()) {
            blockProps = blockProps.variableOpacity();
        }
        final Optional<String> lootFrom = getFieldAndOrLog(properties.lootFrom(), "lootFrom");
        if (lootFrom.isPresent()) {
            blockProps = blockProps.lootFrom(() -> RegistryObjectContext.getBlockRegistryObject(lootFrom.get()).asBlock());
        }
        if (properties.isAir()) {
            blockProps = blockProps.setAir();
        }
        blockProps = setPredicatedProperties(blockProps, properties);
        if (properties.requiresTool()) {
            blockProps = blockProps.setRequiresTool();
        }
        return blockProps;
    }

    private AbstractBlock.Properties setPredicatedProperties(AbstractBlock.Properties blockProps,
                                                             final BlockProperties properties) {
        final Optional<BlockProperties.AllowsSpawn> allowsSpawn = getFieldAndOrLog(properties.allowsSpawn(), "allowsSpawn");
        if (allowsSpawn.isPresent()) {
            blockProps = blockProps.setAllowsSpawn((final BlockState state, final IBlockReader reader, final BlockPos pos, final EntityType<?> entityType) ->
                    state.isSolidSide(reader, pos, allowsSpawn.get().direction())
                            && state.getLightValue(reader, pos) <= allowsSpawn.get().lightLevelUpperBound()
                            && state.getLightValue(reader, pos) >= allowsSpawn.get().lightLevelLowerBound()
            );
        }
        final Optional<Boolean> isOpaque = getFieldAndOrLog(ArrayUtils.toObject(properties.isOpaque()), "isOpaque");
        if (isOpaque.isPresent()) {
            blockProps = blockProps.setOpaque((final BlockState state, final IBlockReader reader, final BlockPos pos) -> isOpaque.get());
        }
        final Optional<Boolean> suffocates = getFieldAndOrLog(ArrayUtils.toObject(properties.suffocates()), "suffocates");
        if (suffocates.isPresent()) {
            blockProps = blockProps.setSuffocates((final BlockState state, final IBlockReader reader, final BlockPos pos) -> suffocates.get());
        }
        final Optional<Boolean> blocksVision = getFieldAndOrLog(ArrayUtils.toObject(properties.blocksVision()), "blocksVision");
        if (blocksVision.isPresent()) {
            blockProps = blockProps.setBlocksVision((final BlockState state, final IBlockReader reader, final BlockPos pos) -> blocksVision.get());
        }
        final Optional<Boolean> needsPostProcessing = getFieldAndOrLog(ArrayUtils.toObject(properties.needsPostProcessing()), "needsPostProcessing");
        if (needsPostProcessing.isPresent()) {
            blockProps = blockProps.setNeedsPostProcessing((final BlockState state, final IBlockReader reader, final BlockPos pos) -> needsPostProcessing.get());
        }
        final Optional<Boolean> emissiveRendering = getFieldAndOrLog(ArrayUtils.toObject(properties.emissiveRendering()), "emissiveRendering");
        if (emissiveRendering.isPresent()) {
            blockProps = blockProps.setEmmisiveRendering((final BlockState state, final IBlockReader reader, final BlockPos pos) -> emissiveRendering.get());
        }
        return blockProps;
    }

    private AbstractBlock.Properties createInitialBlockProps(final BlockProperties properties) {
        AbstractBlock.Properties blockProps = AbstractBlock.Properties.create(FieldAccessor.getStaticFieldValue(
                properties.material(),
                Material.class
        ));
        final Optional<String> dyeColour = getFieldAndOrLog(properties.dyeColour(), "dyeColour");
        final Optional<String> materialColour = getFieldAndOrLog(properties.materialColour(), "materialColour");
        if (materialColour.isPresent()) {
            blockProps = AbstractBlock.Properties.create(
                    FieldAccessor.getStaticFieldValue(
                            properties.material(),
                            Material.class
                    ),
                    FieldAccessor.getStaticFieldValue(
                            materialColour.get(),
                            MaterialColor.class
                    )
            );
        } else if (dyeColour.isPresent()) {
            blockProps = AbstractBlock.Properties.create(
                    FieldAccessor.getStaticFieldValue(
                            properties.material(),
                            Material.class
                    ),
                    DyeColor.valueOf(dyeColour.get().toUpperCase())
            );
        }
        return blockProps;
    }

    private <T> Optional<T> getFieldAndOrLog(final T[] values,
                                             final String fieldName) {
        if (values.length < 1) {
            return Optional.empty();
        }
        if (values.length > 1) {
            LOGGER.warn("More than one value present for {}, defaulting to value at index 0", fieldName);
        }
        return Optional.of(values[0]);
    }

    public <T extends Block> BlockRegistryObject<T> registerNoItem(final String name, final Supplier<T> block) {
        return new BlockRegistryObject<>(this.registration.getBlockRegister().register(name, block));
    }

    public <T extends Block> BlockRegistryObject<T> register(final String name,
                                                             final Supplier<T> block,
                                                             final String tabGroupName) {
        return register(name, block, this::defaultItem, tabGroupName);
    }

    public <T extends Block> BlockRegistryObject<T> register(final String name,
                                                             final Supplier<T> block,
                                                             final BiFunction<BlockRegistryObject<T>, String, Supplier<? extends BlockItem>> item,
                                                             final String tabGroupName) {
        final BlockRegistryObject<T> ret = registerNoItem(name, block);
        this.registration.getItemRegister().register(name, item.apply(ret, tabGroupName));
        return ret;
    }

    public BlockRegistryObject<FlowingFluidBlock> registerFluid(final String name,
                                                                final Supplier<FlowingFluid> fluid,
                                                                final AbstractBlock.Properties properties) {
        return registerNoItem(name, () ->
                new FlowingFluidBlock(fluid, properties));
    }

    public BlockRegistryObject<FlowingFluidBlock> registerFluid(final String name,
                                                                final Supplier<FlowingFluid> fluid) {
        return registerFluid(name, fluid, AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops());
    }

    public <T extends Block> Supplier<BlockItem> defaultItem(final BlockRegistryObject<T> block,
                                                             final String tabGroupName) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(Registration.getTabGroup(StringUtils.isEmpty(tabGroupName.trim()) ? this.modID : tabGroupName)));
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
