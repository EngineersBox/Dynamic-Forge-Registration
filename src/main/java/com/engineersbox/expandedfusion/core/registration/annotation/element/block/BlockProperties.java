package com.engineersbox.expandedfusion.core.registration.annotation.element.block;

import net.minecraft.util.Direction;

public @interface BlockProperties {
    String material() default "AIR";
    // Mutually exclusive with materialColour, will prefer materialColour if both are supplied
    String[] dyeColour() default {};
    String[] materialColour() default {};
    boolean doesNotBlockMovement() default false;
    boolean notSolid() default false;
    int harvestLevel() default -1;
    String[] harvestTool() default {};
    float slipperiness() default 0.6F;
    float speedFactor() default 1.0F;
    float jumpFactor() default 1.0F;
    String soundType() default "STONE";
    int lightLevel() default 0;
    float[] hardness() default {};
    float[] resistance() default {};
    boolean tickRandomly() default false;
    boolean variableOpacity() default false;
    boolean noDrops() default false;
    String[] lootFrom() default {};
    boolean isAir() default false;
    AllowsSpawn[] allowsSpawn() default {};
    boolean[] isOpaque() default {};
    boolean[] suffocates() default {};
    boolean[] blocksVision() default {};
    boolean[] needsPostProcessing() default {};
    boolean[] emissiveRendering() default {};
    boolean requiresTool() default false;

    @interface AllowsSpawn {
        Direction direction() default Direction.UP;
        int lightLevelUpperBound() default 15; // LEQ
        int lightLevelLowerBound() default 0; // GEQ
    }
}
