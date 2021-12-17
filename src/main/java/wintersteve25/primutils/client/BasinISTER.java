package wintersteve25.primutils.client;

import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import wintersteve25.primutils.common.PrimUtilsConstants;
import wintersteve25.primutils.common.contents.BasinItemBlock;

public class BasinISTER extends GeoItemRenderer<BasinItemBlock> {
    public BasinISTER() {
        super(PrimUtilsConstants.Geo.BASIN_IB);
    }
}