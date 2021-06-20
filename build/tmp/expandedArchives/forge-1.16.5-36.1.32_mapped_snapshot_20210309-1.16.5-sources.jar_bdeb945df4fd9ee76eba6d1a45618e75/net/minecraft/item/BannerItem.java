package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;

public class BannerItem extends WallOrFloorItem {
   public BannerItem(Block banner, Block wallBanner, Item.Properties builder) {
      super(banner, wallBanner, builder);
      Validate.isInstanceOf(AbstractBannerBlock.class, banner);
      Validate.isInstanceOf(AbstractBannerBlock.class, wallBanner);
   }

   @OnlyIn(Dist.CLIENT)
   public static void appendHoverTextFromTileEntityTag(ItemStack stack, List<ITextComponent> tooltip) {
      CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
      if (compoundnbt != null && compoundnbt.contains("Patterns")) {
         ListNBT listnbt = compoundnbt.getList("Patterns", 10);

         for(int i = 0; i < listnbt.size() && i < 6; ++i) {
            CompoundNBT compoundnbt1 = listnbt.getCompound(i);
            DyeColor dyecolor = DyeColor.byId(compoundnbt1.getInt("Color"));
            BannerPattern bannerpattern = BannerPattern.byHash(compoundnbt1.getString("Pattern"));
            if (bannerpattern != null) {
               tooltip.add((new TranslationTextComponent("block.minecraft.banner." + bannerpattern.getFileName() + '.' + dyecolor.getTranslationKey())).mergeStyle(TextFormatting.GRAY));
            }
         }

      }
   }

   public DyeColor getColor() {
      return ((AbstractBannerBlock)this.getBlock()).getColor();
   }

   /**
    * allows items to add custom lines of information to the mouseover description
    */
   @OnlyIn(Dist.CLIENT)
   public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
      appendHoverTextFromTileEntityTag(stack, tooltip);
   }
}