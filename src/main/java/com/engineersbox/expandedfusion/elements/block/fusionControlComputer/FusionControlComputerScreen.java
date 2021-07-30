package com.engineersbox.expandedfusion.elements.block.fusionControlComputer;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.common.machine.screen.AbstractMachineScreen;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockScreenProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

@BlockScreenProvider(name = "fusion_control_computer")
public class FusionControlComputerScreen extends AbstractMachineScreen<FusionControlComputerContainer> {
    public static final ResourceLocation TEXTURE = ExpandedFusion.getId("textures/gui/fusion_control_computer.png");

    public FusionControlComputerScreen(final FusionControlComputerContainer containerIn,
                                       final PlayerInventory playerInventory,
                                       final ITextComponent titleIn) {
        super(containerIn, playerInventory, titleIn);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return TEXTURE;
    }

    @Override
    protected int getProgressArrowPosX(final int guiPosX) {
        return guiPosX + 79;
    }

    @Override
    protected int getProgressArrowPosY(final int guiPosY) {
        return guiPosY + 35;
    }
}
