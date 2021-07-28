package com.engineersbox.expandedfusion.core.common.machine.tileentity;

public class EnergyProperties {
    public final int maxEnergy;
    public final int maxReceive;
    public final int usedPerTick;

    public EnergyProperties(final int maxEnergy,
                            final int maxReceive,
                            final int usedPerTick) {
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.usedPerTick = usedPerTick;
    }
}
