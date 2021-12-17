package wintersteve25.primutils.common.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PrimUtilsFluidTE extends PrimUtilsTE {
    protected final FluidTank tank = new FluidTank(fluidTankCapacity()) {
        @Override
        protected void onContentsChanged() {
            PrimUtilsFluidTE.super.updateBlock();
            onFluidChanged();
        }
    };
    private final LazyOptional<IFluidHandler> tankLazyOptional = LazyOptional.of(() -> tank);

    public PrimUtilsFluidTE(TileEntityType<?> te) {
        super(te);
    }

    protected abstract int fluidTankCapacity();

    protected void onFluidChanged() {
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        tank.readFromNBT(tag.getCompound("tank"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        tag.put("tank", tankNBT);
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return tankLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
