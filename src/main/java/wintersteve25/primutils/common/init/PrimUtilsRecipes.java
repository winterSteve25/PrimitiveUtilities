package wintersteve25.primutils.common.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import wintersteve25.primutils.common.recipes.basin.BasinRecipe;
import wintersteve25.primutils.common.recipes.basin.IBasinRecipe;
import wintersteve25.primutils.common.recipes.chopping_block.ChoppingBlockRecipe;
import wintersteve25.primutils.common.recipes.chopping_block.IChoppingBlockRecipe;
import wintersteve25.primutils.common.recipes.drying_rack.DryingRackRecipe;
import wintersteve25.primutils.common.recipes.drying_rack.IDryingRackRecipe;
import wintersteve25.primutils.common.recipes.millstone.IMillstoneRecipe;
import wintersteve25.primutils.common.recipes.millstone.MillstoneRecipe;

public class PrimUtilsRecipes {

    public static final IRecipeType<IMillstoneRecipe> MILLSTONE_RT = new DefaultRecipeType<>();
    public static final IRecipeSerializer<MillstoneRecipe> MILLSTONE_RECIPE_SERIALIZER = new MillstoneRecipe.Serializer();
    public static final IRecipeType<IDryingRackRecipe> DRYING_RACK_RT = new DefaultRecipeType<>();
    public static final IRecipeSerializer<DryingRackRecipe> DRYING_RACK_RECIPE_SERIALIZER = new DryingRackRecipe.Serializer();
    public static final IRecipeType<IChoppingBlockRecipe> CHOPPING_BLOCK_RT = new DefaultRecipeType<>();
    public static final IRecipeSerializer<ChoppingBlockRecipe> CHOPPING_BLOCK_RECIPE_SERIALIZER = new ChoppingBlockRecipe.Serializer();
    public static final IRecipeType<IBasinRecipe> BASIN_RT = new DefaultRecipeType<>();
    public static final IRecipeSerializer<BasinRecipe> BASIN_RECIPE_SERIALIZER = new BasinRecipe.Serializer();

    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        if (PrimUtilsConfig.isEnableMillstone()) {
            Registry.register(Registry.RECIPE_TYPE, IMillstoneRecipe.TYPE_ID, MILLSTONE_RT);
            event.getRegistry().register(MILLSTONE_RECIPE_SERIALIZER.setRegistryName(IMillstoneRecipe.TYPE_ID));
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            Registry.register(Registry.RECIPE_TYPE, IDryingRackRecipe.TYPE_ID, DRYING_RACK_RT);
            event.getRegistry().register(DRYING_RACK_RECIPE_SERIALIZER.setRegistryName(IDryingRackRecipe.TYPE_ID));
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            Registry.register(Registry.RECIPE_TYPE, IChoppingBlockRecipe.TYPE_ID, CHOPPING_BLOCK_RT);
            event.getRegistry().register(CHOPPING_BLOCK_RECIPE_SERIALIZER.setRegistryName(IChoppingBlockRecipe.TYPE_ID));
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            Registry.register(Registry.RECIPE_TYPE, IBasinRecipe.TYPE_ID, BASIN_RT);
            event.getRegistry().register(BASIN_RECIPE_SERIALIZER.setRegistryName(IBasinRecipe.TYPE_ID));
        }
    }

    public static class DefaultRecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
    }
}
