package com.engineersbox.expandedfusion;

import com.engineersbox.expandedfusion.network.IProxy;
import com.engineersbox.expandedfusion.network.NetworkTargetProxy;
import com.engineersbox.expandedfusion.register.registry.BlockRegistryObject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExpandedFusion.MOD_ID)
public class ExpandedFusion {
    public static final String MOD_ID = "expandedfusion";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ExpandedFusion INSTANCE;
    public static IProxy PROXY;

    public static class RegistryProvider {
        public final Map<String, BlockRegistryObject<Block>> blocks = new HashMap<>();
        public final Map<String, TileEntityType<? extends TileEntity>> tileEntities = new HashMap<>();
        public final Map<String, ContainerType<? extends Container>> containers = new HashMap<>();
    }

    @Provides
    @Singleton
    public RegistryProvider provideRegistryProvider() {
        return new RegistryProvider();
    }

    public ExpandedFusion() {
        INSTANCE = this;
        PROXY = DistExecutor.safeRunForDist(() -> NetworkTargetProxy.Client::new, () -> NetworkTargetProxy.Server::new);

    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
