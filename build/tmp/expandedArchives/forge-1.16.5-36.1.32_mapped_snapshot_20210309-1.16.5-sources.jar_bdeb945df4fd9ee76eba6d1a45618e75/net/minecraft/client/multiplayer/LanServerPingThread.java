package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class LanServerPingThread extends Thread {
   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
   private static final Logger LOGGER = LogManager.getLogger();
   private final String motd;
   private final DatagramSocket socket;
   private boolean isStopping = true;
   private final String address;

   public LanServerPingThread(String motd, String address) throws IOException {
      super("LanServerPinger #" + UNIQUE_THREAD_ID.incrementAndGet());
      this.motd = motd;
      this.address = address;
      this.setDaemon(true);
      this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
      this.socket = new DatagramSocket();
   }

   public void run() {
      String s = getPingResponse(this.motd, this.address);
      byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

      while(!this.isInterrupted() && this.isStopping) {
         try {
            InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
            DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
            this.socket.send(datagrampacket);
         } catch (IOException ioexception) {
            LOGGER.warn("LanServerPinger: {}", (Object)ioexception.getMessage());
            break;
         }

         try {
            sleep(1500L);
         } catch (InterruptedException interruptedexception) {
         }
      }

   }

   public void interrupt() {
      super.interrupt();
      this.isStopping = false;
   }

   public static String getPingResponse(String motdMessage, String adMessage) {
      return "[MOTD]" + motdMessage + "[/MOTD][AD]" + adMessage + "[/AD]";
   }

   public static String getMotdFromPingResponse(String pingResponse) {
      int i = pingResponse.indexOf("[MOTD]");
      if (i < 0) {
         return "missing no";
      } else {
         int j = pingResponse.indexOf("[/MOTD]", i + "[MOTD]".length());
         return j < i ? "missing no" : pingResponse.substring(i + "[MOTD]".length(), j);
      }
   }

   public static String getAdFromPingResponse(String pingResponse) {
      int i = pingResponse.indexOf("[/MOTD]");
      if (i < 0) {
         return null;
      } else {
         int j = pingResponse.indexOf("[/MOTD]", i + "[/MOTD]".length());
         if (j >= 0) {
            return null;
         } else {
            int k = pingResponse.indexOf("[AD]", i + "[/MOTD]".length());
            if (k < 0) {
               return null;
            } else {
               int l = pingResponse.indexOf("[/AD]", k + "[AD]".length());
               return l < k ? null : pingResponse.substring(k + "[AD]".length(), l);
            }
         }
      }
   }
}