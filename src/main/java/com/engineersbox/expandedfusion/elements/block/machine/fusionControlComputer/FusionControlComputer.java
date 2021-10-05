package com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer;

import com.engineersbox.expandedfusion.core.elements.MachineTier;
import com.engineersbox.expandedfusion.core.elements.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.PatternKey;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.PatternLine;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.UnlockCriterion;
import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.data.recipe.AccessCriterion;
import com.engineersbox.expandedfusion.core.registration.data.recipe.IngredientType;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;
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

@LangMetadata(locales = @LocaleEntry(key = LangKey.EN_US, mapping = FusionControlComputer.NAME_MAPPING))
@CraftingRecipe(
        pattern = {
                @PatternLine("INI"),
                @PatternLine("SIS"),
                @PatternLine("INI"),
        },
        keys = {
                @PatternKey(symbol = 'I', ingredient = "ingots/iron", type = IngredientType.TAG),
                @PatternKey(symbol = 'N', ingredient = "blocks/" + NiobiumTitaniumCoil.PROVIDER_NAME, type = IngredientType.TAG),
                @PatternKey(symbol = 'S', ingredient = CopperBerylliumBondedShielding.PROVIDER_NAME, type = IngredientType.ITEM)
        },
        criteria = @UnlockCriterion(
                key = "has_ntc",
                ingredient = NiobiumTitaniumCoil.PROVIDER_NAME,
                requirement = AccessCriterion.HAS_ITEM
        )
)
@Tag(
        path = "machine/fusion_reactor",
        mirroredItemTagPath = "machine/fusion_reactor_item"
)
@BlockProvider(
        name = FusionControlComputer.PROVIDER_NAME,
        type = BlockImplType.INTERACTIVE_TILE_ENTITY,
        tabGroup = FusionControlComputer.TAB_GROUP
)
public class FusionControlComputer extends AbstractMachineBlock {
    public static final String PROVIDER_NAME = "fusion_control_computer";
    public static final String NAME_MAPPING = "Fusion Control Computer";
    public static final String TAB_GROUP = "expandedfusion_machines";

    @SuppressWarnings("unused")
    public FusionControlComputer() {
        super(
                MachineTier.REINFORCED,
                Properties.create(Material.IRON)
                        .lootFrom(() -> RegistryObjectContext.getBlockRegistryObject(NiobiumTitaniumCoil.PROVIDER_NAME).asBlock())
                        .setRequiresTool()
                        .hardnessAndResistance(6.0F, 20.0F)
                        .sound(SoundType.METAL)
        );
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
