package com.engineersbox.expandedfusion.core.registration.annotation.processors;

public class LangMetadataProcessor {

    /*
     * TODO: Implement a class to generate the en_us.json file
     * it will need to handle checking if one exists already
     * and amending it with write-back. Otherwise creating a
     * new one and filling it with relevant entries.
     *
     * When creating entries it should be of the form:
     * "<TYPE>.<MOD ID>.<PROVIDER NAME>": "<NAME MAPPING>"
     *
     * The mod ID can be retrieved with System.getProperty("lang.metadata.mod.ID")
     * where the Mod ID is provided as JVM args: -Xlang.metadata.mod.ID
     *
     * It could also be inferred from an @Mod annotation from the Mod.value() property
     */

    public static final String MOD_ID_SYSTEM_PROPERTY = "lang.metadata.mod.ID";

}
