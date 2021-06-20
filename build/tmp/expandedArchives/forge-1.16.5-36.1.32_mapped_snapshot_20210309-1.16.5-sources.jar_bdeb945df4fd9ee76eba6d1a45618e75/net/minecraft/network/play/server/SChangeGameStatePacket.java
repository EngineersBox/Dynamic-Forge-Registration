package net.minecraft.network.play.server;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SChangeGameStatePacket implements IPacket<IClientPlayNetHandler> {
   public static final SChangeGameStatePacket.State SPAWN_NOT_VALID = new SChangeGameStatePacket.State(0);
   public static final SChangeGameStatePacket.State RAINING = new SChangeGameStatePacket.State(1);
   public static final SChangeGameStatePacket.State CLEAR = new SChangeGameStatePacket.State(2);
   public static final SChangeGameStatePacket.State CHANGE_GAMETYPE = new SChangeGameStatePacket.State(3);
   public static final SChangeGameStatePacket.State PERFORM_RESPAWN = new SChangeGameStatePacket.State(4);
   public static final SChangeGameStatePacket.State CHANGE_SETTINGS = new SChangeGameStatePacket.State(5);
   public static final SChangeGameStatePacket.State HIT_PLAYER_ARROW = new SChangeGameStatePacket.State(6);
   public static final SChangeGameStatePacket.State SET_RAIN_STRENGTH = new SChangeGameStatePacket.State(7);
   public static final SChangeGameStatePacket.State SET_THUNDER_STRENGTH = new SChangeGameStatePacket.State(8);
   public static final SChangeGameStatePacket.State STING_PLAYER_PUFFERFISH = new SChangeGameStatePacket.State(9);
   public static final SChangeGameStatePacket.State CURSE_PLAYER_ELDER_GUARDIAN = new SChangeGameStatePacket.State(10);
   public static final SChangeGameStatePacket.State SHOW_DEATH_SCREEN = new SChangeGameStatePacket.State(11);
   private SChangeGameStatePacket.State state;
   private float value;

   public SChangeGameStatePacket() {
   }

   public SChangeGameStatePacket(SChangeGameStatePacket.State state, float value) {
      this.state = state;
      this.value = value;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      this.state = SChangeGameStatePacket.State.idStateMap.get(buf.readUnsignedByte());
      this.value = buf.readFloat();
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeByte(this.state.id);
      buf.writeFloat(this.value);
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleChangeGameState(this);
   }

   @OnlyIn(Dist.CLIENT)
   public SChangeGameStatePacket.State getState() {
      return this.state;
   }

   @OnlyIn(Dist.CLIENT)
   public float getValue() {
      return this.value;
   }

   public static class State {
      private static final Int2ObjectMap<SChangeGameStatePacket.State> idStateMap = new Int2ObjectOpenHashMap<>();
      private final int id;

      public State(int id) {
         this.id = id;
         idStateMap.put(id, this);
      }
   }
}