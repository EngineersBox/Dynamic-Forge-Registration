package com.engineersbox.expandedfusion.network;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.resolver.JITRegistrationResolver;
import com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer.FusionControlComputer;
import com.engineersbox.expandedfusion.elements.block.structure.NiobiumTitaniumCoil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

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
                ExpandedFusion.MOD_ID + "_machines",
                "Expanded Fusion Machines",
                () -> new ItemStack(RegistryObjectContext.getBlockRegistryObject(FusionControlComputer.PROVIDER_NAME).asBlock())
        );

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
    }

    private void serverAboutToStart(final FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    public static class Client extends NetworkTargetProxy {
        public Client() {}
    }

    public static class Server extends NetworkTargetProxy {
        public Server() {}
    }
}