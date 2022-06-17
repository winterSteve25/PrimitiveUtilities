package wintersteve25.primutils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;
import wintersteve25.primutils.common.te.DryingRackTileEntity;

public class DryingRackTESR extends TileEntityRenderer<DryingRackTileEntity> {
    public DryingRackTESR(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DryingRackTileEntity dryingRack, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (dryingRack.getWorld() != null) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack inputItem = dryingRack.getItemHandler().getStackInSlot(0).isEmpty() ? dryingRack.getOutputHandler().getStackInSlot(0) : dryingRack.getItemHandler().getStackInSlot(0);
            ItemStack inputItem2 = dryingRack.getItemHandler().getStackInSlot(1).isEmpty() ? dryingRack.getOutputHandler().getStackInSlot(1) : dryingRack.getItemHandler().getStackInSlot(1);
            ItemStack inputItem3 = dryingRack.getItemHandler().getStackInSlot(2).isEmpty() ? dryingRack.getOutputHandler().getStackInSlot(2) : dryingRack.getItemHandler().getStackInSlot(2);
            ItemStack inputItem4 = dryingRack.getItemHandler().getStackInSlot(3).isEmpty() ? dryingRack.getOutputHandler().getStackInSlot(3) : dryingRack.getItemHandler().getStackInSlot(3);

            BlockState state = dryingRack.getWorld().getBlockState(dryingRack.getPos());
            if (!state.hasProperty(DirectionalBlock.FACING)) return;
            Direction direction = state.get(DirectionalBlock.FACING);

            switch (direction) {
                default:
                    renderItem(matrixStackIn, direction, inputItem, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .3);
                    renderItem(matrixStackIn, direction, inputItem2, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .3);
                    renderItem(matrixStackIn, direction, inputItem3, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .7);
                    renderItem(matrixStackIn, direction, inputItem4, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .7);
                    break;
                case SOUTH:
                    renderItem(matrixStackIn, direction, inputItem, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .7);
                    renderItem(matrixStackIn, direction, inputItem2, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .7);
                    renderItem(matrixStackIn, direction, inputItem3, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .3);
                    renderItem(matrixStackIn, direction, inputItem4, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .3);
                    break;
                case WEST:
                    renderItem(matrixStackIn, direction, inputItem, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .7);
                    renderItem(matrixStackIn, direction, inputItem2, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .3);
                    renderItem(matrixStackIn, direction, inputItem3, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .7);
                    renderItem(matrixStackIn, direction, inputItem4, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .3);
                    break;
                case EAST:
                    renderItem(matrixStackIn, direction, inputItem, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .3);
                    renderItem(matrixStackIn, direction, inputItem2, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .7, .7);
                    renderItem(matrixStackIn, direction, inputItem3, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .3);
                    renderItem(matrixStackIn, direction, inputItem4, itemRenderer, dryingRack.getWorld(), bufferIn, combinedLightIn, combinedOverlayIn, .3, .7);
            }
        }
    }

    private void renderItem(MatrixStack matrixStack, Direction direction, ItemStack item, ItemRenderer renderer, World world, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, double x, double z) {
        if (!item.isEmpty()) {
            matrixStack.push();
            setupMatrix(matrixStack, direction, renderer.getItemModelWithOverrides(item, world, null).isGui3d(), x, z);
            renderer.renderItem(item, ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
            matrixStack.pop();
        }
    }

    private void setupMatrix(MatrixStack matrixStackIn, Direction direction, boolean is3D, double x, double z) {
        if (is3D) {
            matrixStackIn.translate(x, 0.44D, z);
        } else {
            matrixStackIn.translate(x, 0.51D, z);
        }
        float f = -direction.getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
        if (!is3D) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(.7f, .7f, .7f);
        }
    }
}
