package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.UninitialisedRuntimeClassPackageNameException;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public abstract class AnonymousElementBuilder<T, E> implements AnonymousBuilder<T, E> {

    public static final String UNINITIALISED_PACKAGE_NAME = "UNINITIALISED";

    @Inject
    @Named("packageName")
    public static String PACKAGE_NAME = UNINITIALISED_PACKAGE_NAME;
    public static final String DYNAMIC_BLOCK_CLASS_SUFFIX = "_DYN_BLOCK";
    public static final String DYNAMIC_ITEM_CLASS_SUFFIX = "_DYN_ITEM";
    public static final String DYNAMIC_SOURCE_FLUID_CLASS_SUFFIX = "_DYN_SOURCE_FLUID";
    public static final String DYNAMIC_FLOWING_FLUID_CLASS_SUFFIX = "_DYN_FLOWING_FLUID";
    public static final String DYNAMIC_CLASS_IDENTIFIER_FORMAT = "%s.%s%s";

    public void checkPackageNameInitialised() {
        if (PACKAGE_NAME.equals(UNINITIALISED_PACKAGE_NAME)) {
            throw new UninitialisedRuntimeClassPackageNameException("Package name for class generation was not instantiated via static injection. Did the context change?");
        }
    }

    public abstract AnonymousElement.Builder construct();

}
