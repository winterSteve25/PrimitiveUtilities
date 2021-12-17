package wintersteve25.primutils.common.events;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wintersteve25.primutils.PrimitiveUtils;
import wintersteve25.primutils.client.BasinTESR;
import wintersteve25.primutils.client.ChoppingBlockTESR;
import wintersteve25.primutils.client.DryingRackTESR;
import wintersteve25.primutils.client.MillstoneTESR;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsConfig;

@Mod.EventBusSubscriber(modid = PrimitiveUtils.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void clientPreInit(FMLClientSetupEvent event) {
        //TESRs
        if (ModList.get().isLoaded("geckolib3")) {
            if (PrimUtilsConfig.isEnableMillstone()) {
                ClientRegistry.bindTileEntityRenderer(PrimUtilsBlocks.MILLSTONE_TE.get(), MillstoneTESR::new);
            }
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            ClientRegistry.bindTileEntityRenderer(PrimUtilsBlocks.DRYING_RACK_TE.get(), DryingRackTESR::new);
            RenderTypeLookup.setRenderLayer(PrimUtilsBlocks.DRYING_RACK, RenderType.getTranslucent());
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            ClientRegistry.bindTileEntityRenderer(PrimUtilsBlocks.CHOPPING_BLOCK_TE.get(), ChoppingBlockTESR::new);
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            ClientRegistry.bindTileEntityRenderer(PrimUtilsBlocks.BASIN_TE.get(), BasinTESR::new);
        }
    }
}
