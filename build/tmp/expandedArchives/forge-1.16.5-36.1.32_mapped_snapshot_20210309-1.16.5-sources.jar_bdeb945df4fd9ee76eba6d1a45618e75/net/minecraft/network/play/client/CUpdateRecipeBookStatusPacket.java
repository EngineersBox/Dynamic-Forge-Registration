package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CUpdateRecipeBookStatusPacket implements IPacket<IServerPlayNetHandler> {
   private RecipeBookCategory category;
   private boolean visible;
   private boolean filterCraftable;

   public CUpdateRecipeBookStatusPacket() {
   }

   @OnlyIn(Dist.CLIENT)
   public CUpdateRecipeBookStatusPacket(RecipeBookCategory category, boolean visible, boolean filterCraftable) {
      this.category = category;
      this.visible = visible;
      this.filterCraftable = filterCraftable;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.category = buf.readEnumValue(RecipeBookCategory.class);
      this.visible = buf.readBoolean();
      this.filterCraftable = buf.readBoolean();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeEnumValue(this.category);
      buf.writeBoolean(this.visible);
      buf.writeBoolean(this.filterCraftable);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IServerPlayNetHandler handler) {
      handler.func_241831_a(this);
   }

   public RecipeBookCategory getCategory() {
      return this.category;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public boolean shouldFilterCraftable() {
      return this.filterCraftable;
   }
}