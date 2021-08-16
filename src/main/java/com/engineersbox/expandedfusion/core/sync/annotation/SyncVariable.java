package com.engineersbox.expandedfusion.core.sync.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks variables that should be automatically synced with the client. Currently, this is used just
 * for tile entities, but could have other uses I guess?
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface SyncVariable {

    String name();

    boolean onRead() default true;

    boolean onWrite() default true;

    boolean onPacket() default true;

    enum Type {
        READ, WRITE, PACKET
    }
}
