package com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.data.recipe.AccessCriterion;

public @interface UnlockCriterion {
    String key();
    String ingredient();
    AccessCriterion requirement();
}
