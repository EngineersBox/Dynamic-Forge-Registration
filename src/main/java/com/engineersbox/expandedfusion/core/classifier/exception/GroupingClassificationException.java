package com.engineersbox.expandedfusion.core.classifier.exception;

public class GroupingClassificationException extends RuntimeException {

    public GroupingClassificationException(final String headline, final String message) {
        super(String.format(
                "%s%n%s",
                headline,
                message
        ));
    }

}
