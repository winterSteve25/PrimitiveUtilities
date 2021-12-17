package wintersteve25.primutils.common.recipes.basin;

import com.google.common.collect.ImmutableList;
import fictioncraft.wintersteve25.fclib.api.crafting.FluidIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.IDefaultPrimUtilsRecipe;

public interface IBasinRecipe extends IDefaultPrimUtilsRecipe {
    ResourceLocation TYPE_ID = new ResourceLocation(PrimitiveUtils.MODID, "stirring");

    FluidIngredient getInputFluid();

    ImmutableList<ItemStack> getOutput();

    ImmutableList<Ingredient> getInput();

    int getStirAmounts();

    boolean match(ImmutableList<ItemStack> in, FluidStack fluidStack);

    /**
     * default stuff
     */
    @Override
    default ItemStack getIcon() {
        return new ItemStack(PrimUtilsBlocks.BASIN);
    }

    @Override
    default String getGroup() {
        return "stirring";
    }

    @Override
    default IRecipeType<?> getType() {
        return PrimUtilsRecipes.BASIN_RT;
    }
}
