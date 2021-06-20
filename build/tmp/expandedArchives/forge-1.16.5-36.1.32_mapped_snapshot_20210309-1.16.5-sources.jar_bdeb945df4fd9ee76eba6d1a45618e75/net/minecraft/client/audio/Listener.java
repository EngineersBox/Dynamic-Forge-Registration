package net.minecraft.client.audio;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.openal.AL10;

@OnlyIn(Dist.CLIENT)
public class Listener {
   private float volume = 1.0F;
   private Vector3d clientLocation = Vector3d.ZERO;

   public void setPosition(Vector3d pos) {
      this.clientLocation = pos;
      AL10.alListener3f(4100, (float)pos.x, (float)pos.y, (float)pos.z);
   }

   public Vector3d getClientLocation() {
      return this.clientLocation;
   }

   public void setOrientation(Vector3f clientViewVector, Vector3f viewVectorRaised) {
      AL10.alListenerfv(4111, new float[]{clientViewVector.getX(), clientViewVector.getY(), clientViewVector.getZ(), viewVectorRaised.getX(), viewVectorRaised.getY(), viewVectorRaised.getZ()});
   }

   public void setVolume(float gainIn) {
      AL10.alListenerf(4106, gainIn);
      this.volume = gainIn;
   }

   public float getVolume() {
      return this.volume;
   }

   public void init() {
      this.setPosition(Vector3d.ZERO);
      this.setOrientation(Vector3f.ZN, Vector3f.YP);
   }
}