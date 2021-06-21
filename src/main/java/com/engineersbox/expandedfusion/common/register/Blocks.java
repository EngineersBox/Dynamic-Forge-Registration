package com.engineersbox.expandedfusion.common.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Blocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExpandedFusion.MODID);

    // Mineral and metal solid blocks
    public static final RegistryObject<Block> NOBIUM_BLOCK = BLOCKS.register("nobium_block", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> TITANIUM_BLOCK = BLOCKS.register("titanium_block", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> TIN_BLOCK = BLOCKS.register("tin_block", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> BERYLLIUM_BLOCK = BLOCKS.register("beryllium_block", () -> new Block(Block.Properties.create(Material.IRON)));

    // Composite Machine Structure Blocks
    public static final RegistryObject<Block> NOBIUM_TITANIUM_COIL = BLOCKS.register("nobium_titanium_coil", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> NOBIUM_TIN_COIL = BLOCKS.register("nobium_tin_coil", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> COPPER_BERYLLIUM_BONDED_SHIELDING = BLOCKS.register("copper_beryllium_bonded_shielding", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> DIVERTOR_CASSETTE_ASSEMBLY = BLOCKS.register("divertor_cassette_assembly", () -> new Block(Block.Properties.create(Material.IRON)));
    public static final RegistryObject<Block> CRYOSTAT_PRESSURE_CHAMBER_WALL = BLOCKS.register("cryostat_pressure_chamber_wall", () -> new Block(Block.Properties.create(Material.IRON)));
}