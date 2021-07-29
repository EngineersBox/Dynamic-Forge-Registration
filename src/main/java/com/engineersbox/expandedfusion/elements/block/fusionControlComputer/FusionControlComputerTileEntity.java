package com.engineersbox.expandedfusion.elements.block.fusionControlComputer;

import com.engineersbox.expandedfusion.core.common.MachineTier;
import com.engineersbox.expandedfusion.core.common.machine.tileentity.AbstractMachineTileEntity;
import com.engineersbox.expandedfusion.core.common.machine.tileentity.EnergyProperties;
import com.engineersbox.expandedfusion.core.util.TextUtil;
import com.engineersbox.expandedfusion.register.ModTileEntities;
import com.engineersbox.expandedfusion.register.registry.annotation.TileEntityProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@TileEntityProvider(name = "fusion_control_computer")
public class FusionControlComputerTileEntity extends AbstractMachineTileEntity<AbstractCookingRecipe> {
    // Inventory constants
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = {1};
    private static final int[] SLOTS_ALL = {0, 1};
    private static final int INVENTORY_SIZE = SLOTS_ALL.length;

    public FusionControlComputerTileEntity() {
        super(
            ModTileEntities.FUSION_CONTROL_COMPUTER,
            INVENTORY_SIZE,
            MachineTier.REINFORCED,
            new EnergyProperties(
                50_000,
                500,
                30
            )
        );
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return this.energyProps.usedPerTick;
    }

    @Override
    protected int[] getOutputSlots() {
        return SLOTS_OUTPUT;
    }

    @Override
    @Nullable
    protected AbstractCookingRecipe getRecipe() {
        if (world == null) return null;

        final RecipeManager recipeManager = world.getRecipeManager();
        final Optional<BlastingRecipe> optional = recipeManager.getRecipe(IRecipeType.BLASTING, this, world);
        if (optional.isPresent()) return optional.get();

        final Optional<FurnaceRecipe> optional1 = recipeManager.getRecipe(IRecipeType.SMELTING, this, world);
        return optional1.orElse(null);
    }

    @Override
    protected int getProcessTime(final AbstractCookingRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(final AbstractCookingRecipe recipe) {
        return Collections.singleton(recipe.getCraftingResult(this));
    }

    @Override
    public int[] getSlotsForFace(final Direction side) {
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, @Nullable final Direction direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "fusion_control_computer");
    }

    @Override
    protected Container createMenu(final int id, final PlayerInventory playerInventory) {
        return new FusionControlComputerContainer(id, playerInventory, this, this.fields);
    }
}
