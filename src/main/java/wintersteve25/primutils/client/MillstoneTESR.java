package wintersteve25.primutils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import wintersteve25.primutils.common.PrimUtilsConstants;
import wintersteve25.primutils.common.te.MillStoneTileEntity;

public class MillstoneTESR extends PrimUtilsGEOBlockRendererBase<MillStoneTileEntity> {
    public MillstoneTESR(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, PrimUtilsConstants.Geo.MILLSTONE);
    }

    @Override
    public void render(TileEntity tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(tile, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        if (tile instanceof MillStoneTileEntity && tile.getWorld() != null) {
            MillStoneTileEntity millstone = (MillStoneTileEntity) tile;
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack inputItem = millstone.getItemHandler().getStackInSlot(0);
            if (!inputItem.isEmpty()) {

                BlockState state = tile.getWorld().getBlockState(tile.getPos());
                if (!state.hasProperty(DirectionalBlock.FACING)) return;
                Direction direction = state.get(DirectionalBlock.FACING);

                matrixStackIn.push();
                inputMatrix(matrixStackIn, direction, itemRenderer.getItemModelWithOverrides(inputItem, tile.getWorld(), null).isGui3d());
                itemRenderer.renderItem(inputItem, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                matrixStackIn.pop();
            }
        }
    }

    private void inputMatrix(MatrixStack matrixStack, Direction direction, boolean isBlock) {
        float height = isBlock ? 0.4f : 0.46f;

        switch (direction) {
            case SOUTH:
                matrixStack.translate(0.7f, height, 0.5f);
                if (!isBlock) {
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
                } else {
                    matrixStack.rotate(Vector3f.YP.rotation(90));
                }
                break;
            case EAST:
                matrixStack.translate(0.5f, height, 0.3f);
                matrixStack.rotate(Vector3f.YP.rotation(90));
                if (!isBlock) {
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
                }
                break;
            case WEST:
                matrixStack.translate(0.5f, height, 0.7f);
                matrixStack.rotate(Vector3f.YP.rotation(-45));
                if (!isBlock) {
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
                }
                break;
            default:
                matrixStack.translate(0.3f, height, 0.5f);
                if (!isBlock) {
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
                } else {
                    matrixStack.rotate(Vector3f.YP.rotation(-90));
                }
                break;
        }
        matrixStack.scale(0.6f, 0.6f, 0.6f);
    }
}
