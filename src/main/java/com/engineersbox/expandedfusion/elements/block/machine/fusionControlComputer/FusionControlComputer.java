package com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer;

import com.engineersbox.expandedfusion.core.common.MachineTier;
import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.provider.BlockImplType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@BlockProvider(
        name = FusionControlComputer.PROVIDER_NAME,
        type = BlockImplType.TILE_ENTITY
)
public class FusionControlComputer extends AbstractMachineBlock {
    public static final String PROVIDER_NAME = "fusion_control_computer";

    @SuppressWarnings("unused")
    public FusionControlComputer() {
        super(MachineTier.REINFORCED, Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL));
    }

    @Override
    protected void interactWith(final World worldIn, final BlockPos pos, final PlayerEntity player) {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof FusionControlComputerTileEntity) {
            player.openContainer((INamedContainerProvider) tileEntity);
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(final IBlockReader worldIn) {
        return new FusionControlComputerTileEntity();
    }
}
