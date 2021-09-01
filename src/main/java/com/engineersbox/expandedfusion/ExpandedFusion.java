package com.engineersbox.expandedfusion;

import com.engineersbox.expandedfusion.network.IProxy;
import com.engineersbox.expandedfusion.network.NetworkTargetProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ExpandedFusion.MOD_ID)
public class ExpandedFusion {
    public static final String MOD_ID = "expandedfusion";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ExpandedFusion INSTANCE;
    public static IProxy PROXY;

    public ExpandedFusion() {
        INSTANCE = this;
        PROXY = DistExecutor.safeRunForDist(() -> NetworkTargetProxy.Client::new, () -> NetworkTargetProxy.Server::new);

    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
