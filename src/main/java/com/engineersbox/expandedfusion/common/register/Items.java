package com.engineersbox.expandedfusion.common.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExpandedFusion.MODID);

    public static final RegistryObject<Item> EXAMPLE_CRYSTAL = ITEMS.register("example_crystal", () -> new Item(new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP)));
}
