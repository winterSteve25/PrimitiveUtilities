package wintersteve25.primutils.common.recipes.millstone;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
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
import java.util.HashMap;
import java.util.Map;

public class MillstoneRecipe implements IMillstoneRecipe {

    private final ResourceLocation recipeID;
    private final Ingredient input;
    private final int totalProgress;
    private final ImmutableMap<ItemStack, Byte> outputMap;

    public MillstoneRecipe(ResourceLocation recipeID, Ingredient input, int totalProgress, Map<ItemStack, Byte> outputMap) {
        this.recipeID = recipeID;
        this.input = input;
        this.totalProgress = totalProgress;
        this.outputMap = ImmutableMap.copyOf(outputMap);
    }

    @Override
    public boolean match(ItemStack stack) {
        return input.test(stack);
    }

    @Override
    public ImmutableMap<ItemStack, Byte> getOutput() {
        return outputMap;
    }

    @Override
    public int getTotalProgress() {
        return totalProgress;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }

    @Override
    public ResourceLocation getId() {
        return recipeID;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return PrimUtilsRecipes.MILLSTONE_RECIPE_SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MillstoneRecipe> {
        @Override
        public MillstoneRecipe read(ResourceLocation recipeId, JsonObject json) {
            if (!PrimUtilsConfig.isEnableMillstone()) return null;
            int totalProgress = JSONUtils.getInt(json, "total_progress");
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
            Map<ItemStack, Byte> output = new HashMap<>();

            for (JsonElement outputEle : JSONUtils.getJsonArray(json, "output")) {
                ItemStack outputStack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(outputEle.getAsJsonObject().get("result"), "result"));
                byte chance = JSONUtils.getByte(outputEle.getAsJsonObject().get("chance"), "chance");
                output.put(outputStack, chance);
            }

            return new MillstoneRecipe(recipeId, input, totalProgress, output);
        }

        @Nullable
        @Override
        public MillstoneRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            if (!PrimUtilsConfig.isEnableMillstone()) return null;
            Ingredient input = Ingredient.read(buffer);
            int totalProgress = buffer.readInt();
            ItemStack[] outputs = new ItemStack[buffer.readVarInt()];
            for (int i = 0; i < outputs.length; i++) {
                outputs[i] = buffer.readItemStack();
            }

            Map<ItemStack, Byte> output = new HashMap<>();

            byte[] chances = new byte[buffer.readVarInt()];
            for (int i = 0; i < outputs.length; i++) {
                chances[i] = buffer.readByte();
                output.put(outputs[i], chances[i]);
            }

            return new MillstoneRecipe(recipeId, input, totalProgress, output);
        }

        @Override
        public void write(PacketBuffer buffer, MillstoneRecipe recipe) {
            if (!PrimUtilsConfig.isEnableMillstone()) return;
            recipe.input.write(buffer);
            buffer.writeInt(recipe.totalProgress);
            buffer.writeVarInt(recipe.outputMap.size());
            for (ItemStack output : recipe.outputMap.keySet()) {
                buffer.writeItemStack(output);
            }
            buffer.writeVarInt(recipe.outputMap.size());
            for (byte chance : recipe.outputMap.values()) {
                buffer.writeByte(chance);
            }
        }

        @Override
        public String toString() {
            return "milling";
        }
    }
}
