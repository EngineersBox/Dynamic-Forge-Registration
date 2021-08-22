package com.engineersbox.expandedfusion.core.data.annotation.recipe.crafting;

import com.engineersbox.expandedfusion.core.data.recipe.AccessCriterion;

public @interface UnlockCriterion {
    String key();
    String provider();
    AccessCriterion requirement();
}