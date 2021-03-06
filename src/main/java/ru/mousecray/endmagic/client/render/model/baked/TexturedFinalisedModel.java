package ru.mousecray.endmagic.client.render.model.baked;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ItemLayerModel;
import org.apache.commons.lang3.tuple.Pair;
import ru.mousecray.endmagic.util.render.elix_x.baked.UnpackedBakedQuad;
import ru.mousecray.endmagic.util.render.elix_x.ecomms.color.RGBA;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TexturedFinalisedModel extends BakedModelDelegate {
    private Map<String, Integer> parts;

    public TexturedFinalisedModel(IBakedModel baseModel, Map<String, Integer> key) {
        super(baseModel);
        parts = key;
    }

    private ImmutableList<Pair<TextureAtlasSprite, Integer>> getTextureAtlasSprite() {
        return parts
                .entrySet()
                .stream()
                .map(e -> Pair.of(
                        Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(e.getKey()),
                        e.getValue()))
                .collect(ImmutableList.toImmutableList());

    }

    private ImmutableList<BakedQuad> getQuads() {
        if (quads == null) {
            quads = getTextureAtlasSprite()
                    .stream()
                    .flatMap(p ->
                            ItemLayerModel.getQuadsForSprite(1, p.getKey(), DefaultVertexFormats.ITEM, Optional.empty())
                                    .stream()
                                    .map(UnpackedBakedQuad::unpack)
                                    .peek(quad -> quad.getVertices()
                                            .forEach(v -> v.setColor(RGBA.fromARGB(p.getValue()))))
                                    .map(quad -> quad.pack(DefaultVertexFormats.ITEM))
                    )
                    .collect(ImmutableList.toImmutableList());
        }

        return quads;
    }

    private ImmutableList<BakedQuad> quads;

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return getQuads();
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public ItemOverrideList getOverrides() {
        throw new UnsupportedOperationException("TexturedFinalisedModel#getOverrides");
    }
}
