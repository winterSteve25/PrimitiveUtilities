package wintersteve25.primutils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class PrimUtilsGEOBlockRendererBase<T extends TileEntity & IAnimatable> extends GeoBlockRenderer<T> {
    public PrimUtilsGEOBlockRendererBase(TileEntityRendererDispatcher rendererDispatcherIn, AnimatedGeoModel<T> modelProvider) {
        super(rendererDispatcherIn, modelProvider);
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(getTextureLocation(animatable));
    }
}
