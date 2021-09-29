package com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer;

import com.engineersbox.expandedfusion.core.elements.MachineTier;
import com.engineersbox.expandedfusion.core.elements.machine.tileentity.AbstractMachineTileEntity;
import com.engineersbox.expandedfusion.core.elements.machine.tileentity.EnergyProperties;
import com.engineersbox.expandedfusion.core.elements.machine.tileentity.TransportSlotConfiguration;
import com.engineersbox.expandedfusion.core.util.TextUtil;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.TileEntityProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
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

@TileEntityProvider(name = FusionControlComputer.PROVIDER_NAME)
public class FusionControlComputerTileEntity extends AbstractMachineTileEntity<AbstractCookingRecipe> {

    private static final TransportSlotConfiguration SLOT_CONFIG = new TransportSlotConfiguration.Builder()
        .withInputSlots(0)
        .withOutputSlots(1)
        .build();

    public FusionControlComputerTileEntity() {
        super(
            RegistryObjectContext.getTileEntityRegistryObject(FusionControlComputer.PROVIDER_NAME).asTileEntityType(),
            SLOT_CONFIG.slots.length,
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
        return SLOT_CONFIG.output;
    }

    @Override
    @Nullable
    protected AbstractCookingRecipe getRecipe() {
        if (world == null) return null;

        final RecipeManager recipeManager = world.getRecipeManager();
        final Optional<BlastingRecipe> blastingRecipe = recipeManager.getRecipe(IRecipeType.BLASTING, this, world);
        if (blastingRecipe.isPresent()) return blastingRecipe.get();

        final Optional<FurnaceRecipe> furnaceRecipe = recipeManager.getRecipe(IRecipeType.SMELTING, this, world);
        return furnaceRecipe.orElse(null);
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
        return SLOT_CONFIG.slots;
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
