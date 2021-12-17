package wintersteve25.primutils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import wintersteve25.primutils.common.te.ChoppingBlockTileEntity;

public class ChoppingBlockTESR extends TileEntityRenderer<ChoppingBlockTileEntity> {
    public ChoppingBlockTESR(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(ChoppingBlockTileEntity tile, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
        if (tile.getWorld() != null) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack inputItem = tile.getItemHandler().getStackInSlot(0);
            matrixStack.push();
            setupMatrix(matrixStack, tile.getWorld().getBlockState(tile.getPos()).get(DirectionalBlock.FACING), itemRenderer.getItemModelWithOverrides(inputItem, tile.getWorld(), null).isGui3d());
            itemRenderer.renderItem(inputItem, ItemCameraTransforms.TransformType.GROUND, i, i1, matrixStack, iRenderTypeBuffer);
            matrixStack.pop();
        }
    }

    private void setupMatrix(MatrixStack matrixStackIn, Direction direction, boolean is3D) {
        if (is3D) {
            matrixStackIn.translate(.5, .23D, .5);
        } else {
            matrixStackIn.translate(.5, .34D, .5);
        }
        float f = -direction.getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
        if (!is3D) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(1.2f, 1.2f, 1.2f);
        } else {
            matrixStackIn.scale(1.5f, 1.5f, 1.5f);
        }
    }
}
