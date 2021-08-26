package com.engineersbox.expandedfusion.core.registration.registryObject.element;

import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;

public class ItemRegistryObject<T extends Item> extends RegistryObjectWrapper<T> implements IItemProvider {
    public ItemRegistryObject(final RegistryObject<T> item) {
        super(item);
    }

    @Override
    public Item asItem() {
        return registryObject.get();
    }
}