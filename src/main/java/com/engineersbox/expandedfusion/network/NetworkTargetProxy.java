package com.engineersbox.expandedfusion.network;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.resolver.JITRegistrationResolver;
import com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer.FusionControlComputer;
import com.engineersbox.expandedfusion.elements.block.structure.NiobiumTitaniumCoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class NetworkTargetProxy implements IProxy {
    private MinecraftServer server = null;
    private final JITRegistrationResolver registrationResolver;

    NetworkTargetProxy() {
        this.registrationResolver = new JITRegistrationResolver.Builder()
                .withPackageName(ExpandedFusion.class.getPackage().getName())
                .withModId(ExpandedFusion.MOD_ID)
                .build();
        Registration.addTabGroup(
                ExpandedFusion.MOD_ID,
                "Expanded Fusion Core",
                () -> new ItemStack(RegistryObjectContext.getBlockRegistryObject(NiobiumTitaniumCoil.PROVIDER_NAME).asBlock())
        );
        Registration.addTabGroup(
                "expandedfusion_machines",
                "Expanded Fusion Machines",
                () -> new ItemStack(RegistryObjectContext.getBlockRegistryObject(FusionControlComputer.PROVIDER_NAME).asBlock())
        );

        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcPublish);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcSubscribe);

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
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(final FMLDedicatedServerSetupEvent event) {
        }
    }
}