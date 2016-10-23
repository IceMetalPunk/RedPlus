package com.IceMetalPunk.redplus.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockButton;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockGlassButton extends BlockButton {

	protected BlockGlassButton() {
		super(false);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return RedPlusBlocks.glassPressurePlate.getIcon(side, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getItemIconName() {
		return "redplus:glass_button";
	}

	@Override
	public boolean canSilkHarvest() {
		return true;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return null;
	}

}
