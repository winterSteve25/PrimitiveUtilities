package wintersteve25.primutils.common.recipes.basin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fictioncraft.wintersteve25.fclib.api.crafting.FluidIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BasinRecipe implements IBasinRecipe {

    private final ResourceLocation recipeID;
    private final FluidIngredient inputFluid;
    private final ImmutableList<ItemStack> output;
    private final ImmutableList<Ingredient> input;
    private final int stirAmount;

    public BasinRecipe(ResourceLocation recipeID, FluidIngredient inputFluid, List<ItemStack> output, List<Ingredient> input, int stirAmount) {
        this.recipeID = recipeID;
        this.inputFluid = inputFluid;
        this.output = ImmutableList.copyOf(output);
        this.input = ImmutableList.copyOf(input);
        this.stirAmount = stirAmount;
    }

    @Override
    public FluidIngredient getInputFluid() {
        return inputFluid;
    }

    @Override
    public ImmutableList<ItemStack> getOutput() {
        return output;
    }

    @Override
    public ImmutableList<Ingredient> getInput() {
        return input;
    }

    @Override
    public int getStirAmounts() {
        return stirAmount;
    }

    // modified from RecipeElvenTrade class from Botania made by Vazkii and their team.
    @Override
    public boolean match(ImmutableList<ItemStack> in, FluidStack fluidStack) {
        List<Ingredient> inputsMissing = new ArrayList<>(input);
        List<ItemStack> itemsLeft = new ArrayList<>(in);

        if (in.isEmpty()) return false;

        for (ItemStack stack : in) {
            if (!stack.isEmpty()) {
                int stackIndex = -1;

                for (int i = 0; i < inputsMissing.size(); i++) {
                    Ingredient ingredientInput = inputsMissing.get(i);
                    if (ingredientInput.test(stack)) {
                        stackIndex = i;
                        break;
                    }
                }

                if (stackIndex != -1) {
                    inputsMissing.remove(stackIndex);
                    itemsLeft.removeIf((is) -> is.getItem() == stack.getItem());
                }
            }
        }

        return inputsMissing.isEmpty() && inputFluid.test(fluidStack) && itemsLeft.isEmpty();
    }

    @Override
    public ResourceLocation getId() {
        return recipeID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PrimUtilsRecipes.BASIN_RECIPE_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BasinRecipe> {

        @Override
        public BasinRecipe read(ResourceLocation recipeId, JsonObject json) {
            if (!PrimUtilsConfig.isEnableBasin()) return null;
            int stirAmount = JSONUtils.getInt(json, "stirAmounts");
            List<Ingredient> inputs = new ArrayList<>();
            List<ItemStack> outputs = new ArrayList<>();

            for (JsonElement ingEle : JSONUtils.getJsonArray(json, "inputs")) {
                Ingredient ingInput = Ingredient.deserialize(ingEle);
                if (!ingInput.hasNoMatchingItems()) inputs.add(ingInput);
            }
            for (JsonElement ingEle : JSONUtils.getJsonArray(json, "output")) {
                ItemStack stackOutput = ShapedRecipe.deserializeItem(ingEle.getAsJsonObject());
                if (!stackOutput.isEmpty()) outputs.add(stackOutput);
            }
            JsonObject ingObj = JSONUtils.getJsonObject(json, "input_fluid");
            FluidIngredient fluidInput = FluidIngredient.deserialize(ingObj);
            return new BasinRecipe(recipeId, fluidInput, outputs, inputs, stirAmount);
        }

        @Nullable
        @Override
        public BasinRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            if (!PrimUtilsConfig.isEnableBasin()) return null;
            int stirAmount = buffer.readInt();
            Ingredient[] inputs = new Ingredient[buffer.readVarInt()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = Ingredient.read(buffer);
            }
            ItemStack[] outputs = new ItemStack[buffer.readVarInt()];
            for (int i = 0; i < outputs.length; i++) {
                outputs[i] = buffer.readItemStack();
            }

            FluidIngredient fluid = FluidIngredient.read(buffer);

            return new BasinRecipe(recipeId, fluid, Lists.newArrayList(outputs), Lists.newArrayList(inputs), stirAmount);
        }

        @Override
        public void write(PacketBuffer buffer, BasinRecipe recipe) {
            if (!PrimUtilsConfig.isEnableBasin()) return;
            buffer.writeInt(recipe.getStirAmounts());
            buffer.writeVarInt(recipe.getInput().size());
            for (Ingredient ing : recipe.getInput()) {
                ing.write(buffer);
            }
            buffer.writeVarInt(recipe.getOutput().size());
            for (ItemStack is : recipe.getOutput()) {
                buffer.writeItemStack(is);
            }
            recipe.getInputFluid().write(buffer);
        }
    }
}
