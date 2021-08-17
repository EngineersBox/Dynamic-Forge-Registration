package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LangFileResourceHandler {

    private static final Logger LOGGER = LogManager.getLogger(LangFileResourceHandler.class);

    private static final String RESOURCE_OUTPUT_PROPERTY = "langmetadata.resource_out";
    private static final String LANG_FILE_NAME = "en_us.json";

    private final String outputDirectory;
    private final Map<String, String> langMappings;

    public LangFileResourceHandler() {
        this.outputDirectory = this.getOutputDirectory();
        this.langMappings = this.readFileToMapOrCreateNewMap();
    }

    private String getOutputDirectory() {
        final String outputPath = System.getProperty(RESOURCE_OUTPUT_PROPERTY);
        if (outputPath == null) {
            throw new RuntimeException(String.format(
                    "No output directory was provided, please provide it via the \"-D%s=<DIR>\" argument",
                    RESOURCE_OUTPUT_PROPERTY
            )); // TODO: Implement an exception for this
        }
        return outputPath;
    }

    @SuppressWarnings("unchecked")
    private Map<String,String> readFileToMapOrCreateNewMap() {
        Map<String,String> langFileMap;
        try {
            final Gson gson = new Gson();
            LOGGER.info(String.format("Attempting to read from %s", getFormattedFilePath()));
            final Reader reader = Files.newBufferedReader(Paths.get(getFormattedFilePath()));
            langFileMap = (Map<String,String>) gson.fromJson(reader, Map.class);
            reader.close();
        } catch (final IOException e) {
            LOGGER.info(String.format(
                    "Provided output directory %s did not contain existing %s file, a new file will be created",
                    this.outputDirectory,
                    LANG_FILE_NAME
            ));
            langFileMap = new HashMap<>();
        }
        return langFileMap;
    }

    public void addLangEntryIfNotExists(@NonNull final String providerNamespace,
                                        @NonNull final String humanReadable) {
        final String associatedHumanReadable = this.langMappings.get(providerNamespace);
        if (associatedHumanReadable != null) {
            LOGGER.info(String.format(
                    "Mapping for %s already exists: %s, skipping",
                    providerNamespace,
                    associatedHumanReadable
            ));
            return;
        }
        this.langMappings.put(providerNamespace, humanReadable);
        LOGGER.info(String.format(
                "Added mapping for: \"%s\" -> \"%s\"",
                providerNamespace,
                humanReadable
        ));
    }

    public void exportMappingsToFile() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String jsonContent = gson.toJson(this.langMappings);

        LOGGER.info(String.format("Writing JSON to %s", getFormattedFilePath()));
        try(BufferedWriter br = new BufferedWriter(new FileWriter(getFormattedFilePath()))) {
            br.write(jsonContent);
        } catch (final IOException e) {
            throw new RuntimeException(String.format(
                    "Could not write JSON content to file: %s%s",
                    this.outputDirectory,
                    LANG_FILE_NAME
            ), e); // TODO: Implement exception for this
        }
    }

    private String getFormattedFilePath() {
        return String.format(
                "%s/%s",
                this.outputDirectory,
                LANG_FILE_NAME
        );
    }
}