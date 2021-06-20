package net.minecraft.world.spawner;

import net.minecraft.world.server.ServerWorld;

public interface ISpecialSpawner {
   int onUpdate(ServerWorld world, boolean spawnHostiles, boolean spawnPassives);
}