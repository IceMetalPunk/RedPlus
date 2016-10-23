package com.IceMetalPunk.redplus.blocks;

import java.util.HashMap;

import com.IceMetalPunk.redplus.tileentities.TileEntityDigitalClock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDigitalClock extends Block implements ITileEntityProvider {

	long time = 0;
	HashMap<String, World> outputMap = new HashMap<String, World>();

	protected BlockDigitalClock() {
		super(Material.iron);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
		int output = Math.round(this.time * 15.0f / 23999.0f);
		return output;
	}

	public void setTime(long setValue) {
		this.time = setValue;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDigitalClock();
	}

}
