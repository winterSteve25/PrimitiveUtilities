package wintersteve25.primutils.common.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fictioncraft.wintersteve25.fclib.client.rendering.RenderingHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.config.Constants;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.recipes.chopping_block.ChoppingBlockRecipe;
import wintersteve25.primutils.common.recipes.drying_rack.DryingRackRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoppingBlockCategory implements IRecipeCategory<ChoppingBlockRecipe> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable choppingBlock = new IDrawable() {
        @Override
        public int getWidth() {
            return 1;
        }

        @Override
        public int getHeight() {
            return 1;
        }

        @Override
        public void draw(MatrixStack matrixStack, int i, int i1) {
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            RenderingHelper.renderItemModelIntoGUI(new ItemStack(PrimUtilsBlocks.CHOPPING_BLOCK), i, i1, 64.0F, 64.0F, 64.0F);
            RenderSystem.disableBlend();
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popMatrix();
        }
    };

    public ChoppingBlockCategory(IGuiHelper iGuiHelper) {
        this.background = iGuiHelper.createBlankDrawable(120, 50);
        this.icon = iGuiHelper.createDrawableIngredient(new ItemStack(PrimUtilsBlocks.CHOPPING_BLOCK));
        this.arrow = iGuiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(5*20, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.CHOPPING_BLOCK_ID;
    }

    @Override
    public Class<? extends ChoppingBlockRecipe> getRecipeClass() {
        return ChoppingBlockRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("primutils.jei.chopping_block.title");
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
    public void setIngredients(ChoppingBlockRecipe choppingBlockRecipe, IIngredients iIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(choppingBlockRecipe.getInput().getMatchingStacks()));
        iIngredients.setOutput(VanillaTypes.ITEM, choppingBlockRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, ChoppingBlockRecipe choppingBlockRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 23, 9);
        itemStackGroup.set(0, Arrays.asList(choppingBlockRecipe.getInput().getMatchingStacks()));

        itemStackGroup.init(1, false, 92, 12);
        itemStackGroup.set(1, choppingBlockRecipe.getOutput());
    }

    @Override
    public void draw(ChoppingBlockRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        choppingBlock.draw(matrixStack, 25, 16);
        arrow.draw(matrixStack, 63, 13);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(ChoppingBlockRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> list = new ArrayList<>();

        if (mouseX > 63 && mouseX < 87   && mouseY > 10 && mouseY < 30) {
            list.add(new TranslationTextComponent("primutils.jei.chopping_block.duration", recipe.getChopAmounts()));
            return list;
        }
        return list;
    }
}
