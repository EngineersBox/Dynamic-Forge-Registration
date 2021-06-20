package net.minecraft.client.gui.fonts;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.providers.DefaultGlyphProvider;
import net.minecraft.client.gui.fonts.providers.GlyphProviderTypes;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class FontResourceManager implements AutoCloseable {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final ResourceLocation MISSING_TEXTURE = new ResourceLocation("minecraft", "missing");
   private final Font field_238545_c_;
   private final Map<ResourceLocation, Font> idToFontMap = Maps.newHashMap();
   private final TextureManager textureManager;
   private Map<ResourceLocation, ResourceLocation> unicodeForcedMap = ImmutableMap.of();
   private final IFutureReloadListener reloadListener = new ReloadListener<Map<ResourceLocation, List<IGlyphProvider>>>() {
      /**
       * Performs any reloading that can be done off-thread, such as file IO
       */
      protected Map<ResourceLocation, List<IGlyphProvider>> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
         profilerIn.startTick();
         Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
         Map<ResourceLocation, List<IGlyphProvider>> map = Maps.newHashMap();

         for(ResourceLocation resourcelocation : resourceManagerIn.getAllResourceLocations("font", (p_215274_0_) -> {
            return p_215274_0_.endsWith(".json");
         })) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring("font/".length(), s.length() - ".json".length()));
            List<IGlyphProvider> list = map.computeIfAbsent(resourcelocation1, (p_215272_0_) -> {
               return Lists.newArrayList(new DefaultGlyphProvider());
            });
            profilerIn.startSection(resourcelocation1::toString);

            try {
               for(IResource iresource : resourceManagerIn.getAllResources(resourcelocation)) {
                  profilerIn.startSection(iresource::getPackName);

                  try (
                     InputStream inputstream = iresource.getInputStream();
                     Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                  ) {
                     profilerIn.startSection("reading");
                     JsonArray jsonarray = JSONUtils.getJsonArray(JSONUtils.fromJson(gson, reader, JsonObject.class), "providers");
                     profilerIn.endStartSection("parsing");

                     for(int i = jsonarray.size() - 1; i >= 0; --i) {
                        JsonObject jsonobject = JSONUtils.getJsonObject(jsonarray.get(i), "providers[" + i + "]");

                        try {
                           String s1 = JSONUtils.getString(jsonobject, "type");
                           GlyphProviderTypes glyphprovidertypes = GlyphProviderTypes.byName(s1);
                           profilerIn.startSection(s1);
                           IGlyphProvider iglyphprovider = glyphprovidertypes.getFactory(jsonobject).create(resourceManagerIn);
                           if (iglyphprovider != null) {
                              list.add(iglyphprovider);
                           }

                           profilerIn.endSection();
                        } catch (RuntimeException runtimeexception) {
                           FontResourceManager.LOGGER.warn("Unable to read definition '{}' in fonts.json in resourcepack: '{}': {}", resourcelocation1, iresource.getPackName(), runtimeexception.getMessage());
                        }
                     }

                     profilerIn.endSection();
                  } catch (RuntimeException runtimeexception1) {
                     FontResourceManager.LOGGER.warn("Unable to load font '{}' in fonts.json in resourcepack: '{}': {}", resourcelocation1, iresource.getPackName(), runtimeexception1.getMessage());
                  }

                  profilerIn.endSection();
               }
            } catch (IOException ioexception) {
               FontResourceManager.LOGGER.warn("Unable to load font '{}' in fonts.json: {}", resourcelocation1, ioexception.getMessage());
            }

            profilerIn.startSection("caching");
            IntSet intset = new IntOpenHashSet();

            for(IGlyphProvider iglyphprovider1 : list) {
               intset.addAll(iglyphprovider1.func_230428_a_());
            }

            intset.forEach((int glyphID) -> {
               if (glyphID != 32) {
                  for(IGlyphProvider iglyphprovider2 : Lists.reverse(list)) {
                     if (iglyphprovider2.getGlyphInfo(glyphID) != null) {
                        break;
                     }
                  }

               }
            });
            profilerIn.endSection();
            profilerIn.endSection();
         }

         profilerIn.endTick();
         return map;
      }

      protected void apply(Map<ResourceLocation, List<IGlyphProvider>> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
         profilerIn.startTick();
         profilerIn.startSection("closing");
         FontResourceManager.this.idToFontMap.values().forEach(Font::close);
         FontResourceManager.this.idToFontMap.clear();
         profilerIn.endStartSection("reloading");
         objectIn.forEach((p_238556_1_, p_238556_2_) -> {
            Font font = new Font(FontResourceManager.this.textureManager, p_238556_1_);
            font.setGlyphProviders(Lists.reverse(p_238556_2_));
            FontResourceManager.this.idToFontMap.put(p_238556_1_, font);
         });
         profilerIn.endSection();
         profilerIn.endTick();
      }

      public String getSimpleName() {
         return "FontManager";
      }
   };

   public FontResourceManager(TextureManager p_i49772_1_) {
      this.textureManager = p_i49772_1_;
      this.field_238545_c_ = Util.make(new Font(p_i49772_1_, MISSING_TEXTURE), (p_238550_0_) -> {
         p_238550_0_.setGlyphProviders(Lists.newArrayList(new DefaultGlyphProvider()));
      });
   }

   public void setForcedUnicodeMap(Map<ResourceLocation, ResourceLocation> unicodeForcedMap) {
      this.unicodeForcedMap = unicodeForcedMap;
   }

   public FontRenderer getDefaultFontRenderer() {
      return new FontRenderer((p_238552_1_) -> {
         return this.idToFontMap.getOrDefault(this.unicodeForcedMap.getOrDefault(p_238552_1_, p_238552_1_), this.field_238545_c_);
      });
   }

   public IFutureReloadListener getReloadListener() {
      return this.reloadListener;
   }

   public void close() {
      this.idToFontMap.values().forEach(Font::close);
      this.field_238545_c_.close();
   }
}