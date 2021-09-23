package com.engineersbox.expandedfusion.core.registration.handler.element;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidFog;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.FluidRegistryObject;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@InternalEventHandler
@ClientEventHandler
public class FogClientEventHandler implements EventSubscriptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(FogClientEventHandler.class);
    private final ElementRegistryProvider elementRegistryProvider;
    private Map<Block, FluidFog> fogToBeSet;

    @Inject
    public FogClientEventHandler(final ElementRegistryProvider elementRegistryProvider) {
        this.elementRegistryProvider = elementRegistryProvider;
    }

    private void retrieveFogSettableFluids() {
        elementRegistryProvider.sourceFluids.forEach((final String providerName, final FluidRegistryObject<? extends Fluid> sourceFluid) -> {
            final Block fluidBlock;
            try {
                fluidBlock = RegistryObjectContext.getBlockRegistryObject(providerName).asBlock();
            } catch (final RegistryObjectRetrievalException e) {
                return;
            }
            final FluidFog fluidFogAnnotation = sourceFluid.asFluid().getClass().getAnnotation(FluidFog.class);
            if (fluidFogAnnotation == null) {
                LOGGER.trace("No @FluidFog annotation present on fluid {}, skipping", sourceFluid.asFluid().getClass().getName());
                return;
            }
            fogToBeSet.put(fluidBlock, fluidFogAnnotation);
        });
    }

    @Subscriber
    public void setFog(final EntityViewRenderEvent.FogColors fog) {
        if (this.fogToBeSet == null) {
            this.fogToBeSet = new HashMap<>();
            retrieveFogSettableFluids();
        }
        final World w = fog.getInfo().getRenderViewEntity().getEntityWorld();
        final BlockPos pos = fog.getInfo().getBlockPos();
        final BlockState bs = w.getBlockState(pos);
        final Block b = bs.getBlock();

        this.fogToBeSet.entrySet()
                .stream()
                .filter((final Map.Entry<Block, FluidFog> entry) -> b.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach((final FluidFog fluidFog) -> {
                    fog.setRed(fluidFog.red());
                    fog.setGreen(fluidFog.green());
                    fog.setBlue(fluidFog.blue());
                });
    }

}
