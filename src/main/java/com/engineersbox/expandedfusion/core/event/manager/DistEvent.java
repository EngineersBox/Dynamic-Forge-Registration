package com.engineersbox.expandedfusion.core.event.manager;

import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.CommonEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ServerEventHandler;

import java.lang.annotation.Annotation;

public enum DistEvent {
    COMMON(CommonEventHandler.class),
    CLIENT(ClientEventHandler.class),
    SERVER(ServerEventHandler.class),
    DATA(DataEventHandler.class);

    private final Class<? extends Annotation> eventHandlerAnnotation;

    DistEvent(final Class<? extends Annotation> eventHandlerAnnotation) {
        this.eventHandlerAnnotation = eventHandlerAnnotation;
    }

    public Class<? extends Annotation> getEventHandlerAnnotation() {
        return this.eventHandlerAnnotation;
    }
}
