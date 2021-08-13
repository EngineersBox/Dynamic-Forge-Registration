package com.engineersbox.expandedfusion.register.provider.grouping.item;

import com.engineersbox.expandedfusion.register.annotation.item.ItemProvider;
import com.engineersbox.expandedfusion.register.exception.grouping.DuplicateItemComponentBinding;
import com.engineersbox.expandedfusion.register.provider.grouping.ImplGrouping;
import net.minecraft.item.Item;

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
}
