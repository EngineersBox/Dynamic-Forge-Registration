package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SSpawnParticlePacket implements IPacket<IClientPlayNetHandler> {
   private double xCoord;
   private double yCoord;
   private double zCoord;
   private float xOffset;
   private float yOffset;
   private float zOffset;
   private float particleSpeed;
   private int particleCount;
   private boolean longDistance;
   private IParticleData particle;

   public SSpawnParticlePacket() {
   }

   public <T extends IParticleData> SSpawnParticlePacket(T particle, boolean longDistance, double xCoord, double yCoord, double zCoord, float xOffset, float yOffset, float zOffset, float particleSpeed, int particleCount) {
      this.particle = particle;
      this.longDistance = longDistance;
      this.xCoord = xCoord;
      this.yCoord = yCoord;
      this.zCoord = zCoord;
      this.xOffset = xOffset;
      this.yOffset = yOffset;
      this.zOffset = zOffset;
      this.particleSpeed = particleSpeed;
      this.particleCount = particleCount;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      ParticleType<?> particletype = Registry.PARTICLE_TYPE.getByValue(buf.readInt());
      if (particletype == null) {
         particletype = ParticleTypes.BARRIER;
      }

      this.longDistance = buf.readBoolean();
      this.xCoord = buf.readDouble();
      this.yCoord = buf.readDouble();
      this.zCoord = buf.readDouble();
      this.xOffset = buf.readFloat();
      this.yOffset = buf.readFloat();
      this.zOffset = buf.readFloat();
      this.particleSpeed = buf.readFloat();
      this.particleCount = buf.readInt();
      this.particle = this.readParticle(buf, particletype);
   }

   private <T extends IParticleData> T readParticle(PacketBuffer buffer, ParticleType<T> particleType) {
      return particleType.getDeserializer().read(particleType, buffer);
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeInt(Registry.PARTICLE_TYPE.getId(this.particle.getType()));
      buf.writeBoolean(this.longDistance);
      buf.writeDouble(this.xCoord);
      buf.writeDouble(this.yCoord);
      buf.writeDouble(this.zCoord);
      buf.writeFloat(this.xOffset);
      buf.writeFloat(this.yOffset);
      buf.writeFloat(this.zOffset);
      buf.writeFloat(this.particleSpeed);
      buf.writeInt(this.particleCount);
      this.particle.write(buf);
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isLongDistance() {
      return this.longDistance;
   }

   /**
    * Gets the x coordinate to spawn the particle.
    */
   @OnlyIn(Dist.CLIENT)
   public double getXCoordinate() {
      return this.xCoord;
   }

   /**
    * Gets the y coordinate to spawn the particle.
    */
   @OnlyIn(Dist.CLIENT)
   public double getYCoordinate() {
      return this.yCoord;
   }

   /**
    * Gets the z coordinate to spawn the particle.
    */
   @OnlyIn(Dist.CLIENT)
   public double getZCoordinate() {
      return this.zCoord;
   }

   /**
    * Gets the x coordinate offset for the particle. The particle may use the offset for particle spread.
    */
   @OnlyIn(Dist.CLIENT)
   public float getXOffset() {
      return this.xOffset;
   }

   /**
    * Gets the y coordinate offset for the particle. The particle may use the offset for particle spread.
    */
   @OnlyIn(Dist.CLIENT)
   public float getYOffset() {
      return this.yOffset;
   }

   /**
    * Gets the z coordinate offset for the particle. The particle may use the offset for particle spread.
    */
   @OnlyIn(Dist.CLIENT)
   public float getZOffset() {
      return this.zOffset;
   }

   /**
    * Gets the speed of the particle animation (used in client side rendering).
    */
   @OnlyIn(Dist.CLIENT)
   public float getParticleSpeed() {
      return this.particleSpeed;
   }

   /**
    * Gets the amount of particles to spawn
    */
   @OnlyIn(Dist.CLIENT)
   public int getParticleCount() {
      return this.particleCount;
   }

   @OnlyIn(Dist.CLIENT)
   public IParticleData getParticle() {
      return this.particle;
   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleParticles(this);
   }
}