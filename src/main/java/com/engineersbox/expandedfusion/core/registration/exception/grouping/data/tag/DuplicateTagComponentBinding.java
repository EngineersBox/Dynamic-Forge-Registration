package com.engineersbox.expandedfusion.core.registration.exception.grouping.data.tag;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import net.minecraft.tags.ITag;

public class DuplicateTagComponentBinding extends DuplicateComponentBinding {

    public DuplicateTagComponentBinding(final ITag.INamedTag<?> current,
                                        final ITag.INamedTag<?> duplicate) {
        super("tag", ITag.INamedTag.class.getName(), formatTagName(current), formatTagName(duplicate));
    }

    private static String formatTagName(final ITag.INamedTag<?> tag) {
        return String.format(
                "%s/%s",
                tag.getName().getNamespace(),
                tag.getName().getPath()
        );
    }
}
