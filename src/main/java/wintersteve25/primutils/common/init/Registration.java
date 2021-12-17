package wintersteve25.primutils.common.init;

import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.primutils.PrimitiveUtils;

import java.util.function.Supplier;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PrimitiveUtils.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrimitiveUtils.MODID);
    public static final DeferredRegister<TileEntityType<?>> TE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PrimitiveUtils.MODID);

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TE.register(eventBus);

        PrimUtilsBlocks.register();
        registerBlocks();
        eventBus.addGenericListener(IRecipeSerializer.class, PrimUtilsRecipes::registerRecipes);

        PrimitiveUtils.LOGGER.info("Primitive Utils Registration Complete");
    }

    private static void registerBlocks() {
        for (IFCDataGenObject<Block> block : PrimUtilsBlocks.registryList.keySet()) {
            if (PrimUtilsBlocks.registryList.get(block) != null) {
                Helper.register(MiscHelper.langToReg(block.regName()), block::getOg, PrimUtilsBlocks.registryList.get(block));
            } else {
                Helper.register(MiscHelper.langToReg(block.regName()), block::getOg);
            }
        }
    }

    public static class Helper {
        public static <I extends Block> RegistryObject<I> register(String name, Supplier<? extends I> block) {
            RegistryObject<I> registryObject = Registration.BLOCKS.register(name, block);
            Registration.ITEMS.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties().group(PrimitiveUtils.creativeTab)));
            return registryObject;
        }

        public static <B extends Block, I extends Item> RegistryObject<B> register(String name, Supplier<? extends B> block, I blockItem) {
            RegistryObject<B> registryObject = Registration.BLOCKS.register(name, block);
            Registration.ITEMS.register(name, () -> blockItem);
            return registryObject;
        }
    }
}
