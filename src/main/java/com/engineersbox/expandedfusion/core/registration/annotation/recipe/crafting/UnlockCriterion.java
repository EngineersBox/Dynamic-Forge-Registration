package com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.data.recipe.AccessCriterion;

public @interface UnlockCriterion {
    String key();
    String provider();
    AccessCriterion requirement();
}
