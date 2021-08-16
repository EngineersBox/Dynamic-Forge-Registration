package com.engineersbox.expandedfusion.core.elements.tileentity;

import com.engineersbox.expandedfusion.core.util.math.MCMathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class LockableSidedInventoryTileEntity extends LockableTileEntity implements ISidedInventory {
    protected NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers =
            SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    protected LockableSidedInventoryTileEntity(final TileEntityType<?> typeIn, final int inventorySize) {
        super(typeIn);
        this.items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (final ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(final int index) {
        if (index < 0 || index >= items.size()) {
            return ItemStack.EMPTY;
        }
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return ItemStackHelper.getAndSplit(items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
    }

    @Override
    public boolean isUsableByPlayer(final PlayerEntity player) {
        return world != null && world.getTileEntity(pos) == this && MCMathUtils.distanceSq(player, pos) <= 64;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void read(final BlockState stateIn, final CompoundNBT tags) {
        super.read(stateIn, tags);
        items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, items);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tags) {
        super.write(tags);
        ItemStackHelper.saveAllItems(tags, items);
        return tags;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        final CompoundNBT tags = getUpdateTag();
        ItemStackHelper.saveAllItems(tags, items);
        return new SUpdateTileEntityPacket(pos, 1, tags);
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        ItemStackHelper.loadAllItems(packet.getNbtCompound(), items);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP)
                return handlers[0].cast();
            if (side == Direction.DOWN)
                return handlers[1].cast();
            return handlers[2].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        for (final LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}
