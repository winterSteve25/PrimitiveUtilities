package wintersteve25.primutils.common.te;

import com.google.common.collect.ImmutableList;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasValidItems;
import fictioncraft.wintersteve25.fclib.common.interfaces.IWorkable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.capability.IFluidHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import wintersteve25.primutils.common.base.PrimUtilsFluidTE;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;
import wintersteve25.primutils.common.init.PrimUtilsConfig;
import wintersteve25.primutils.common.init.PrimUtilsRecipes;
import wintersteve25.primutils.common.recipes.basin.IBasinRecipe;

import java.util.List;
import java.util.function.BiPredicate;

public class BasinTileEntity extends PrimUtilsFluidTE implements IAnimatable, ITickableTileEntity, IWorkable, IHasProgress, IHasValidItems {

    public static final int TICKS_FOR_ONE_OPERATION = 40;

    private final AnimationFactory manager = new AnimationFactory(this);
    private int totalProgress = -1;
    private int progress = totalProgress;
    private boolean working = false;
    private int operationProgress = TICKS_FOR_ONE_OPERATION;

    public BasinTileEntity() {
        super(PrimUtilsBlocks.BASIN_TE.get());
    }

    @Override
    public void tick() {
        if (getWorld().isRemote()) return;
        if (getWorking()) {
            operationProgress--;
            if (operationProgress % 2 == 0 && MiscHelper.chanceHandling(12)) {
                ((ServerWorld) getWorld()).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, tank.getFluid().getFluid().getDefaultState().getBlockState()), pos.getX() + 0.5 + MiscHelper.randomInRange(-0.25, 0.25), pos.getY() + 0.45 + MiscHelper.randomInRange(-0.12, 0.05), pos.getZ() + 0.5 + MiscHelper.randomInRange(-0.25, 0.25), 4, 0, 0, 0, 0.1d);
            }
            if (operationProgress <= 0) {
                operationProgress = TICKS_FOR_ONE_OPERATION;
                progress--;
                if (progress == 0) {
                    IBasinRecipe recipe = getRecipe();
                    if (recipe == null) return;
                    spawnOutput(recipe);
                    for (Ingredient ing : recipe.getInput()) {
                        for (int i = 0; i < 4; i++) {
                            int count = ing.getMatchingStacks()[0].getCount();
                            if (ing.test(itemHandler.getStackInSlot(i)) && itemHandler.getStackInSlot(i).getCount() >= count) {
                                itemHandler.extractItem(i, count, false);
                                break;
                            }
                        }
                    }
                    tank.drain(recipe.getInputFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                    totalProgress = -1;
                    progress = totalProgress;
                    operationProgress = TICKS_FOR_ONE_OPERATION;
                    if (checkRecipe()) {
                        setupRecipe();
                    }
                }
                setWorking(false);
            }
            updateBlock();
        }
    }

    @Override
    public void onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isSneaking() && player.getHeldItem(handIn).isEmpty() && !getWorking() && progress != 0) {
            if (checkRecipe() && progress != -1) {
                getWorld().playSound((PlayerEntity) null, pos, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 1.0F, 1f);
                setWorking(true);
                updateBlock();
            }
            if (totalProgress < -1 || progress < -1) {
                totalProgress = -1;
                progress = totalProgress;
            }
        }
    }

    @Override
    public void onItemAdded(int slot) {
        setupRecipe();
    }

    @Override
    protected void onFluidChanged() {
        setupRecipe();
    }

    private void setupRecipe() {
        IBasinRecipe recipe = getRecipe();
        if (recipe != null) {
            if (totalProgress == -1) {
                totalProgress = recipe.getStirAmounts();
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
        IBasinRecipe recipe = getRecipe();
        return recipe != null;
    }

    private void spawnOutput(IBasinRecipe recipe) {
        if (getWorld() != null && !getWorld().isRemote()) {
            if (!getWorld().isRemote()) {
                ((ServerWorld) getWorld()).spawnParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 4, 0, 0, 0, 0.4d);
            }
            getWorld().playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1f);
            for (ItemStack stack : recipe.getOutput()) {
                InventoryHelper.spawnItemStack(getWorld(), pos.getX(), (double) pos.getY() + 0.5, pos.getZ(), stack.copy());
            }
        }
    }

    public IBasinRecipe getRecipe() {
        List<ItemStack> inputs = getAllItemsInInventory();
        if (getWorld() == null) return null;
        List<IBasinRecipe> recipes = getWorld().getRecipeManager().getRecipesForType(PrimUtilsRecipes.BASIN_RT);
        if (recipes.isEmpty()) return null;
        for (IBasinRecipe recipe : recipes) {
            if (recipe.match(ImmutableList.copyOf(inputs), tank.getFluid())) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected int fluidTankCapacity() {
        return 4000;
    }

    @Override
    public int getInvSize() {
        return 4;
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.basin.stir", true));
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

    @Override
    public BiPredicate<ItemStack, Integer> validItems() {
        return (stack, slot) -> PrimUtilsConfig.isBasinAllowStack() || itemHandler.getStackInSlot(slot).isEmpty();
    }

    @Override
    public boolean addItem(PlayerEntity player, ItemStack heldItem, BlockRayTraceResult hit) {
        if (!PrimUtilsConfig.isBasinAllowStack()) {
            for (int i = 0; i < getInvSize(); i++) {
                if (itemHandler.getStackInSlot(i).isEmpty()) {
                    ItemStack itemAdd = heldItem.copy();
                    itemAdd.setCount(1);
                    itemHandler.insertItem(i, itemAdd, false);
                    if (player != null) {
                        heldItem.shrink(1);
                    }
                    updateBlock();
                    return true;
                }
            }

            return false;
        }
        return super.addItem(player, heldItem, hit);
    }
}
