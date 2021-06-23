package com.engineersbox.expandedfusion;

import com.engineersbox.expandedfusion.common.register.Blocks;
import com.engineersbox.expandedfusion.common.register.Fluids;
import com.engineersbox.expandedfusion.common.register.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExpandedFusion.MODID)
public class ExpandedFusion {
    public static final String MODID = "expandedfusion";

    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public ExpandedFusion() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        registerElements();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerElements() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        Fluids.FLUIDS.register(modEventBus);
    }
}
