package wintersteve25.primutils.common.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static final ResourceLocation UID = new ResourceLocation(PrimitiveUtils.MODID, "jei_plugin");
    public static final ResourceLocation MILLSTONE_ID = new ResourceLocation(PrimitiveUtils.MODID, "millstone");
    public static final ResourceLocation DRYING_RACK_ID = new ResourceLocation(PrimitiveUtils.MODID, "drying_rack");
    public static final ResourceLocation CHOPPING_BLOCK_ID = new ResourceLocation(PrimitiveUtils.MODID, "chopping_block");
    public static final ResourceLocation BASIN_ID = new ResourceLocation(PrimitiveUtils.MODID, "basin");

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (PrimUtilsConfig.isEnableMillstone()) {
            registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(PrimUtilsRecipes.MILLSTONE_RT), MILLSTONE_ID);
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(PrimUtilsRecipes.DRYING_RACK_RT), DRYING_RACK_ID);
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(PrimUtilsRecipes.CHOPPING_BLOCK_RT), CHOPPING_BLOCK_ID);
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipesForType(PrimUtilsRecipes.BASIN_RT), BASIN_ID);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (PrimUtilsConfig.isEnableMillstone()) {
            registration.addRecipeCatalyst(new ItemStack(PrimUtilsBlocks.MILLSTONE), MILLSTONE_ID);
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            registration.addRecipeCatalyst(new ItemStack(PrimUtilsBlocks.DRYING_RACK), DRYING_RACK_ID);
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            registration.addRecipeCatalyst(new ItemStack(PrimUtilsBlocks.CHOPPING_BLOCK), CHOPPING_BLOCK_ID);
            for (Item item : ForgeRegistries.ITEMS) {
                if (item.getToolTypes(item.getDefaultInstance()).contains(ToolType.AXE)) {
                    registration.addRecipeCatalyst(new ItemStack(item), CHOPPING_BLOCK_ID);
                }
            }
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            registration.addRecipeCatalyst(new ItemStack(PrimUtilsBlocks.BASIN), BASIN_ID);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (PrimUtilsConfig.isEnableMillstone()) {
            registration.addRecipeCategories(new MillstoneCategory(registration.getJeiHelpers().getGuiHelper()));
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            registration.addRecipeCategories(new DryingRackCategory(registration.getJeiHelpers().getGuiHelper()));
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            registration.addRecipeCategories(new ChoppingBlockCategory(registration.getJeiHelpers().getGuiHelper()));
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            registration.addRecipeCategories(new BasinCategory(registration.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
