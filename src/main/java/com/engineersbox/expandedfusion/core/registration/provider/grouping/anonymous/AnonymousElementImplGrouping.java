package com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.transformer.BindingGroupTransformer;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.AnonymousElementRetrievalException;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.anonymous.DuplicateAnonymousElementProviderBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class AnonymousElementImplGrouping implements ImplGrouping {

    private Class<?> registrant;

    public void setRegistrant(final Class<?> registrant) {
        if (this.registrant != null) {
            throw new DuplicateAnonymousElementProviderBinding(this.registrant, registrant);
        }
        this.registrant = registrant;
    }

    public List<AnonymousElement> getRegistrantElements() {
        if (this.registrant == null) {
            throw new AnonymousElementRetrievalException("Registrant was not present, class is either synthetic or altered at runtime");
        }
        final BindingGroupTransformer groupTransformer = new BindingGroupTransformer(this.registrant);
        return Enum.class.isAssignableFrom(this.registrant)
                ? groupTransformer.getEnumRegistrantElements()
                : groupTransformer.getStaticFieldRegistrantElements();
    }

    @Override

    public List<Class<?>> getAllClasses() {
        return ImmutableList.of(this.registrant);
    }

    @Override
    public <E> E getCommonIdentifier() {
        return null;
    }
}
