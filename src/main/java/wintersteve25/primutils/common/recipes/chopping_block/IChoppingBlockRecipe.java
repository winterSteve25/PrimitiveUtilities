package wintersteve25.primutils.common.recipes.chopping_block;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.IDefaultPrimUtilsRecipe;

public interface IChoppingBlockRecipe extends IDefaultPrimUtilsRecipe {
    ResourceLocation TYPE_ID = new ResourceLocation(PrimitiveUtils.MODID, "chopping");

    ItemStack getOutput();

    Ingredient getInput();

    int getChopAmounts();

    boolean match(ItemStack in);

    /**
     * default stuff
     */
    @Override
    default ItemStack getIcon() {
        return new ItemStack(PrimUtilsBlocks.CHOPPING_BLOCK);
    }

    @Override
    default String getGroup() {
        return "chopping";
    }

    @Override
    default IRecipeType<?> getType() {
        return PrimUtilsRecipes.CHOPPING_BLOCK_RT;
    }
}
