package ru.mousecray.endmagic.util.registry;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;

public interface CreativeTabProvider {
	@Nullable public CreativeTabs creativeTab();
}
