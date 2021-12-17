package wintersteve25.primutils.common.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.recipes.basin.BasinRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BasinCategory implements IRecipeCategory<BasinRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    private final LoadingCache<Integer, IDrawableAnimated> arrow;

    public BasinCategory(IGuiHelper iGuiHelper) {
        this.background = iGuiHelper.createDrawable(new ResourceLocation(PrimitiveUtils.MODID, "textures/gui/basin.png"), 0, 0, 205, 90);
        this.icon = iGuiHelper.createDrawableIngredient(new ItemStack(PrimUtilsBlocks.BASIN));
        this.arrow = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer, IDrawableAnimated>() {
            public IDrawableAnimated load(Integer cookTime) {
                return iGuiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.BASIN_ID;
    }

    @Override
    public Class<? extends BasinRecipe> getRecipeClass() {
        return BasinRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("primutils.jei.basin.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(BasinRecipe basinRecipe, IIngredients iIngredients) {
        List<ItemStack> list = new ArrayList<>();
        for (Ingredient ing : basinRecipe.getInput()) {
            list.addAll(Arrays.asList(ing.getMatchingStacks()));
        }
        iIngredients.setInputs(VanillaTypes.ITEM, list);
        iIngredients.setInputs(VanillaTypes.FLUID, Arrays.asList(basinRecipe.getInputFluid().getMatchingStacks()));
        iIngredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(basinRecipe.getOutput()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BasinRecipe basinRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidGroup = iRecipeLayout.getFluidStacks();

        int inputIndex = 0;
        Vector2f position = new Vector2f(24, 0);
        Vector2f center = new Vector2f(24, 37);
        double angleBetweenEach = 360.0 / iIngredients.getInputs(VanillaTypes.ITEM).size();

        for (int i = 0; i < iIngredients.getInputs(VanillaTypes.ITEM).size(); i++) {
            itemStackGroup.init(inputIndex, true, (int) position.x, (int) position.y);
            itemStackGroup.set(inputIndex, Arrays.asList(basinRecipe.getInput().get(i).getMatchingStacks()));
            inputIndex++;
            position = rotatePointAbout(position, center, angleBetweenEach);
        }

        int outputIndex = inputIndex+1;
        int outputX = 90;
        int outputY = 29;

        for (int i = 0; i < iIngredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
            itemStackGroup.init(outputIndex, false, outputX, outputY);
            itemStackGroup.set(outputIndex, basinRecipe.getOutput().get(i));
            outputIndex++;
            outputX+=17;
            if (outputX >= 183) {
                outputX = 98;
                outputY += 17;
            }
        }

        fluidGroup.init(0, true, (int) center.x, (int) center.y);
        fluidGroup.set(0, Arrays.asList(basinRecipe.getInputFluid().getMatchingStacks()));
        fluidGroup.addTooltipCallback(((i, b, fluidStack, list) -> list.add(new TranslationTextComponent("primutils.jei.basin.fluidAmount", fluidStack.getAmount()).mergeStyle(TextFormatting.DARK_GRAY))));
    }

    @Override
    public void draw(BasinRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        try {
            arrow.get(recipe.getStirAmounts()*40).draw(matrixStack, 61, 38);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // modified from PetalApothecaryRecipeCategory class in Botania by Vazkii and their team
    public static Vector2f rotatePointAbout(Vector2f in, Vector2f about, double degrees) {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Vector2f((float) newX, (float) newY);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(BasinRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> list = new ArrayList<>();
        if (mouseX > 60 && mouseX < 85 && mouseY > 35 && mouseY < 50) {
            list.add(new TranslationTextComponent("primutils.jei.basin.stirAmount", recipe.getStirAmounts()));
        }
        return list;
    }
}
