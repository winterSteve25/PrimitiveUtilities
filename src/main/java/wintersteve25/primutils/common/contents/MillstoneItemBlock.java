package wintersteve25.primutils.common.contents;

import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.client.MillstoneISTER;
import wintersteve25.primutils.common.base.PrimUtilsBlockItemAnimated;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;

public class MillstoneItemBlock extends PrimUtilsBlockItemAnimated {
    public MillstoneItemBlock() {
        super(PrimUtilsBlocks.MILLSTONE, new Item.Properties().setISTER(()->MillstoneISTER::new), new TranslationTextComponent("primutils.tooltip.millstone"));
    }
}
