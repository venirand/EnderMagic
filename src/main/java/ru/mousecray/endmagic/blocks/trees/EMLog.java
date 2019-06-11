package ru.mousecray.endmagic.blocks.trees;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import ru.mousecray.endmagic.client.render.model.IModelRegistration;
import ru.mousecray.endmagic.util.IEMModel;
import ru.mousecray.endmagic.util.NameAndTabUtils;
import ru.mousecray.endmagic.util.NameProvider;

import java.util.function.Function;

public class EMLog<TreeType extends Enum<TreeType> & IStringSerializable> extends BlockLog implements NameProvider, IEMModel {
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

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(treeType, byIndex.apply(stack.getItemDamage())));
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 4; i++)
            items.add(new ItemStack(this, 1, i));
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
        String rawName = NameAndTabUtils.getName(type);
        return rawName.substring(0, rawName.lastIndexOf('_')) + "_log";
    }

    @Override
    public void registerModels(IModelRegistration modelRegistration) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
        for (int i = 1; i < 4; i++)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i,
                    new ModelResourceLocation(getRegistryName(), "inventory,meta=" + i));
    }
}