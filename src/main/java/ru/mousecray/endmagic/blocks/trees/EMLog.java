package ru.mousecray.endmagic.blocks.trees;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import ru.mousecray.endmagic.util.NameAndTabUtils;
import ru.mousecray.endmagic.util.NameProvider;

import java.util.Arrays;
import java.util.function.Function;

public class EMLog<TreeType extends Enum<TreeType> & IStringSerializable> extends BlockLog implements NameProvider {
    private final IProperty<TreeType> treeType;
    private final Function<Integer, TreeType> byIndex;
    private final Class<TreeType> type;

    public EMLog(Class<TreeType> type, Function<Integer, TreeType> byIndex) {
        super();
        this.type = type;
        treeType = PropertyEnum.create("tree_type", type);
        this.byIndex = byIndex;

        //super
        blockState = new BlockStateContainer(this, LOG_AXIS, treeType);
        //

        setDefaultState(blockState.getBaseState()
                .withProperty(LOG_AXIS, BlockLog.EnumAxis.Y)
                .withProperty(treeType, byIndex.apply(0)));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int axis = meta & 3;
        int type = meta >> 2;
        return getDefaultState()
                .withProperty(LOG_AXIS, BlockLog.EnumAxis.values()[axis])
                .withProperty(treeType, byIndex.apply(type));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal() + (state.getValue(treeType).ordinal() << 2);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    public String name() {
        return NameAndTabUtils.getName(type) + "_log";
    }
}
