package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementAnnotationConstructor;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementDynamicClassGenerator;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Map;

public abstract class BaseFluidBuilder<E, F> extends AnonymousElementBuilder<E, F> {

    public <T> T createLangMappedFluid(final Class<T> baseClass,
                                       final String providerName,
                                       final Map<LangKey, String> langMapping,
                                       final ForgeFlowingFluid.Properties props) {
        checkPackageNameInitialised();
        return new ElementDynamicClassGenerator<>(baseClass)
                .retrieveDeclaredConstructor(ForgeFlowingFluid.Properties.class)
                .withAnnotation(ElementAnnotationConstructor.createLangMetadata(langMapping))
                .withAnnotation(ElementAnnotationConstructor.createFluidProvider(providerName))
                .createUnloadedClass(String.format(
                        DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                        PACKAGE_NAME,
                        providerName,
                        ForgeFlowingFluid.Source.class.isAssignableFrom(baseClass) ? DYNAMIC_SOURCE_FLUID_CLASS_SUFFIX : DYNAMIC_FLOWING_FLUID_CLASS_SUFFIX
                ))
                .loadClass()
                .getInstance(props);
    }

}
