package com.engineersbox.expandedfusion.core.elements.machine.tileentity;

import com.engineersbox.expandedfusion.core.elements.DataField;
import com.engineersbox.expandedfusion.core.elements.MachineTier;
import com.engineersbox.expandedfusion.core.elements.capability.EnergyStorageCapability;
import com.engineersbox.expandedfusion.core.elements.machine.IMachineInventory;
import com.engineersbox.expandedfusion.core.elements.machine.exception.UnknownDataFieldException;
import com.engineersbox.expandedfusion.core.util.InventoryUtils;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.StreamSupport;

public abstract class AbstractMachineTileEntity<R extends IRecipe<?>> extends AbstractMachineBaseTileEntity implements IMachineInventory {
    public final EnergyProperties energyProps;

    public static final int FIELDS_COUNT = 7;

    protected float progress;
    protected int processTime;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(final int index) {
            switch (DataField.fromInt(index)) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case ENERGY_STORED_LOWER:
                    // Energy lower bytes
                    return AbstractMachineTileEntity.this.getEnergyStored() & 0xFFFF;
                case ENERGY_STORED_HIGHER:
                    // Energy upper bytes
                    return (AbstractMachineTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case MAX_ENERGY_STORED_LOWER:
                    // Max energy lower bytes
                    return AbstractMachineTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case MAX_ENERGY_STORED_HIGHER:
                    // Max energy upper bytes
                    return (AbstractMachineTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case PROGRESS:
                    return (int) AbstractMachineTileEntity.this.progress;
                case PROCESS_TIME:
                    return AbstractMachineTileEntity.this.processTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(final int index, final int value) {
            switch (DataField.fromInt(index)) {
                case PROGRESS:
                    AbstractMachineTileEntity.this.progress = value;
                    break;
                case PROCESS_TIME:
                    AbstractMachineTileEntity.this.processTime = value;
                    break;
                default:
                    throw new UnknownDataFieldException(index);
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };

    protected AbstractMachineTileEntity(final TileEntityType<?> typeIn,
                                        final int inventorySize,
                                        final  MachineTier tier,
                                        final EnergyProperties energyProps) {
        super(typeIn, inventorySize, tier.energyCapacity, 500, 0, tier);
        this.energyProps = energyProps;
    }

    @Override
    public EnergyStorageCapability getEnergyImpl() {
        return super.getEnergyImpl();
    }

    protected abstract int getEnergyUsedPerTick();

    protected BlockState getActiveState(final BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, true);
    }

    protected BlockState getInactiveState(final BlockState currentState) {
        return currentState.with(AbstractFurnaceBlock.LIT, false);
    }

    /**
     * Indexes of output slots. Recipe outputs will be merged into these slots.
     *
     * @return The output slots
     */
    protected abstract int[] getOutputSlots();

    /**
     * Get the recipe that matches the current inventory.
     *
     * @return The recipe to process, or null if there is no matching recipe
     */
    @Nullable
    protected abstract R getRecipe();

    /**
     * Get the base time (in ticks) to process the given recipe.
     *
     * @param recipe The recipe
     * @return Process time in ticks
     */
    protected abstract int getProcessTime(final R recipe);

    /**
     * Get the processing speed. This is added to processing progress every tick. A speed of 1 would
     * process a 200 tick recipe in 200 ticks, speed 2 would be 100 ticks. Should account for speed
     * upgrades.
     *
     * @return The processing speed
     */
    protected float getProcessSpeed() {
        return tier.processingSpeed;
    }

    /**
     * Get the results of the recipe.
     *
     * @param recipe The recipe
     * @return The results of the processing operation
     */
    protected abstract Collection<ItemStack> getProcessResults(final R recipe);

    /**
     * Get all possible results of processing this recipe. Override if recipes can contain a
     * variable number of outputs.
     *
     * @param recipe The recipe
     * @return All possible results of the processing operation
     */
    protected Collection<ItemStack> getPossibleProcessResult(final R recipe) {
        return getProcessResults(recipe);
    }

    @Override
    public int getInputSlotCount() {
        return 1;
    }

    protected void sendUpdate(final BlockState newState) {
        if (world == null) return;
        final BlockState oldState = world.getBlockState(pos);
        if (oldState != newState) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    protected void setInactiveState() {
        if (world == null) return;
        sendUpdate(getInactiveState(world.getBlockState(pos)));
    }

    private void handleProcessProgressChanges(final R recipe) {
        if (world == null) {
            return;
        }
        processTime = getProcessTime(recipe);
        progress += getProcessSpeed();
        energy.consumeEnergy(getEnergyUsedPerTick());

        if (progress >= processTime) {
            // Create result
            getProcessResults(recipe).forEach(this::storeResultItem);
            consumeIngredients(recipe);
            progress = 0;

            if (getRecipe() == null) {
                setInactiveState();
            }
        } else {
            sendUpdate(getActiveState(world.getBlockState(pos)));
        }
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        final R recipe = getRecipe();
        if (recipe != null && canMachineRun(recipe)) {
            handleProcessProgressChanges(recipe);
        } else {
            if (recipe == null) {
                progress = 0;
            }
            setInactiveState();
        }
    }

    private boolean canMachineRun(final R recipe) {
        return world != null
                && getEnergyStored() >= getEnergyUsedPerTick()
                && hasRoomInOutput(getPossibleProcessResult(recipe));
    }

    protected boolean hasRoomInOutput(final Iterable<ItemStack> results) {
        return StreamSupport.stream(results.spliterator(), false).allMatch(this::hasRoomForOutputItem);
    }

    private boolean hasRoomForOutputItem(final ItemStack stack) {
        return Arrays.stream(getOutputSlots()).anyMatch((i) -> InventoryUtils.canItemsStack(stack, getStackInSlot(i)));
    }

    protected void storeResultItem(final ItemStack stack) {
        // Merge the item into any output slot it can fit in
        for (int i : getOutputSlots()) {
            final ItemStack output = getStackInSlot(i);
            if (InventoryUtils.canItemsStack(stack, output)) {
                if (output.isEmpty()) {
                    setInventorySlotContents(i, stack);
                } else {
                    output.setCount(output.getCount() + stack.getCount());
                }
                return;
            }
        }
    }

    protected void consumeIngredients(final R recipe) {
        decrStackSize(0, 1);
    }

    @Override
    public IIntArray getFields() {
        return fields;
    }

    @Override
    public void read(final BlockState state, final CompoundNBT tags) {
        super.read(state, tags);
        this.progress = tags.getInt("Progress");
        this.processTime = tags.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT write(final CompoundNBT tags) {
        super.write(tags);
        tags.putInt("Progress", (int) this.progress);
        tags.putInt("ProcessTime", this.processTime);
        return tags;
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        final CompoundNBT tags = packet.getNbtCompound();
        this.progress = tags.getInt("Progress");
        this.processTime = tags.getInt("ProcessTime");
    }

    @Override
    public CompoundNBT getUpdateTag() {
        final CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progress", (int) this.progress);
        tags.putInt("ProcessTime", this.processTime);
        return tags;
    }
}
