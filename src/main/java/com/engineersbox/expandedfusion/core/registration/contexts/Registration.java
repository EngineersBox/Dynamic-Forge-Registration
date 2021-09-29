package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.exception.contexts.CreativeTabItemGroupRegistrationException;
import com.engineersbox.expandedfusion.core.registration.resolver.JITResolver;
import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Singleton
public final class Registration {
    private final String modID;
    private static final Map<String, ItemGroup> CREATIVE_TABS = new HashMap<>();

    private DeferredRegister<Fluid> fluids;
    private DeferredRegister<Block> blocks;
    private DeferredRegister<ContainerType<?>> containers;
    private DeferredRegister<Item> items;
    private DeferredRegister<IRecipeSerializer<?>> recipeSerializers;
    private DeferredRegister<TileEntityType<?>> tileEntities;

    public Registration(final String modID) {
        this.modID = modID;
    }

    private void configureDeferredRegistries() {
        fluids = createRegister(ForgeRegistries.FLUIDS);
        blocks = createRegister(ForgeRegistries.BLOCKS);
        containers = createRegister(ForgeRegistries.CONTAINERS);
        items = createRegister(ForgeRegistries.ITEMS);
        recipeSerializers = createRegister(ForgeRegistries.RECIPE_SERIALIZERS);
        tileEntities = createRegister(ForgeRegistries.TILE_ENTITIES);
    }

    public void register(final JITResolver registrationResolver) {
        configureDeferredRegistries();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        fluids.register(modEventBus);
        blocks.register(modEventBus);
        containers.register(modEventBus);
        items.register(modEventBus);
        recipeSerializers.register(modEventBus);
        tileEntities.register(modEventBus);

        registrationResolver.instantiateResolvers();
        registrationResolver.registerAll();
    }

    public DeferredRegister<Fluid> getFluidRegister() {
        return fluids;
    }

    public DeferredRegister<Block> getBlockRegister() {
        return blocks;
    }

    public DeferredRegister<ContainerType<?>> getContainerRegister() {
        return containers;
    }

    public DeferredRegister<Item> getItemRegister() {
        return items;
    }

    public DeferredRegister<IRecipeSerializer<?>> getRecipeSerializerRegister() {
        return recipeSerializers;
    }

    public DeferredRegister<TileEntityType<?>> getTileEntityRegister() {
        return tileEntities;
    }

    private <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createRegister(final IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, this.modID);
    }

    public static ItemGroup getTabGroup(final String name) {
        if (!CREATIVE_TABS.containsKey(name)) {
            throw new CreativeTabItemGroupRegistrationException(String.format(
                    "Creative tab not present for name key %s",
                    name
            ));
        }
        return CREATIVE_TABS.get(name);
    }

    public static void addTabGroup(@Nonnull final String name,
                                   @Nonnull final String nameMapping,
                                   @Nonnull final Supplier<ItemStack> iconSupplier) {
        if (CREATIVE_TABS.containsKey(name)) {
            return;
        }
        CREATIVE_TABS.put(name, new ModItemGroup(
                name,
                nameMapping,
                iconSupplier
        ));
    }
}
