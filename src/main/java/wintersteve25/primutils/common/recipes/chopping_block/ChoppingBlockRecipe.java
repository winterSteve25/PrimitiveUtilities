package wintersteve25.primutils.common.recipes.chopping_block;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;

import javax.annotation.Nullable;

public class ChoppingBlockRecipe implements IChoppingBlockRecipe {
    private final ResourceLocation recipeID;
    private final Ingredient input;
    private final ItemStack output;
    private final int chopAmounts;

    public ChoppingBlockRecipe(ResourceLocation recipeID, Ingredient input, ItemStack output, int chopAmounts) {
        this.recipeID = recipeID;
        this.input = input;
        this.output = output;
        this.chopAmounts = chopAmounts;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
    public int getChopAmounts() {
        return chopAmounts;
    }

    @Override
    public boolean match(ItemStack in) {
        return input.test(in);
    }

    @Override
    public ResourceLocation getId() {
        return recipeID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PrimUtilsRecipes.CHOPPING_BLOCK_RECIPE_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChoppingBlockRecipe> {
        @Override
        public ChoppingBlockRecipe read(ResourceLocation recipeId, JsonObject json) {
            if (!PrimUtilsConfig.isEnableChoppingBlock()) return null;
            int chopAmounts = JSONUtils.getInt(json, "chopAmounts");
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json.get("output"), "output"));

            return new ChoppingBlockRecipe(recipeId, input, output, chopAmounts);
        }

        @Nullable
        @Override
        public ChoppingBlockRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            if (!PrimUtilsConfig.isEnableChoppingBlock()) return null;
            Ingredient input = Ingredient.read(buffer);
            int chopAmounts = buffer.readInt();
            ItemStack output = buffer.readItemStack();
            return new ChoppingBlockRecipe(recipeId, input, output, chopAmounts);
        }

        @Override
        public void write(PacketBuffer buffer, ChoppingBlockRecipe recipe) {
            if (!PrimUtilsConfig.isEnableChoppingBlock()) return;
            recipe.getInput().write(buffer);
            buffer.writeInt(recipe.getChopAmounts());
            buffer.writeItemStack(recipe.getOutput());
        }

        @Override
        public String toString() {
            return "chopping";
        }
    }
}
