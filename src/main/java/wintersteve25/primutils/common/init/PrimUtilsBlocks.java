package wintersteve25.primutils.common.init;

import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.VoxelShapeHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import wintersteve25.primutils.common.base.PrimUtilsInteractableBlock;
import wintersteve25.primutils.common.base.PrimUtilsToolTipItemBlock;
import wintersteve25.primutils.common.contents.BasinItemBlock;
import wintersteve25.primutils.common.contents.MillstoneItemBlock;
import wintersteve25.primutils.common.te.ChoppingBlockTileEntity;
import wintersteve25.primutils.common.te.BasinTileEntity;
import wintersteve25.primutils.common.te.DryingRackTileEntity;
import wintersteve25.primutils.common.te.MillStoneTileEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PrimUtilsBlocks {

    private static final String MILLSTONE_ID = "millstone";
    private static final String DRYING_RACK_ID = "drying_rack";
    private static final String CHOPPING_BLOCK_ID = "chopping_block";
    private static final String BASIN_ID = "basin";

    private static final VoxelShape MILLSTONE_SHAPE = Stream.of(
            Block.makeCuboidShape(13, 0, 3, 14, 3, 13),
            Block.makeCuboidShape(2, 0, 3, 3, 3, 13),
            Block.makeCuboidShape(14, 0, 4, 15, 3, 12),
            Block.makeCuboidShape(1, 0, 4, 2, 3, 12),
            Block.makeCuboidShape(2, 3, 4, 3, 4, 12),
            Block.makeCuboidShape(13, 3, 4, 14, 4, 12),
            Block.makeCuboidShape(3, 3, 3, 13, 4, 13),
            Block.makeCuboidShape(2, 4, 3, 3, 7, 13),
            Block.makeCuboidShape(1, 4, 4, 2, 7, 12),
            Block.makeCuboidShape(14, 4, 4, 15, 7, 12),
            Block.makeCuboidShape(13, 4, 3, 14, 7, 13),
            Block.makeCuboidShape(3, 4, 2, 13, 7, 14),
            Block.makeCuboidShape(3, 0, 2, 13, 3, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape DRYING_RACK_SHAPE = Stream.of(
            Block.makeCuboidShape(2, 0, 2, 3, 8, 3),
            Block.makeCuboidShape(13, 0, 2, 14, 8, 3),
            Block.makeCuboidShape(2, 0, 13, 3, 8, 14),
            Block.makeCuboidShape(13, 0, 13, 14, 8, 14),
            Block.makeCuboidShape(1, 7, 1, 2, 8, 15),
            Block.makeCuboidShape(14, 7, 1, 15, 8, 15),
            Block.makeCuboidShape(2, 7, 1, 14, 8, 2),
            Block.makeCuboidShape(2, 7, 14, 14, 8, 15),
            Block.makeCuboidShape(3, 7, 2, 13, 8, 14),
            Block.makeCuboidShape(2, 7, 3, 3, 8, 13),
            Block.makeCuboidShape(13, 7, 3, 14, 8, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape BASIN_SHAPE = Stream.of(
            Block.makeCuboidShape(3, 4, 3, 13, 5, 13),
            Block.makeCuboidShape(13, 5, 3, 14, 9, 13),
            Block.makeCuboidShape(2, 5, 3, 3, 9, 13),
            Block.makeCuboidShape(3, 5, 13, 13, 9, 14),
            Block.makeCuboidShape(3, 5, 2, 13, 9, 3),
            Block.makeCuboidShape(1, 8, 1, 15, 11, 2),
            Block.makeCuboidShape(1, 8, 14, 15, 11, 15),
            Block.makeCuboidShape(14, 8, 2, 15, 11, 14),
            Block.makeCuboidShape(1, 8, 2, 2, 11, 14),
            Block.makeCuboidShape(13, 0, 13, 14, 9, 14),
            Block.makeCuboidShape(13, 0, 14, 14, 1, 15),
            Block.makeCuboidShape(14, 0, 13, 15, 1, 14),
            Block.makeCuboidShape(2, 0, 13, 3, 9, 14),
            Block.makeCuboidShape(2, 0, 14, 3, 1, 15),
            Block.makeCuboidShape(1, 0, 13, 2, 1, 14),
            Block.makeCuboidShape(13, 0, 2, 14, 9, 3),
            Block.makeCuboidShape(13, 0, 1, 14, 1, 2),
            Block.makeCuboidShape(14, 0, 2, 15, 1, 3),
            Block.makeCuboidShape(2, 0, 2, 3, 9, 3),
            Block.makeCuboidShape(2, 0, 1, 3, 1, 2),
            Block.makeCuboidShape(1, 0, 2, 2, 1, 3)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public static final PrimUtilsInteractableBlock MILLSTONE = new PrimUtilsInteractableBlock(MILLSTONE_ID) {
        @Override
        public TileEntityType<?> getTileType() {
            return MILLSTONE_TE.get();
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
            switch (state.get(DirectionalBlock.FACING)) {
                case SOUTH:
                    return VoxelShapeHelper.rotate(MILLSTONE_SHAPE, Rotation.CLOCKWISE_180);
                case EAST:
                    return VoxelShapeHelper.rotate(MILLSTONE_SHAPE, Rotation.CLOCKWISE_90);
                case WEST:
                    return VoxelShapeHelper.rotate(MILLSTONE_SHAPE, Rotation.COUNTERCLOCKWISE_90);
                default:
                    return MILLSTONE_SHAPE;
            }
        }

        @Override
        public BlockRenderType getRenderType(BlockState state) {
            return BlockRenderType.ENTITYBLOCK_ANIMATED;
        }
    };
    public static final RegistryObject<TileEntityType<MillStoneTileEntity>> MILLSTONE_TE = Registration.TE.register(MiscHelper.langToReg(MILLSTONE_ID), () -> TileEntityType.Builder.create(MillStoneTileEntity::new, MILLSTONE).build(null));

    public static final PrimUtilsInteractableBlock DRYING_RACK = new PrimUtilsInteractableBlock(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2.f, 1.4f).setOpaque((state, world, pos) -> false).setBlocksVision((state, world, pos) -> false), DRYING_RACK_ID) {
        @Override
        public TileEntityType<?> getTileType() {
            return DRYING_RACK_TE.get();
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
            switch (state.get(DirectionalBlock.FACING)) {
                case SOUTH:
                    return VoxelShapeHelper.rotate(DRYING_RACK_SHAPE, Rotation.CLOCKWISE_180);
                case EAST:
                    return VoxelShapeHelper.rotate(DRYING_RACK_SHAPE, Rotation.CLOCKWISE_90);
                case WEST:
                    return VoxelShapeHelper.rotate(DRYING_RACK_SHAPE, Rotation.COUNTERCLOCKWISE_90);
                default:
                    return DRYING_RACK_SHAPE;
            }
        }

        @Override
        public boolean isTransparent(BlockState state) {
            return true;
        }
    };
    public static final RegistryObject<TileEntityType<DryingRackTileEntity>> DRYING_RACK_TE = Registration.TE.register(MiscHelper.langToReg(DRYING_RACK_ID), () -> TileEntityType.Builder.create(DryingRackTileEntity::new, DRYING_RACK).build(null));

    public static final PrimUtilsInteractableBlock CHOPPING_BLOCK = new PrimUtilsInteractableBlock(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2.f, 0.8f), CHOPPING_BLOCK_ID) {
        @Override
        public TileEntityType<?> getTileType() {
            return CHOPPING_BLOCK_TE.get();
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
            return Block.makeCuboidShape(0, 0, 0, 16, 5, 16);
        }
    };
    public static final RegistryObject<TileEntityType<ChoppingBlockTileEntity>> CHOPPING_BLOCK_TE = Registration.TE.register(MiscHelper.langToReg(CHOPPING_BLOCK_ID), () -> TileEntityType.Builder.create(ChoppingBlockTileEntity::new, CHOPPING_BLOCK).build(null));

    public static final PrimUtilsInteractableBlock BASIN = new PrimUtilsInteractableBlock(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2.f, 0.8f).setOpaque((var, var1, var2)->false), BASIN_ID) {
        @Override
        public TileEntityType<?> getTileType() {
            return BASIN_TE.get();
        }

        @Override
        public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
            return BASIN_SHAPE;
        }

        @Override
        public BlockRenderType getRenderType(BlockState state) {
            return BlockRenderType.ENTITYBLOCK_ANIMATED;
        }
    };
    public static final RegistryObject<TileEntityType<BasinTileEntity>> BASIN_TE = Registration.TE.register(MiscHelper.langToReg(BASIN_ID), () -> TileEntityType.Builder.create(BasinTileEntity::new, BASIN).build(null));

    public static Map<IFCDataGenObject<Block>, Item> registryList = new HashMap<>();

    public static void register() {
        if (PrimUtilsConfig.isEnableMillstone()) {
            MILLSTONE.init(registryList, new MillstoneItemBlock());
        }
        if (PrimUtilsConfig.isEnableDryingRack()) {
            DRYING_RACK.init(registryList, new PrimUtilsToolTipItemBlock(DRYING_RACK, new Item.Properties(), new TranslationTextComponent("primutils.tooltip.drying_rack")));
        }
        if (PrimUtilsConfig.isEnableChoppingBlock()) {
            CHOPPING_BLOCK.init(registryList, new PrimUtilsToolTipItemBlock(CHOPPING_BLOCK, new Item.Properties(), new TranslationTextComponent("primutils.tooltip.chopping_block")));
        }
        if (PrimUtilsConfig.isEnableBasin()) {
            BASIN.init(registryList, new BasinItemBlock());
        }
    }
}
