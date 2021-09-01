package com.engineersbox.expandedfusion.network;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.register.*;
import com.engineersbox.expandedfusion.core.registration.handler.element.BlockClientEventHandler;
import com.engineersbox.expandedfusion.core.registration.handler.element.ItemClientEventHandler;
import com.engineersbox.expandedfusion.core.registration.resolver.JITRegistrationResolver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NetworkTargetProxy implements IProxy {
    private MinecraftServer server = null;
    private static final JITRegistrationResolver REGISTRATION_RESOLVER = new JITRegistrationResolver.Builder()
            .withLogger(ExpandedFusion.LOGGER)
            .withPackageName("com.engineersbox.expandedfusion")
            .withModId(ExpandedFusion.MOD_ID)
            .build();

    NetworkTargetProxy() {
        Registration.register(REGISTRATION_RESOLVER);

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcPublish);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcSubscribe);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(REGISTRATION_RESOLVER::publishCommonEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(REGISTRATION_RESOLVER::publishGatherDataEvent);

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
    }

    private void imcPublish(final InterModEnqueueEvent event) {
    }

    private void imcSubscribe(final InterModProcessEvent event) {
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(REGISTRATION_RESOLVER::publishClientEvent);

            MinecraftForge.EVENT_BUS.addListener(this::setFog);
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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(REGISTRATION_RESOLVER::publishServerEvent);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        }
    }
}