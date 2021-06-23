package com.engineersbox.expandedfusion;

import com.engineersbox.expandedfusion.common.register.Blocks;
import com.engineersbox.expandedfusion.common.register.Items;
import com.engineersbox.expandedfusion.common.register.ModItemGroups;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = ExpandedFusion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(ExpandedFusion.MODID + " Mod Event Subscriber");

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        final Item.Properties properties = new Item.Properties()
                .group(ModItemGroups.MOD_ITEM_GROUP);
        // Automatically register BlockItems for all our Blocks
        Blocks.BLOCKS.getEntries().stream()
            .map(RegistryObject::get)
            // Extra filtering can be done if some blocks don't need to have a BlockItem automatically registered for them
            // .filter(block -> needsItemBlock(block))
            .forEach(block -> {
                final BlockItem blockItem = new BlockItem(block, properties);
                final ResourceLocation location = block.getRegistryName();
                if (location == null) {
                    LOGGER.warn(String.format(
                        "Block %s was not registered to any known registry, skipping",
                        block.getTranslationKey()
                    ));
                }
                blockItem.setRegistryName(block.getRegistryName());
                registry.register(blockItem);
            });
        LOGGER.debug("Registered BlockItems");
        Items.ITEMS.getEntries().stream()
            .map(RegistryObject::get)
            .forEach(registry::register);
    }

}
