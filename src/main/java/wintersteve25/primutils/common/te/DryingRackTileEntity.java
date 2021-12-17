package wintersteve25.primutils.common.te;

import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasValidItems;
import fictioncraft.wintersteve25.fclib.common.interfaces.IWorkable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import wintersteve25.primutils.common.base.PrimUtilsTE;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.drying_rack.IDryingRackRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;

public class DryingRackTileEntity extends PrimUtilsTE implements IHasValidItems, ITickableTileEntity {

    private final ItemStackHandler outputHandler = new FCLibInventoryHandler(this);
    private final LazyOptional<IItemHandler> outputLazyOptional = LazyOptional.of(()->outputHandler);

    private final DryingRackProcessor SLOT_ONE = new DryingRackProcessor(this, 0);
    private final DryingRackProcessor SLOT_TWO = new DryingRackProcessor(this, 1);
    private final DryingRackProcessor SLOT_THREE = new DryingRackProcessor(this, 2);
    private final DryingRackProcessor SLOT_FOUR = new DryingRackProcessor(this, 3);

    public DryingRackTileEntity() {
        super(PrimUtilsBlocks.DRYING_RACK_TE.get());
    }

    @Override
    public void tick() {
        if (!isServer()) return;
        SLOT_ONE.tick();
        SLOT_TWO.tick();
        SLOT_THREE.tick();
        SLOT_FOUR.tick();
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

    @Override
    public boolean removeItem(PlayerEntity player) {
        for (int i = 0; i < getInvSize(); i++) {
            ItemStack stack = outputHandler.getStackInSlot(i);
            ItemStack stack1 = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                player.addItemStackToInventory(stack.copy());
                outputHandler.extractItem(i, stack.getCount(), false);
                updateBlock();
                return true;
            }
            if (!stack1.isEmpty()) {
                player.addItemStackToInventory(stack1.copy());
                itemHandler.extractItem(i, stack1.getCount(), false);
                updateBlock();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onItemAdded(int slot) {
        checkRecipe(slot);
    }

    private void checkRecipe(int slot) {
        switch (slot) {
            case 0:
                SLOT_ONE.checkRecipe();
                break;
            case 1:
                SLOT_TWO.checkRecipe();
                break;
            case 2:
                SLOT_THREE.checkRecipe();
                break;
            case 3:
                SLOT_FOUR.checkRecipe();
                break;
        }
    }

    @Override
    public int getInvSize() {
        return 4;
    }

    @Override
    public BiPredicate<ItemStack, Integer> validItems() {
        return (stack, slot) -> this.itemHandler.getStackInSlot(slot).isEmpty() && this.outputHandler.getStackInSlot(slot).isEmpty();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        SLOT_ONE.readFromNBT(tag);
        SLOT_TWO.readFromNBT(tag);
        SLOT_THREE.readFromNBT(tag);
        SLOT_FOUR.readFromNBT(tag);
        outputHandler.deserializeNBT(tag.getCompound("outputHandler"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        SLOT_ONE.writeToNBT(tag);
        SLOT_TWO.writeToNBT(tag);
        SLOT_THREE.writeToNBT(tag);
        SLOT_FOUR.writeToNBT(tag);
        tag.put("outputHandler", outputHandler.serializeNBT());
        return super.write(tag);
    }

    public ItemStackHandler getOutputHandler() {
        return outputHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN) {
                return outputLazyOptional.cast();
            }
            return itemLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    public static class DryingRackProcessor implements IWorkable, IHasProgress {

        private final DryingRackTileEntity parent;

        private final int indexInHandler;
        private int totalProgress = -1;
        private int progress = totalProgress;
        private boolean working = false;

        public DryingRackProcessor(DryingRackTileEntity parent, int indexInHandler) {
            this.parent = parent;
            this.indexInHandler = indexInHandler;
        }

        public void tick() {
            if (getWorking()) {
                BlockPos pos = parent.getPos();
                progress--;
                if (progress % 2 == 0 && MiscHelper.chanceHandling(4)) {
                    ((ServerWorld) parent.getWorld()).spawnParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + 0.5 + MiscHelper.randomInRange(-0.25, 0.25), pos.getY() + 0.38 + MiscHelper.randomInRange(-0.12, 0.05), pos.getZ() + 0.5 + MiscHelper.randomInRange(-0.25, 0.25), 4, 0, 0, 0, 0.18d);
                }
                if (progress == 0) {
                    IDryingRackRecipe recipe = getRecipe();
                    if (recipe == null) return;
                    parent.getWorld().playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1f);
                    parent.itemHandler.extractItem(indexInHandler, recipe.getInput().getMatchingStacks()[0].getCount(), false);
                    parent.outputHandler.insertItem(indexInHandler, recipe.getOutput().copy(), false);
                    totalProgress = -1;
                    progress = totalProgress;
                    setWorking(false);
                    checkRecipe();
                }
            }
        }

        public IDryingRackRecipe getRecipe() {
            ItemStack input = parent.itemHandler.getStackInSlot(indexInHandler);
            if (input.isEmpty()) return null;
            if (parent.getWorld() == null) return null;
            List<IDryingRackRecipe> recipes = parent.getWorld().getRecipeManager().getRecipesForType(PrimUtilsRecipes.DRYING_RACK_RT);
            if (recipes.isEmpty()) return null;
            for (IDryingRackRecipe recipe : recipes) {
                if (recipe.match(input)) {
                    return recipe;
                }
            }
            return null;
        }

        private void checkRecipe() {
            IDryingRackRecipe recipe = getRecipe();
            if (recipe != null && !getWorking()) {
                setWorking(true);
                totalProgress = recipe.getDuration();
                progress = totalProgress;
            } else if (recipe == null && getWorking()){
                setWorking(false);
                totalProgress = -1;
                progress = totalProgress;
            }
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
        public boolean getWorking() {
            return working;
        }

        @Override
        public void setWorking(boolean b) {
            this.working = b;
        }

        public void writeToNBT(CompoundNBT tag) {
            tag.putInt("progress" + indexInHandler, progress);
            tag.putBoolean("working" + indexInHandler, working);
        }

        public void readFromNBT(CompoundNBT tag) {
            setProgress(tag.getInt("progress" + indexInHandler));
            setWorking(tag.getBoolean("working" + indexInHandler));
        }
    }
}
