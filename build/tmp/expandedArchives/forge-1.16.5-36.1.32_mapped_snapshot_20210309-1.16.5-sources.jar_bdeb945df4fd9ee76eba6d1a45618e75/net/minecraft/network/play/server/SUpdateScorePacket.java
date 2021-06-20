package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SUpdateScorePacket implements IPacket<IClientPlayNetHandler> {
   private String name = "";
   @Nullable
   private String objective;
   private int value;
   private ServerScoreboard.Action action;

   public SUpdateScorePacket() {
   }

   public SUpdateScorePacket(ServerScoreboard.Action action, @Nullable String objective, String name, int value) {
      if (action != ServerScoreboard.Action.REMOVE && objective == null) {
         throw new IllegalArgumentException("Need an objective name");
      } else {
         this.name = name;
         this.objective = objective;
         this.value = value;
         this.action = action;
      }
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.name = buf.readString(40);
      this.action = buf.readEnumValue(ServerScoreboard.Action.class);
      String s = buf.readString(16);
      this.objective = Objects.equals(s, "") ? null : s;
      if (this.action != ServerScoreboard.Action.REMOVE) {
         this.value = buf.readVarInt();
      }

   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeString(this.name);
      buf.writeEnumValue(this.action);
      buf.writeString(this.objective == null ? "" : this.objective);
      if (this.action != ServerScoreboard.Action.REMOVE) {
         buf.writeVarInt(this.value);
      }

   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleUpdateScore(this);
   }

   @OnlyIn(Dist.CLIENT)
   public String getPlayerName() {
      return this.name;
   }

   @Nullable
   @OnlyIn(Dist.CLIENT)
   public String getObjectiveName() {
      return this.objective;
   }

   @OnlyIn(Dist.CLIENT)
   public int getScoreValue() {
      return this.value;
   }

   @OnlyIn(Dist.CLIENT)
   public ServerScoreboard.Action getAction() {
      return this.action;
   }
}