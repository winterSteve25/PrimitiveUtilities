package wintersteve25.primutils.common.recipes.drying_rack;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.IDefaultPrimUtilsRecipe;

public interface IDryingRackRecipe extends IDefaultPrimUtilsRecipe {
    ResourceLocation TYPE_ID = new ResourceLocation(PrimitiveUtils.MODID, "drying");

    boolean match(ItemStack input);

    ItemStack getOutput();

    Ingredient getInput();

    int getDuration();

    /**
     * default stuff
     */
    @Override
    default ItemStack getIcon() {
        return new ItemStack(PrimUtilsBlocks.DRYING_RACK);
    }

    @Override
    default String getGroup() {
        return "drying";
    }

    @Override
    default IRecipeType<?> getType() {
        return PrimUtilsRecipes.DRYING_RACK_RT;
    }
}
