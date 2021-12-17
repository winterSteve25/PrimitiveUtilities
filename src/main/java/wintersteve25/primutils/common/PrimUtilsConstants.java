package wintersteve25.primutils.common;

import wintersteve25.primutils.client.PrimUtilsGeoModelBase;
import wintersteve25.primutils.common.contents.BasinItemBlock;
import wintersteve25.primutils.common.contents.MillstoneItemBlock;
import wintersteve25.primutils.common.te.BasinTileEntity;
import wintersteve25.primutils.common.te.MillStoneTileEntity;

public class PrimUtilsConstants {
    public static final class Geo {
        public static final PrimUtilsGeoModelBase<MillStoneTileEntity> MILLSTONE = new PrimUtilsGeoModelBase<>("millstone.geo.json", "millstone_stitched.png", "millstone.animation.json");
        public static final PrimUtilsGeoModelBase<MillstoneItemBlock> MILLSTONE_IB = new PrimUtilsGeoModelBase<>("millstone.geo.json", "millstone_stitched.png", "millstone.animation.json");

        public static final PrimUtilsGeoModelBase<BasinTileEntity> BASIN = new PrimUtilsGeoModelBase<>("basin.geo.json", "basin_stitched.png", "basin.animation.json");
        public static final PrimUtilsGeoModelBase<BasinItemBlock> BASIN_IB = new PrimUtilsGeoModelBase<>("basin.geo.json", "basin_stitched.png", "basin.animation.json");
    }
}
