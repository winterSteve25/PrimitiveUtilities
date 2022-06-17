package wintersteve25.primutils.common.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import wintersteve25.primutils.PrimitiveUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class PrimUtilsConfig {

    private static final File file = new File(FMLPaths.CONFIGDIR.get() + "/primutils.json");
    private static boolean enableMillstone;
    private static boolean enableDryingRack;
    private static boolean enableChoppingBlock;
    private static boolean enableBasin;
    private static boolean basinAllowStack;
    private static boolean onlyAcceptMainHandItem;

    public static void read() {
        if (!file.exists()) {
            PrimitiveUtils.LOGGER.warn("Config file not found! Creating a new one..");
            write();
        } else {
            try {
                PrimitiveUtils.LOGGER.info("Attempting to read {}", file.getName());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Config c = gson.fromJson(new FileReader(file), Config.class);
                if (c != null) {
                    enableMillstone = c.isEnableMillstone();
                    enableDryingRack = c.isEnableDryingRack();
                    enableChoppingBlock = c.isEnableChoppingBlock();
                    enableBasin = c.isEnableBasin();
                    basinAllowStack = c.isBasinAllowStack();
                    onlyAcceptMainHandItem = c.isOnlyAcceptMainHandItem();
                }
            } catch (FileNotFoundException e) {
                PrimitiveUtils.LOGGER.warn("Config file not found! Creating a new one..");
                e.printStackTrace();
                write();
            }
        }
    }

    public static void write() {
        if (!file.exists()) {
            PrintWriter writer = JsonUtils.createWriter(file, PrimitiveUtils.LOGGER);
            writer.print(JsonUtils.getGson().toJson(new Config(true, true, true, true, false, onlyAcceptMainHandItem)));
            writer.close();
        }
    }

    public static boolean isEnableMillstone() {
        return enableMillstone;
    }

    public static boolean isEnableDryingRack() {
        return enableDryingRack;
    }

    public static boolean isEnableChoppingBlock() {
        return enableChoppingBlock;
    }

    public static boolean isEnableBasin() {
        return enableBasin;
    }

    public static boolean isBasinAllowStack() {
        return basinAllowStack;
    }

    public static boolean isOnlyAcceptMainHandItem() {
        return onlyAcceptMainHandItem;
    }

    private static class Config {
        private final boolean enableMillstone;
        private final boolean enableDryingRack;
        private final boolean enableChoppingBlock;
        private final boolean enableBasin;
        private final boolean basinAllowStack;
        private final boolean onlyAcceptMainHandItem;

        private Config(boolean enableMillstone, boolean enableDryingRack, boolean enableChoppingBlock, boolean enableBasin, boolean basinAllowStack, boolean onlyAcceptMainHandItem) {
            this.enableMillstone = enableMillstone;
            this.enableDryingRack = enableDryingRack;
            this.enableChoppingBlock = enableChoppingBlock;
            this.enableBasin = enableBasin;
            this.basinAllowStack = basinAllowStack;
            this.onlyAcceptMainHandItem = onlyAcceptMainHandItem;
        }

        public boolean isEnableMillstone() {
            return enableMillstone;
        }

        public boolean isEnableDryingRack() {
            return enableDryingRack;
        }

        public boolean isEnableChoppingBlock() {
            return enableChoppingBlock;
        }

        public boolean isEnableBasin() {
            return enableBasin;
        }

        public boolean isBasinAllowStack() {
            return basinAllowStack;
        }

        public boolean isOnlyAcceptMainHandItem() {
            return onlyAcceptMainHandItem;
        }
    }
}
