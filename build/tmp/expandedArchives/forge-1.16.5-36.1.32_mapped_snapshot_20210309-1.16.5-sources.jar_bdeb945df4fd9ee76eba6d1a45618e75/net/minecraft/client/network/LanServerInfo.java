package net.minecraft.client.network;

import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LanServerInfo {
   private final String lanServerMotd;
   private final String lanServerIpPort;
   private long timeLastSeen;

   public LanServerInfo(String lanServerMotd, String lanServerIpPort) {
      this.lanServerMotd = lanServerMotd;
      this.lanServerIpPort = lanServerIpPort;
      this.timeLastSeen = Util.milliTime();
   }

   public String getServerMotd() {
      return this.lanServerMotd;
   }

   public String getServerIpPort() {
      return this.lanServerIpPort;
   }

   /**
    * Updates the time this LanServer was last seen.
    */
   public void updateLastSeen() {
      this.timeLastSeen = Util.milliTime();
   }
}