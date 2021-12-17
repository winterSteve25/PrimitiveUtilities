package wintersteve25.primutils.common.contents;

import net.minecraft.item.Item;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.primutils.client.BasinISTER;
import wintersteve25.primutils.common.base.PrimUtilsBlockItemAnimated;
import wintersteve25.primutils.common.init.PrimUtilsBlocks;

public class BasinItemBlock extends PrimUtilsBlockItemAnimated {
    public BasinItemBlock() {
        super(PrimUtilsBlocks.BASIN, new Item.Properties().setISTER(()->BasinISTER::new), new TranslationTextComponent("primutils.tooltip.basin"));
    }
}
