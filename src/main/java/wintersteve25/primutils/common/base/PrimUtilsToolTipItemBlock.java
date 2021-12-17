package wintersteve25.primutils.common.base;

import com.google.common.collect.Lists;
import fictioncraft.wintersteve25.fclib.common.base.FCLibBlockItem;
import fictioncraft.wintersteve25.fclib.common.interfaces.IColoredName;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasToolTip;
import net.minecraft.block.Block;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import wintersteve25.primutils.PrimitiveUtils;

import java.util.List;

public class PrimUtilsToolTipItemBlock extends FCLibBlockItem implements IHasToolTip, IColoredName {

    private final ITextComponent[] texts;

    public PrimUtilsToolTipItemBlock(Block blockIn, Properties properties, ITextComponent... text) {
        super(blockIn, properties.group(PrimitiveUtils.creativeTab));
        texts = text;
    }

    @Override
    public List<ITextComponent> tooltip() {
        return Lists.newArrayList(texts);
    }

    @Override
    public TextFormatting color() {
        return TextFormatting.DARK_RED;
    }
}
