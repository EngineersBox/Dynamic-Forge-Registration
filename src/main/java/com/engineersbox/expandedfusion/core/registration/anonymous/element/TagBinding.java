package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class TagBinding<T> {

    private ResourceLocation tagResource;
    private ITag.INamedTag<T> tag;
    private ResourceLocation mirroredTagResource;
    private ITag.INamedTag<Item> mirroredTag;

    public TagBinding() {}

    public ResourceLocation getTagResource() {
        return tagResource;
    }

    public void setTagResource(ResourceLocation tagResource) {
        this.tagResource = tagResource;
    }

    public ITag.INamedTag<T> getTag() {
        return tag;
    }

    public void setTag(ITag.INamedTag<T> tag) {
        this.tag = tag;
    }

    public ResourceLocation getMirroredTagResource() {
        return mirroredTagResource;
    }

    public void setMirroredTagResource(ResourceLocation mirroredTagResource) {
        this.mirroredTagResource = mirroredTagResource;
    }

    public ITag.INamedTag<Item> getMirroredTag() {
        return mirroredTag;
    }

    public void setMirroredTag(ITag.INamedTag<Item> mirroredTag) {
        this.mirroredTag = mirroredTag;
    }
}
