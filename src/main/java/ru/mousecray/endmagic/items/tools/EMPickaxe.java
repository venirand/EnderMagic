package ru.mousecray.endmagic.items.tools;

import net.minecraft.item.ItemPickaxe;
import ru.mousecray.endmagic.EM;
import ru.mousecray.endmagic.util.registry.NameProvider;

public class EMPickaxe extends ItemPickaxe implements NameProvider, ItemTexturedTool {

    private final String name;
    private final int color;

    public EMPickaxe(ToolMaterial material, String name, int color) {
        super(material);
        this.name = name;
        this.color = color;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String stick() {
        return EM.ID + ":items/tools/pickaxe_stick";
    }

    @Override
    public String toolType() {
        return "pickaxe";
    }

    @Override
    public int color() {
        return color;
    }
}
