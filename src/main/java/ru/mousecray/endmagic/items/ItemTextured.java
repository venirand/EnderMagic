package ru.mousecray.endmagic.items;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import ru.mousecray.endmagic.EM;
import ru.mousecray.endmagic.client.render.model.IModelRegistration;
import ru.mousecray.endmagic.proxy.ClientProxy;
import ru.mousecray.endmagic.util.CreativeTabProvider;
import ru.mousecray.endmagic.util.IEMModel;

public abstract class ItemTextured extends Item implements IEMModel, CreativeTabProvider {
    public ItemTextured() {
        ClientProxy.registerTexture(new ResourceLocation(textureName()));
    }

    public abstract String textureName();

    @Override
    public void registerModels(IModelRegistration modelRegistration) {
        ModelLoader.setCustomModelResourceLocation(this, 0, companion.simpletexturemodel);
    }

    @Nullable
    @Override
    public CreativeTabs creativeTab() {
        return EM.EM_CREATIVE;
    }

    public static class companion {
        public static ItemTextured simpletexturemodelItem = new ItemTextured() {
            public String textureName() {
                return "none";
            }

            public CreativeTabs creativeTab() {
                return null;
            }

			@Override
			public void registerModels(IModelRegistration modelRegistration) {}
        };
        public static ModelResourceLocation simpletexturemodel = new ModelResourceLocation(EM.ID + ":simpletexturemodel", "inventory");

    }
}