package com.engineersbox.expandedfusion.core.elements.machine.screen;

import com.engineersbox.expandedfusion.core.elements.machine.container.AbstractMachineContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractMachineScreen<C extends AbstractMachineContainer<?>> extends AbstractMachineBaseScreen<C> {
    public AbstractMachineScreen(final C containerIn,
                                 final PlayerInventory playerInventoryIn,
                                 final ITextComponent titleIn) {
        super(containerIn, playerInventoryIn, titleIn);
    }

    protected abstract int getProgressArrowPosX(final int guiPosX);

    protected abstract int getProgressArrowPosY(final int guiPosY);

    @Override
    public void render(final MatrixStack matrixStack,
                       final int mouseX,
                       final int mouseY,
                       final float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final MatrixStack matrixStack,
                                                   final float partialTicks,
                                                   final int x,
                                                   final int y) {
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y);

        final int xPos = (this.width - this.xSize) / 2;
        final int yPos = (this.height - this.ySize) / 2;

        // Progress arrow
        final int progress = container.getProgress();
        final int processTime = container.getProcessTime();
        final int length = progress > 0 && progress < processTime ? progress * 24 / processTime : 0;
        blit(matrixStack, getProgressArrowPosX(xPos), getProgressArrowPosY(yPos), 176, 14, length + 1, 16);

        // Energy meter
        final int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(matrixStack, xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }
    }
}
