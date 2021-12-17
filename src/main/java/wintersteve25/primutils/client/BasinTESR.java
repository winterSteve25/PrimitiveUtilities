package wintersteve25.primutils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import wintersteve25.primutils.common.PrimUtilsConstants;
import wintersteve25.primutils.common.te.BasinTileEntity;

public class BasinTESR extends PrimUtilsGEOBlockRendererBase<BasinTileEntity> {
    public BasinTESR(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, PrimUtilsConstants.Geo.BASIN);
    }

    @Override
    public void render(TileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        if (tile instanceof BasinTileEntity && tile.getWorld() != null) {
            BasinTileEntity basin = (BasinTileEntity) tile;
            FluidStack fluid = basin.getTank().getFluid();

            if (!fluid.isEmpty()) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture());
                IVertexBuilder builder = bufferIn.getBuffer(RenderType.getSolid());

                int color = fluid.getFluid().getAttributes().getColor();

                float a = 1.0F;
                float r = (color >> 16 & 0xFF) / 255.0F;
                float g = (color >> 8 & 0xFF) / 255.0F;
                float b = (color & 0xFF) / 255.0F;

                float height = ((0.55f - 0.34f)/4000)*fluid.getAmount();
                matrixStackIn.push();
                add(builder, matrixStackIn, 1 - .82f, .34f+height, .82f, sprite.getMinU(), sprite.getMaxV(), r, g, b, a);
                add(builder, matrixStackIn, .82f, .34f+height, .82f, sprite.getMaxU(), sprite.getMaxV(), r, g, b, a);
                add(builder, matrixStackIn, .82f, .34f+height, .18f, sprite.getMaxU(), sprite.getMinV(), r, g, b, a);
                add(builder, matrixStackIn, 1 - .82f, .34f+height, .18f, sprite.getMinU(), sprite.getMinV(), r, g, b, a);
                matrixStackIn.pop();
            }

            if (basin.hasItem()) {
                if (basin.getWorld() == null) return;
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                Direction direction = basin.getWorld().getBlockState(basin.getPos()).get(DirectionalBlock.FACING);
                for(int i = 0; i < basin.getInvSize(); i++) {
                    ItemStack stack = basin.getItemHandler().getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        int index = i+1;
                        boolean even = index%2==0;

                        matrixStackIn.push();
                        matrixStackIn.translate(0.5 + (!even ? index == 1 ? 0.2 : -0.2 : 0), 0.43, 0.5 + (even ? index == 2 ? 0.2 : -0.2 : 0));
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(index*90));
                        matrixStackIn.rotate(Vector3f.XP.rotation(index*2));
                        matrixStackIn.rotate(Vector3f.ZP.rotation(index*2));
                        matrixStackIn.scale(0.7f, 0.7f, 0.7f);
                        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
                        matrixStackIn.pop();
                    }
                }
            }
        }
    }

    private static void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
}
