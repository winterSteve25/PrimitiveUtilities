package wintersteve25.primutils.common.compat.jei;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fictioncraft.wintersteve25.fclib.client.rendering.RenderingHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.recipes.millstone.MillstoneRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MillstoneCategory implements IRecipeCategory<MillstoneRecipe> {

    private final IDrawable millstone = new IDrawable() {
        @Override
        public int getWidth() {
            return 48;
        }

        @Override
        public int getHeight() {
            return 40;
        }

        @Override
        public void draw(MatrixStack matrixStack, int i, int i1) {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            RenderingHelper.renderItemModelIntoGUI(new ItemStack(PrimUtilsBlocks.MILLSTONE), i, i1, 64.0F, 64.0F, 64.0F);
            RenderSystem.disableBlend();
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popMatrix();
        }
    };
    private final IDrawable background;
    private final IDrawable tabIcon;

    public MillstoneCategory(IGuiHelper iGuiHelper) {
        this.background = iGuiHelper.createDrawable(new ResourceLocation(PrimitiveUtils.MODID, "textures/gui/millstone.png"), 0, 0, 205, 80);
        this.tabIcon = iGuiHelper.createDrawableIngredient(new ItemStack(PrimUtilsBlocks.MILLSTONE));
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.MILLSTONE_ID;
    }

    @Override
    public Class<? extends MillstoneRecipe> getRecipeClass() {
        return MillstoneRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("primutils.jei.millstone.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return tabIcon;
    }

    @Override
    public void setIngredients(MillstoneRecipe millstoneRecipe, IIngredients iIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(millstoneRecipe.getInput().getMatchingStacks()));
        iIngredients.setOutputs(VanillaTypes.ITEM, ImmutableList.copyOf(millstoneRecipe.getOutput().keySet()));
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, MillstoneRecipe millstoneRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();
        itemStackGroup.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (input) return;
            byte chance = millstoneRecipe.getOutput().get(ingredient);
            tooltip.add(new TranslationTextComponent("primutils.jei.millstone.chance_tooltip", chance).mergeStyle(TextFormatting.DARK_GRAY));
        });

        itemStackGroup.init(0, true, 0, 5);
        itemStackGroup.set(0, Arrays.asList(millstoneRecipe.getInput().getMatchingStacks()));

        int index = 1;
        int x = 98;
        int y = 27;

        for (int i = 0; i < iIngredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
            itemStackGroup.init(index, false, x, y);
            itemStackGroup.set(index, millstoneRecipe.getOutput().keySet().asList().get(i));
            index++;
            x+=17;
            if (x >= 183) {
                x = 98;
                y += 17;
            }
        }
    }

    @Override
    public List<ITextComponent> getTooltipStrings(MillstoneRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> list = new ArrayList<>();

        if (mouseX > 2 && mouseX < 75 && mouseY > 2 && mouseY < 78) {
            list.add(new TranslationTextComponent("primutils.jei.millstone.useAmount", recipe.getTotalProgress()));
            return list;
        }
        return list;
    }

    @Override
    public void draw(MillstoneRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        millstone.draw(matrixStack, 30, 35);
    }
}
