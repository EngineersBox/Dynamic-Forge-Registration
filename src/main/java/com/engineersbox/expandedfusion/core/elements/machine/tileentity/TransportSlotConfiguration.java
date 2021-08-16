package com.engineersbox.expandedfusion.core.elements.machine.tileentity;

import org.apache.commons.lang3.ArrayUtils;

public class TransportSlotConfiguration {

    public final int[] slots;
    public final int[] input;
    public final int[] output;

    private TransportSlotConfiguration(final int[] input, final int[] output) {
        this.slots = ArrayUtils.addAll(input, output);
        this.input = input;
        this.output = output;
    }

    public static class Builder {
        private int[] in;
        private int[] out;

        public Builder withInputSlots(final int ...input) {
            this.in = input.clone();
            return this;
        }

        public Builder withOutputSlots(final int ...output) {
            this.out = output.clone();
            return this;
        }

        public TransportSlotConfiguration build() {
            return new TransportSlotConfiguration(this.in, this.out);
        }
    }
}
