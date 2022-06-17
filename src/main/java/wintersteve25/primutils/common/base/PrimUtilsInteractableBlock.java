package wintersteve25.primutils.common.base;

import fictioncraft.wintersteve25.fclib.common.base.FCLibDirectionalBlock;
import fictioncraft.wintersteve25.fclib.common.helper.ISHandlerHelper;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.te.DryingRackTileEntity;

import javax.annotation.Nullable;

public abstract class PrimUtilsInteractableBlock extends FCLibDirectionalBlock {
    public PrimUtilsInteractableBlock(String regName) {
        super(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.f, 1.5f).setRequiresTool().setOpaque((state, world, pos)->false), regName);
    }

    public PrimUtilsInteractableBlock(Properties properties, String regName) {
        super(properties, regName);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PrimUtilsTE && tileEntity.getWorld() != null && !tileEntity.getWorld().isRemote()) {
            ItemStack stack;

            if (PrimUtilsConfig.isOnlyAcceptMainHandItem()) {
                stack = player.getHeldItemMainhand();
            } else {
                stack = player.getHeldItem(handIn);
            }

            if (tileEntity instanceof PrimUtilsFluidTE) {
                PrimUtilsFluidTE fTe = (PrimUtilsFluidTE) tileEntity;
                IFluidHandler handler = fTe.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace()).orElse(null);
                if (FluidUtil.getFluidHandler(player.getHeldItem(handIn)).isPresent()) {
                    FluidUtil.interactWithFluidHandler(player, handIn, handler);
                    return ActionResultType.SUCCESS;
                }
            }

            PrimUtilsTE te = (PrimUtilsTE) tileEntity;
            if (!stack.isEmpty() && te.addItem(player, stack, hit)) {
                return ActionResultType.SUCCESS;
            }

            if (player.isSneaking() && te.removeItem(player)) {
                return ActionResultType.SUCCESS;
            }

            te.onUse(state, worldIn, pos, player, handIn, hit);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof PrimUtilsTE){
            PrimUtilsTE te = (PrimUtilsTE) tile;
            if (te.hasItem()) {
                ISHandlerHelper.dropInventory(te, world, state, pos, te.getInvSize());
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        return this.getDefaultState().with(FACING, blockItemUseContext.getPlacementHorizontalFacing());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public abstract TileEntityType<?> getTileType();

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return getTileType().create();
    }
}
