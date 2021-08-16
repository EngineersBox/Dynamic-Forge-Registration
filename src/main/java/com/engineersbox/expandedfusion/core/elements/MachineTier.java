package com.engineersbox.expandedfusion.core.elements;

public enum MachineTier {

    BASIC(10_000, 1.0f),
    REINFORCED(50_000, 2.0f),
    ADVANCED(100_000, 3.0f),
    SUPER(500_000, 4.0f);

    public final int energyCapacity;
    public final float processingSpeed;

    MachineTier(final int energyCapacity, final float processingSpeed) {
        this.energyCapacity = energyCapacity;
        this.processingSpeed = processingSpeed;
    }

}
