package com.engineersbox.expandedfusion.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public Config(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        // Register config props here
        builder.pop();
    }
}
