package wintersteve25.primutils.common.recipes.millstone;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.IDefaultPrimUtilsRecipe;

public interface IMillstoneRecipe extends IDefaultPrimUtilsRecipe {
    ResourceLocation TYPE_ID = new ResourceLocation(PrimitiveUtils.MODID, "milling");

    boolean match(ItemStack stack);

    /**
     * @return Spits out a map of output items evaluating the input item with change of producing the output
     */
    ImmutableMap<ItemStack, Byte> getOutput();

    int getTotalProgress();

    Ingredient getInput();

    /**
     * default stuff
     */
    @Override
    default ItemStack getIcon() {
        return new ItemStack(PrimUtilsBlocks.MILLSTONE);
    }

    @Override
    default String getGroup() {
        return "milling";
    }

    @Override
    default IRecipeType<?> getType() {
        return PrimUtilsRecipes.MILLSTONE_RT;
    }
}
