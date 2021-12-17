package wintersteve25.primutils.common.te;

import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasValidItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import wintersteve25.primutils.common.base.PrimUtilsTE;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.chopping_block.IChoppingBlockRecipe;

import java.util.List;
import java.util.function.BiPredicate;

public class ChoppingBlockTileEntity extends PrimUtilsTE implements IHasProgress, IHasValidItems {

    private int totalProgress = -1;
    private int progress = totalProgress;

    public ChoppingBlockTileEntity() {
        super(PrimUtilsBlocks.CHOPPING_BLOCK_TE.get());
    }

    @Override
    public int getInvSize() {
        return 1;
    }

    @Override
    public void onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack axe = player.getHeldItem(handIn);
        if (axe.getItem().getToolTypes(axe).contains(ToolType.AXE) && !player.getCooldownTracker().hasCooldown(axe.getItem())) {
            if (checkRecipe()) {
                progress--;
                axe.damageItem(1, player, (player1)->{});
                player.getCooldownTracker().setCooldown(axe.getItem(), 15);
                if (!getWorld().isRemote()) {
                    ItemStack input = itemHandler.getStackInSlot(0);
                    if (input.getItem() instanceof BlockItem) {
                        ((ServerWorld) getWorld()).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, ((BlockItem)input.getItem()).getBlock().getDefaultState()), pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 4, 0, 0, 0, 0.2d);
                    } else {
                        ((ServerWorld) getWorld()).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, input), pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 4, 0, 0, 0, 0.2d);
                    }
                }
                if (progress == 0) {
                    spawnOutput(getRecipe());
                    itemHandler.extractItem(0, 1, false);
                    totalProgress = -1;
                    progress = totalProgress;
                }
            }
        }
    }

    @Override
    public boolean addItem(PlayerEntity player, ItemStack heldItem, BlockRayTraceResult hit) {
        for (int i = 0; i < getInvSize(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                ItemStack itemAdd = heldItem.copy();
                itemAdd.setCount(1);
                itemHandler.insertItem(i, itemAdd, false);
                if(player != null) {
                    heldItem.shrink(1);
                }
                updateBlock();
                return true;
            }
        }

        return false;
    }

    private void spawnOutput(IChoppingBlockRecipe recipe) {
        if (getWorld() != null && !getWorld().isRemote() && recipe != null) {
            getWorld().playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1f);
            InventoryHelper.spawnItemStack(getWorld(), pos.getX(), (double) pos.getY() + 0.5, pos.getZ(), recipe.getOutput().copy());
        }
    }

    private IChoppingBlockRecipe getRecipe() {
        ItemStack input = itemHandler.getStackInSlot(0);
        if (input.isEmpty()) return null;
        if (getWorld() == null) return null;
        List<IChoppingBlockRecipe> recipes = getWorld().getRecipeManager().getRecipesForType(PrimUtilsRecipes.CHOPPING_BLOCK_RT);
        if (recipes.isEmpty()) return null;
        for (IChoppingBlockRecipe recipe : recipes) {
            if (recipe.match(input)) {
                return recipe;
            }
        }
        return null;
    }

    private boolean checkRecipe() {
        IChoppingBlockRecipe recipe = getRecipe();
        if (recipe != null && progress == -1) {
            totalProgress = recipe.getChopAmounts();
            progress = totalProgress;
            return true;
        } else if (recipe == null) {
            totalProgress = -1;
            progress = totalProgress;
            return false;
        }
        return true;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int i) {
        this.progress = i;
    }

    @Override
    public int getTotalProgress() {
        return totalProgress;
    }

    @Override
    public void setTotalProgress(int i) {
        this.totalProgress = i;
    }

    @Override
    public BiPredicate<ItemStack, Integer> validItems() {
        return (stack, slot) -> itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public void onItemAdded(int slot) {
        checkRecipe();
    }
}
