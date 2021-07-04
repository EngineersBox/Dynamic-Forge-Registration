package com.engineersbox.expandedfusion;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExpandedFusion.MOD_ID)
public class ExpandedFusion {
    public static final String MOD_ID = "expandedfusion";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ExpandedFusion INSTANCE;
    public static IProxy PROXY;

    public ExpandedFusion() {
        INSTANCE = this;
        PROXY = DistExecutor.safeRunForDist(() -> SideProxy.Client::new, () -> SideProxy.Server::new);
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
