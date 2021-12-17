package wintersteve25.primutils.common.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IDefaultPrimUtilsRecipe extends IRecipe<IInventory> {
    /**
     * default stuff
     */
    @Override
    default NonNullList<ItemStack> getRemainingItems(IInventory inv) {
        return NonNullList.create();
    }

    @Override
    default ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean isDynamic() {
        return true;
    }

    @Override
    default boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

}
