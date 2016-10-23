package com.IceMetalPunk.redplus.blocks;

import java.util.Random;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockGlassPressurePlate extends BlockPressurePlate {

	protected BlockGlassPressurePlate() {
		super("redplus:glass_pressure_plate", Material.glass, BlockPressurePlate.Sensitivity.players);
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
