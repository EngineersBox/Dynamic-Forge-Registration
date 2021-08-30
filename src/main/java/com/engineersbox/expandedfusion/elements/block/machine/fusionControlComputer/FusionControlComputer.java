package com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer;

import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternKey;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.PatternLine;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.UnlockCriterion;
import com.engineersbox.expandedfusion.core.registration.data.recipe.AccessCriterion;
import com.engineersbox.expandedfusion.core.registration.data.recipe.IngredientType;
import com.engineersbox.expandedfusion.core.elements.MachineTier;
import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.provider.element.block.BlockImplType;
import com.engineersbox.expandedfusion.elements.block.structure.CopperBerylliumBondedShielding;
import com.engineersbox.expandedfusion.elements.block.structure.NiobiumTitaniumCoil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@LangMetadata(
        nameMapping = FusionControlComputer.NAME_MAPPING
)
@CraftingRecipe(
        pattern = {
                @PatternLine("INI"),
                @PatternLine("SIS"),
                @PatternLine("INI"),
        },
        keys = {
                @PatternKey(symbol = 'I', ingredient = "iron_ingot", type = IngredientType.ITEM),
                @PatternKey(symbol = 'N', ingredient = NiobiumTitaniumCoil.PROVIDER_NAME, type = IngredientType.ITEM),
                @PatternKey(symbol = 'S', ingredient = CopperBerylliumBondedShielding.PROVIDER_NAME, type = IngredientType.ITEM)
        },
        criteria = @UnlockCriterion(
                key = "has_ntc",
                ingredient = NiobiumTitaniumCoil.PROVIDER_NAME,
                requirement = AccessCriterion.HAS_ITEM_ITEM
        )
)
@BlockProvider(
        name = FusionControlComputer.PROVIDER_NAME,
        type = BlockImplType.TILE_ENTITY
)
public class FusionControlComputer extends AbstractMachineBlock {
    public static final String PROVIDER_NAME = "fusion_control_computer";
    public static final String NAME_MAPPING = "Fusion Control Computer";

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
