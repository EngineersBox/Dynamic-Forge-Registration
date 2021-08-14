package com.engineersbox.expandedfusion.network;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.register.*;
import com.engineersbox.expandedfusion.register.handler.ItemClientEventHandler;
import com.engineersbox.expandedfusion.register.resolver.JITRegistrationResolver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NetworkTargetProxy implements IProxy {
    private MinecraftServer server = null;
    private static final JITRegistrationResolver REGISTRATION_RESOLVER = new JITRegistrationResolver.Builder()
            .withLogger(ExpandedFusion.LOGGER)
            .withPackageName("com.engineersbox.expandedfusion")
            .withDefaultEventHandlers()
            .withCustomEventHandlers(
                ItemClientEventHandler.class
            )
            .build();

    NetworkTargetProxy() {
        Registration.register(REGISTRATION_RESOLVER);

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        // Add listeners for registry events
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Fluid.class, ModFluids::registerFluids);

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
    }

    @Override
    public void tryFetchTagsHack() {}

    private void imcEnqueue(final InterModEnqueueEvent event) {
    }

    private void imcProcess(final InterModProcessEvent event) {
    }

    private void serverAboutToStart(final FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    public static class Client extends NetworkTargetProxy {
        public Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(REGISTRATION_RESOLVER::publishEvent);

            MinecraftForge.EVENT_BUS.addListener(this::setFog);
        }

        @Override
        public void tryFetchTagsHack() {
            TagRegistryManager.fetchTags();
        }

        public void setFog(final EntityViewRenderEvent.FogColors fog) {
            final World w = fog.getInfo().getRenderViewEntity().getEntityWorld();
            final BlockPos pos = fog.getInfo().getBlockPos();
            final BlockState bs = w.getBlockState(pos);
            final Block b = bs.getBlock();

//            if (b.equals(ModBlocks.OIL)) {
//                float red = 0.02F;
//                float green = 0.02F;
//                float blue = 0.02F;
//                fog.setRed(red);
//                fog.setGreen(green);
//                fog.setBlue(blue);
//            }
        }
    }

    public static class Server extends NetworkTargetProxy {
        public Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        }
    }
}