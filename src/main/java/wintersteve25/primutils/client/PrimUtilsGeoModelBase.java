package wintersteve25.primutils.client;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import wintersteve25.primutils.PrimitiveUtils;

public class PrimUtilsGeoModelBase<T extends IAnimatable> extends AnimatedGeoModel<T> {
    private final ResourceLocation rl1;
    private final ResourceLocation rl2;
    private final ResourceLocation rl3;

    public PrimUtilsGeoModelBase(String pathModel, String pathTexture, String pathAnimation) {
        this.rl1 = new ResourceLocation(PrimitiveUtils.MODID, "geo/" + pathModel);
        this.rl2 = new ResourceLocation(PrimitiveUtils.MODID, "textures/block/" + pathTexture);
        this.rl3 = new ResourceLocation(PrimitiveUtils.MODID, "animations/" + pathAnimation);
    }

    @Override
    public ResourceLocation getModelLocation(T t) {
        return rl1;
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return rl2;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(T t) {
        return rl3;
    }
}
