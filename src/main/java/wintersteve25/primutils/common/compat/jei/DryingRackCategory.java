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
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.recipes.drying_rack.DryingRackRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DryingRackCategory implements IRecipeCategory<DryingRackRecipe> {

    private final IDrawable dryingRack = new IDrawable() {
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
            RenderingHelper.renderItemModelIntoGUI(new ItemStack(PrimUtilsBlocks.DRYING_RACK), i, i1, 32.0F, 32.0F, 32.0F);
            RenderSystem.disableBlend();
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popMatrix();
        }
    };
    private final IDrawable background;
    private final IDrawable tabIcon;

    public DryingRackCategory(IGuiHelper iGuiHelper) {
        this.background = iGuiHelper.createDrawable(new ResourceLocation(PrimitiveUtils.MODID, "textures/gui/drying_rack.png"), 0, 0, 120, 45);
        this.tabIcon = iGuiHelper.createDrawableIngredient(new ItemStack(PrimUtilsBlocks.DRYING_RACK));
    }

    @Override
    public ResourceLocation getUid() {
        return JEIPlugin.DRYING_RACK_ID;
    }

    @Override
    public Class<? extends DryingRackRecipe> getRecipeClass() {
        return DryingRackRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("primutils.jei.drying_rack.title");
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
    public void setIngredients(DryingRackRecipe dryingRackRecipe, IIngredients iIngredients) {
        iIngredients.setInputs(VanillaTypes.ITEM, Arrays.asList(dryingRackRecipe.getInput().getMatchingStacks()));
        iIngredients.setOutput(VanillaTypes.ITEM, dryingRackRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, DryingRackRecipe dryingRackRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 0, 12);
        itemStackGroup.set(0, Arrays.asList(dryingRackRecipe.getInput().getMatchingStacks()));

        itemStackGroup.init(1, false, 98, 12);
        itemStackGroup.set(1, dryingRackRecipe.getOutput());
    }

    @Override
    public List<ITextComponent> getTooltipStrings(DryingRackRecipe recipe, double mouseX, double mouseY) {
        List<ITextComponent> list = new ArrayList<>();

        if (mouseX > 28 && mouseX < 80 && mouseY > 4 && mouseY < 30) {
            list.add(new TranslationTextComponent("primutils.jei.drying_rack.duration", recipe.getDuration()));
            return list;
        }
        return list;
    }

    @Override
    public void draw(DryingRackRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        dryingRack.draw(matrixStack, 50, 14);
    }
}
