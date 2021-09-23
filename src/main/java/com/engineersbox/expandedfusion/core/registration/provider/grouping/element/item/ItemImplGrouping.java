package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item;

import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.item.DuplicateItemComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemImplGrouping implements ImplGrouping {
    private Class<? extends Item> item;

    public Class<? extends Item> getItem() {
        return this.item;
    }

    public ItemProvider getItemProviderAnnotation() {
        if (this.item == null) {
            return null;
        }
        return this.item.getAnnotation(ItemProvider.class);
    }

    public void setItem(final Class<? extends Item> item) throws DuplicateItemComponentBinding {
        if (this.item != null) {
            throw new DuplicateItemComponentBinding(this.item, item);
        }
        this.item = item;
    }

    @Override
    public List<Class<?>> getAllClasses() {
        return new ArrayList<>();
    }

    @Override
    public <E> E getCommonIdentifier() {
        return null;
    }
}
