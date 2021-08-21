package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.elements.block.structure.NiobiumTitaniumCoil;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.resolver.JITResolver;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Registration {
    public static final ItemGroup CREATIVE_TAB_ITEM_GROUP = new ModItemGroups.ModItemGroup(
            ExpandedFusion.MOD_ID,
            () -> new ItemStack(RegistryObjectContext.getBlockRegistryObject(NiobiumTitaniumCoil.PROVIDER_NAME).asBlock())
    );
    public static final DeferredRegister<Fluid> FLUIDS = create(ForgeRegistries.FLUIDS);
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = create(ForgeRegistries.CONTAINERS);
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = create(ForgeRegistries.RECIPE_SERIALIZERS);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = create(ForgeRegistries.TILE_ENTITIES);

    private Registration() {throw new IllegalAccessError("Utility class");}

    public static void register(final JITResolver registrationResolver) {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FLUIDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);

        registrationResolver.instantiateResolvers();
        registrationResolver.registerAll();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> Collection<T> getBlocks(final Class<T> clazz) {
        return BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(clazz::isInstance)
                .map(block -> (T) block)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Item> Collection<T> getItems(final Class<T> clazz) {
        return ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(clazz::isInstance)
                .map(item -> (T) item)
                .collect(Collectors.toList());
    }

    public static Collection<Item> getItems(final Predicate<Item> predicate) {
        return ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(final IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, ExpandedFusion.MOD_ID);
    }
}
