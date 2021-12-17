package wintersteve25.primutils.common.base;

import fictioncraft.wintersteve25.fclib.common.base.FCLibTEInv;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class PrimUtilsTE extends FCLibTEInv {
    public PrimUtilsTE(TileEntityType<?> te) {
        super(te);
    }

    public List<ItemStack> getAllItemsInInventory() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < getInvSize(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) list.add(stack);
        }
        return list;
    }

    public boolean addItem(PlayerEntity player, ItemStack heldItem, BlockRayTraceResult hit) {
        for (int i = 0; i < getInvSize(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                ItemStack itemAdd = heldItem.copy();
                itemAdd.setCount(heldItem.getCount());
                itemHandler.insertItem(i, itemAdd, false);
                if (player != null) {
                    heldItem.shrink(heldItem.getCount());
                }
                updateBlock();
                return true;
            }
        }

        return false;
    }

    public boolean removeItem(PlayerEntity player) {
        for (int i = getInvSize() - 1; i >= 0; i--) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            player.addItemStackToInventory(stack.copy());
            itemHandler.extractItem(i, stack.getCount(), false);
            updateBlock();
            return true;
        }

        return false;
//        for (int i = 0; i < getInvSize(); i++) {
//            ItemStack stack = itemHandler.getStackInSlot(i);
//            if (!stack.isEmpty()) {
//                player.addItemStackToInventory(stack.copy());
//                itemHandler.extractItem(i, stack.getCount(), false);
//                updateBlock();
//                return true;
//            }
//        }
//
//        return false;
    }

    public void onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
