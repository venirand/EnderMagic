package ru.mousecray.endmagic.client.gui.elements;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiScreen;
import ru.mousecray.endmagic.api.embook.BookApi;
import ru.mousecray.endmagic.api.embook.alignment.Rectangle;
import ru.mousecray.endmagic.client.gui.IStructuralGuiElement;

public class Tooltip extends GuiScreen implements IStructuralGuiElement {
    public final ImmutableList<String> lines;
    public final Rectangle rectangle;

    public Tooltip(ImmutableList<String> lines, Rectangle rectangle) {
        this.lines = lines;
        this.rectangle = rectangle;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (rectangle.contains(mouseX, mouseY, BookApi.pageWidth, BookApi.pageHeight))
            drawHoveringText(lines, mouseX, mouseY);

    }
}
