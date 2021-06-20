package net.minecraft.world.gen.feature.structure;

import java.util.Locale;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.TemplateManager;

public interface IStructurePieceType {
   IStructurePieceType MINESHAFT_CORRIDOR = register(MineshaftPieces.Corridor::new, "MSCorridor");
   IStructurePieceType MINESHAFT_CROSSING = register(MineshaftPieces.Cross::new, "MSCrossing");
   IStructurePieceType MINESHAFT_ROOM = register(MineshaftPieces.Room::new, "MSRoom");
   IStructurePieceType MINESHAFT_STAIRS = register(MineshaftPieces.Stairs::new, "MSStairs");
   IStructurePieceType NETHER_FORTRESS_CROSSING_3 = register(FortressPieces.Crossing3::new, "NeBCr");
   IStructurePieceType NETHER_FORTRESS_END = register(FortressPieces.End::new, "NeBEF");
   IStructurePieceType NETHER_FORTRESS_STRAIGHT = register(FortressPieces.Straight::new, "NeBS");
   IStructurePieceType NETHER_FORTRESS_CORRIDOR_3 = register(FortressPieces.Corridor3::new, "NeCCS");
   IStructurePieceType NETHER_FORTRESS_CORRIDOR_4 = register(FortressPieces.Corridor4::new, "NeCTB");
   IStructurePieceType NETHER_FORTRESS_ENTRANCE = register(FortressPieces.Entrance::new, "NeCE");
   IStructurePieceType NETHER_FORTRESS_CROSSING_2 = register(FortressPieces.Crossing2::new, "NeSCSC");
   IStructurePieceType NETHER_FORTRESS_CORRIDOR = register(FortressPieces.Corridor::new, "NeSCLT");
   IStructurePieceType NETHER_FORTRESS_CORRIDOR_5 = register(FortressPieces.Corridor5::new, "NeSC");
   IStructurePieceType NETHER_FORTRESS_CORRIDOR_2 = register(FortressPieces.Corridor2::new, "NeSCRT");
   IStructurePieceType NETHER_FORTRESS_NETHER_STALK_ROOM = register(FortressPieces.NetherStalkRoom::new, "NeCSR");
   IStructurePieceType NETHER_FORTRESS_THRONE = register(FortressPieces.Throne::new, "NeMT");
   IStructurePieceType NETHER_FORTRESS_CROSSING = register(FortressPieces.Crossing::new, "NeRC");
   IStructurePieceType NETHER_FORTRESS_STAIRS = register(FortressPieces.Stairs::new, "NeSR");
   IStructurePieceType NETHER_FORTRESS_START = register(FortressPieces.Start::new, "NeStart");
   IStructurePieceType STRONGHOLD_CHEST_CORRIDOR = register(StrongholdPieces.ChestCorridor::new, "SHCC");
   IStructurePieceType STRONGHOLD_CORRIDOR = register(StrongholdPieces.Corridor::new, "SHFC");
   IStructurePieceType STRONGHOLD_CROSSING = register(StrongholdPieces.Crossing::new, "SH5C");
   IStructurePieceType STRONGHOLD_LEFT_TURN = register(StrongholdPieces.LeftTurn::new, "SHLT");
   IStructurePieceType STRONGHOLD_LIBRARY = register(StrongholdPieces.Library::new, "SHLi");
   IStructurePieceType STRONGHOLD_PORTAL_ROOM = register(StrongholdPieces.PortalRoom::new, "SHPR");
   IStructurePieceType STRONGHOLD_PRISON = register(StrongholdPieces.Prison::new, "SHPH");
   IStructurePieceType STRONGHOLD_RIGHT_TURN = register(StrongholdPieces.RightTurn::new, "SHRT");
   IStructurePieceType STRONGHOLD_ROOM_CROSSING = register(StrongholdPieces.RoomCrossing::new, "SHRC");
   IStructurePieceType STRONGHOLD_STAIRS = register(StrongholdPieces.Stairs::new, "SHSD");
   IStructurePieceType STRONGHOLD_STAIRS_2 = register(StrongholdPieces.Stairs2::new, "SHStart");
   IStructurePieceType STRONGHOLD_STRAIGHT = register(StrongholdPieces.Straight::new, "SHS");
   IStructurePieceType STRONGHOLD_STAIRS_STRAIGHT = register(StrongholdPieces.StairsStraight::new, "SHSSD");
   IStructurePieceType JUNGLE_PYRAMID_PIECE = register(JunglePyramidPiece::new, "TeJP");
   IStructurePieceType OCEAN_RUIN_PIECE = register(OceanRuinPieces.Piece::new, "ORP");
   IStructurePieceType IGLOO_PIECE = register(IglooPieces.Piece::new, "Iglu");
   IStructurePieceType RUINED_PORTAL = register(RuinedPortalPiece::new, "RUPO");
   IStructurePieceType SWAMP_HUT_PIECE = register(SwampHutPiece::new, "TeSH");
   IStructurePieceType DESERT_PYRAMID_PIECE = register(DesertPyramidPiece::new, "TeDP");
   IStructurePieceType OCEAN_MONUMENT_BUILDING = register(OceanMonumentPieces.MonumentBuilding::new, "OMB");
   IStructurePieceType OCEAN_MONUMENT_CORE_ROOM = register(OceanMonumentPieces.MonumentCoreRoom::new, "OMCR");
   IStructurePieceType OCEAN_MONUMENT_DOUBLE_X_ROOM = register(OceanMonumentPieces.DoubleXRoom::new, "OMDXR");
   IStructurePieceType OCEAN_MONUMENT_DOUBLE_XY_ROOM = register(OceanMonumentPieces.DoubleXYRoom::new, "OMDXYR");
   IStructurePieceType OCEAN_MONUMENT_DOUBLE_Y_ROOM = register(OceanMonumentPieces.DoubleYRoom::new, "OMDYR");
   IStructurePieceType OCEAN_MONUMENT_DOUBLE_YZ_ROOM = register(OceanMonumentPieces.DoubleYZRoom::new, "OMDYZR");
   IStructurePieceType OCEAN_MONUMENT_DOUBLE_Z_ROOM = register(OceanMonumentPieces.DoubleZRoom::new, "OMDZR");
   IStructurePieceType OCEAN_MONUMENT_ENTRY_ROOM = register(OceanMonumentPieces.EntryRoom::new, "OMEntry");
   IStructurePieceType OCEAN_MONUMENT_PENTHOUSE = register(OceanMonumentPieces.Penthouse::new, "OMPenthouse");
   IStructurePieceType OCEAN_MONUMENT_SIMPLE_ROOM = register(OceanMonumentPieces.SimpleRoom::new, "OMSimple");
   IStructurePieceType OCEAN_MONUMENT_SIMPLE_TOP_ROOM = register(OceanMonumentPieces.SimpleTopRoom::new, "OMSimpleT");
   IStructurePieceType OCEAN_MONUMENT_WING_ROOM = register(OceanMonumentPieces.WingRoom::new, "OMWR");
   IStructurePieceType END_CITY_TEMPLATE = register(EndCityPieces.CityTemplate::new, "ECP");
   IStructurePieceType WOODLAND_MANSION_TEMPLATE = register(WoodlandMansionPieces.MansionTemplate::new, "WMP");
   IStructurePieceType BURIED_TREASURE_PIECE = register(BuriedTreasure.Piece::new, "BTP");
   IStructurePieceType SHIPWRECK = register(ShipwreckPieces.Piece::new, "Shipwreck");
   IStructurePieceType NETHER_FOSSIL = register(NetherFossilStructures.Piece::new, "NeFos");
   IStructurePieceType field_242786_ad = register(AbstractVillagePiece::new, "jigsaw");

   StructurePiece load(TemplateManager p_load_1_, CompoundNBT p_load_2_);

   static IStructurePieceType register(IStructurePieceType type, String key) {
      return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), type);
   }
}