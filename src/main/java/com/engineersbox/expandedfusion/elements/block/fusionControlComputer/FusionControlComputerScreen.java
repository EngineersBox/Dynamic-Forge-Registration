package com.engineersbox.expandedfusion.elements.block.fusionControlComputer;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.common.machine.screen.AbstractMachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FusionControlComputerScreen extends AbstractMachineScreen<FusionControlComputerContainer> {
    public static final ResourceLocation TEXTURE = ExpandedFusion.getId("textures/gui/fusion_control_computer.png");

    public FusionControlComputerScreen(FusionControlComputerContainer containerIn, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(containerIn, playerInventory, titleIn);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return TEXTURE;
    }

    @Override
    protected int getProgressArrowPosX(int guiPosX) {
        return guiPosX + 79;
    }

    @Override
    protected int getProgressArrowPosY(int guiPosY) {
        return guiPosY + 35;
    }
}
