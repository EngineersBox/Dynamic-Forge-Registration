package net.minecraft.network.play.server;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SStatisticsPacket implements IPacket<IClientPlayNetHandler> {
   private Object2IntMap<Stat<?>> statisticMap;

   public SStatisticsPacket() {
   }

   public SStatisticsPacket(Object2IntMap<Stat<?>> statisticsMap) {
      this.statisticMap = statisticsMap;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleStatistics(this);
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      int i = buf.readVarInt();
      this.statisticMap = new Object2IntOpenHashMap<>(i);

      for(int j = 0; j < i; ++j) {
         this.readValues(Registry.STATS.getByValue(buf.readVarInt()), buf);
      }

   }

   private <T> void readValues(StatType<T> statType, PacketBuffer buffer) {
      int i = buffer.readVarInt();
      int j = buffer.readVarInt();
      this.statisticMap.put(statType.get(statType.getRegistry().getByValue(i)), j);
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeVarInt(this.statisticMap.size());

      for(Entry<Stat<?>> entry : this.statisticMap.object2IntEntrySet()) {
         Stat<?> stat = entry.getKey();
         buf.writeVarInt(Registry.STATS.getId(stat.getType()));
         buf.writeVarInt(this.func_197683_a(stat));
         buf.writeVarInt(entry.getIntValue());
      }

   }

   private <T> int func_197683_a(Stat<T> stat) {
      return stat.getType().getRegistry().getId(stat.getValue());
   }

   @OnlyIn(Dist.CLIENT)
   public Map<Stat<?>, Integer> getStatisticMap() {
      return this.statisticMap;
   }
}