package wintersteve25.primutils.common.base;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.util.text.ITextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.concurrent.Callable;

public class PrimUtilsBlockItemAnimated extends PrimUtilsToolTipItemBlock implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    public String controllerName = "controller";

    public PrimUtilsBlockItemAnimated(Block blockIn, Properties builder, ITextComponent... tooltips) {
        super(blockIn, builder, tooltips);
    }

    public <P extends BlockItem & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, controllerName, 1, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
