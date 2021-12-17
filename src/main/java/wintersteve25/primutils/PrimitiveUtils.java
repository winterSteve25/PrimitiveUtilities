package wintersteve25.primutils;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.init.Registration;

@Mod(PrimitiveUtils.MODID)
public class PrimitiveUtils {
    public static final String MODID = "primutils";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ItemGroup creativeTab = new ItemGroup("primutils") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(PrimUtilsBlocks.MILLSTONE);
        }
    };

    public PrimitiveUtils() {
        PrimUtilsConfig.write();
        PrimUtilsConfig.read();
        Registration.init();

        MinecraftForge.EVENT_BUS.addListener(PrimitiveUtils::reload);
    }

    public static void reload(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener<Void>() {
            @Override
            protected Void prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
                return null;
            }

            @Override
            protected void apply(Void objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
                PrimUtilsConfig.read();
            }
        });
    }
}
