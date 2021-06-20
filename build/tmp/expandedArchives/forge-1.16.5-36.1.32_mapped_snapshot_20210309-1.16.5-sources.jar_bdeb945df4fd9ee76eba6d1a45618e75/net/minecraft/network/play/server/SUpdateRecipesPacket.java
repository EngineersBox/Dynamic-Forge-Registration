package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SUpdateRecipesPacket implements IPacket<IClientPlayNetHandler> {
   private List<IRecipe<?>> recipes;

   public SUpdateRecipesPacket() {
   }

   public SUpdateRecipesPacket(Collection<IRecipe<?>> recipes) {
      this.recipes = Lists.newArrayList(recipes);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleUpdateRecipes(this);
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.recipes = Lists.newArrayList();
      int i = buf.readVarInt();

      for(int j = 0; j < i; ++j) {
         this.recipes.add(readRecipe(buf));
      }

   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(this.recipes.size());

      for(IRecipe<?> irecipe : this.recipes) {
         writeRecipe(irecipe, buf);
      }

   }

   @OnlyIn(Dist.CLIENT)
   public List<IRecipe<?>> getRecipes() {
      return this.recipes;
   }

   public static IRecipe<?> readRecipe(PacketBuffer buffer) {
      ResourceLocation resourcelocation = buffer.readResourceLocation();
      ResourceLocation resourcelocation1 = buffer.readResourceLocation();
      return Registry.RECIPE_SERIALIZER.getOptional(resourcelocation).orElseThrow(() -> {
         return new IllegalArgumentException("Unknown recipe serializer " + resourcelocation);
      }).read(resourcelocation1, buffer);
   }

   public static <T extends IRecipe<?>> void writeRecipe(T recipe, PacketBuffer buffer) {
      buffer.writeResourceLocation(Registry.RECIPE_SERIALIZER.getKey(recipe.getSerializer()));
      buffer.writeResourceLocation(recipe.getId());
      ((net.minecraft.item.crafting.IRecipeSerializer<T>)recipe.getSerializer()).write(buffer, recipe);
   }
}