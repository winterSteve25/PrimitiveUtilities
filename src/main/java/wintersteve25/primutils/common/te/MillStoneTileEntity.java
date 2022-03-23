package wintersteve25.primutils.common.te;

import com.google.common.collect.ImmutableMap;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IWorkable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import wintersteve25.primutils.common.base.PrimUtilsTE;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.millstone.IMillstoneRecipe;

import java.util.List;

public class MillStoneTileEntity extends PrimUtilsTE implements IAnimatable, IHasProgress, IWorkable, ITickableTileEntity {

    public static final int TICKS_FOR_ONE_OPERATION = 40;

    private final AnimationFactory manager = new AnimationFactory(this);
    private boolean working = false;
    private int totalProgress = -1;
    private int progress = totalProgress;
    private int operationProgress = TICKS_FOR_ONE_OPERATION;

    public MillStoneTileEntity() {
        super(PrimUtilsBlocks.MILLSTONE_TE.get());
    }

    @Override
    public void tick() {
        if (getWorld().isRemote()) return;
        if (getWorking()) {
            operationProgress--;
            if (operationProgress <= 0) {
                operationProgress = TICKS_FOR_ONE_OPERATION;
                progress--;
                if (progress == 0) {
                    IMillstoneRecipe recipe = getRecipe(itemHandler.getStackInSlot(0)).getB();
                    if (recipe == null) return;
                    spawnOutput(recipe.getOutput());
                    itemHandler.extractItem(0, recipe.getInput().getMatchingStacks()[0].getCount(), false);
                    totalProgress = -1;
                    progress = totalProgress;
                    operationProgress = TICKS_FOR_ONE_OPERATION;

                    setupRecipe();
                }
                setWorking(false);
            }
            updateBlock();
        }
    }

    @Override
    public void onItemAdded(int slot) {
        setupRecipe();
    }

    private void setupRecipe() {
        IMillstoneRecipe recipe = getRecipe(itemHandler.getStackInSlot(0)) == null ? null : getRecipe(itemHandler.getStackInSlot(0)).getB();
        if (recipe != null) {
            if (totalProgress == -1) {
                totalProgress = recipe.getTotalProgress();
                progress = totalProgress;
            }
        } else {
            totalProgress = -1;
            progress = totalProgress;
            operationProgress = TICKS_FOR_ONE_OPERATION;
            setWorking(false);
        }
    }

    private boolean checkRecipe() {
        IMillstoneRecipe recipe = getRecipe(itemHandler.getStackInSlot(0)) == null ? null : getRecipe(itemHandler.getStackInSlot(0)).getB();
        return recipe != null;
    }

    private void spawnOutput(ImmutableMap<ItemStack, Byte> output) {
        for (ItemStack stack : output.keySet()) {
            if (getWorld() != null && !getWorld().isRemote()) {
                if (MiscHelper.chanceHandling(output.get(stack))) {
                    if (!getWorld().isRemote()) {
                        ((ServerWorld) getWorld()).spawnParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 4, 0, 0, 0, 0.4d);
                    }
                    getWorld().playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1f);
                    InventoryHelper.spawnItemStack(getWorld(), pos.getX(), (double) pos.getY() + 0.5, pos.getZ(), stack.copy());
                } else {
                    getWorld().playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1f);
                }
            }
        }
    }

    public Tuple<ImmutableMap<ItemStack, Byte>, IMillstoneRecipe> getRecipe(ItemStack input) {
        if (input.isEmpty()) return null;
        if (getWorld() == null) return null;
        List<IMillstoneRecipe> recipes = getWorld().getRecipeManager().getRecipesForType(PrimUtilsRecipes.MILLSTONE_RT);
        if (recipes.isEmpty()) return null;
        for (IMillstoneRecipe millstoneRecipe : recipes) {
            if (millstoneRecipe.match(input)) {
                return new Tuple<>(millstoneRecipe.getOutput(), millstoneRecipe);
            }
        }
        return null;
    }

    @Override
    public void onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isSneaking() && player.getHeldItem(handIn).isEmpty() && !getWorking() && progress != 0) {
            if (itemHandler.getStackInSlot(0).isEmpty()) return;
            if (checkRecipe()) {
                setupRecipe();
                getWorld().playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.0F, 1f);
                setWorking(true);
                updateBlock();
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("operationProgress", operationProgress);
        return super.write(tag);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        operationProgress = tag.getInt("operationProgress");
        super.read(state, tag);
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 1;
        if (getWorking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.millstone.use", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder());
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return manager;
    }

    @Override
    public int getInvSize() {
        return 1;
    }

    @Override
    public boolean getWorking() {
        return working;
    }

    @Override
    public void setWorking(boolean b) {
        this.working = b;
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
}
