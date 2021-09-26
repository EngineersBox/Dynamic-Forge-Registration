package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.annotation.data.loot.NoDefaultLootItem;
import net.minecraft.block.Block;

@NoDefaultLootItem
public class NoDefaultLootTableBlock extends Block {

    public NoDefaultLootTableBlock(final Properties properties) {
        super(properties);
    }
}
