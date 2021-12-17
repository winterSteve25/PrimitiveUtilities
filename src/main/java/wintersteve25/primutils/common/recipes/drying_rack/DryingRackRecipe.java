package wintersteve25.primutils.common.recipes.drying_rack;

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

public class DryingRackRecipe implements IDryingRackRecipe {

    private final ResourceLocation recipeID;
    private final Ingredient input;
    private final ItemStack output;
    private final int duration;

    public DryingRackRecipe(ResourceLocation recipeID, Ingredient input, ItemStack output, int duration) {
        this.recipeID = recipeID;
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    @Override
    public boolean match(ItemStack in) {
        return input.test(in);
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
    public int getDuration() {
        return duration;
    }

    @Override
    public ResourceLocation getId() {
        return recipeID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PrimUtilsRecipes.DRYING_RACK_RECIPE_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DryingRackRecipe> {
        @Override
        public DryingRackRecipe read(ResourceLocation recipeId, JsonObject json) {
            if (!PrimUtilsConfig.isEnableDryingRack()) return null;
            int duration = JSONUtils.getInt(json, "duration");
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json.get("output"), "output"));

            return new DryingRackRecipe(recipeId, input, output, duration);
        }

        @Nullable
        @Override
        public DryingRackRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            if (!PrimUtilsConfig.isEnableDryingRack()) return null;
            Ingredient input = Ingredient.read(buffer);
            int duration = buffer.readInt();
            ItemStack output = buffer.readItemStack();
            return new DryingRackRecipe(recipeId, input, output, duration);
        }

        @Override
        public void write(PacketBuffer buffer, DryingRackRecipe recipe) {
            if (!PrimUtilsConfig.isEnableDryingRack()) return;
            recipe.getInput().write(buffer);
            buffer.writeInt(recipe.getDuration());
            buffer.writeItemStack(recipe.getOutput());
        }

        @Override
        public String toString() {
            return "drying";
        }
    }
}
