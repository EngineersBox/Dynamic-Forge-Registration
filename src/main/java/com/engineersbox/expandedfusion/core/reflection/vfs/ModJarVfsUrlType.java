package com.engineersbox.expandedfusion.core.reflection.vfs;

import com.engineersbox.expandedfusion.core.reflection.exception.UnsupportedModJarVfsTypeException;
import org.reflections.vfs.Vfs;

import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ModJarVfsUrlType implements Vfs.UrlType {

    private static final String MOD_JAR_PROTOCOL = "modjar";

    public Set<URL> filterOutFromURLs(final Collection<URL> urls) {
        return urls.stream()
                .filter((final URL url) -> !this.matches(url))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean matches(final URL url) {
        return url.getProtocol().equals(MOD_JAR_PROTOCOL);
    }

    @Override
    public Vfs.Dir createDir(URL url) throws UnsupportedModJarVfsTypeException {
        throw new UnsupportedModJarVfsTypeException();
    }
}
