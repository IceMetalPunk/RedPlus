package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.tileentities.TileEntityLightSensor;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightSensor extends Block implements ITileEntityProvider {

	protected BlockLightSensor() {
		super(Material.glass);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
		World world = (World) blockAccess;
		int xoff = 0, yoff = 0, zoff = 0;
		int value = 0, faceValue = 0;
		for (int face = 0; face < 6; ++face) {
			xoff = Facing.offsetsXForSide[face];
			yoff = Facing.offsetsYForSide[face];
			zoff = Facing.offsetsZForSide[face];
			faceValue = world.getSavedLightValue(EnumSkyBlock.Block, x + xoff, y + yoff, z + zoff);
			// world.light
			value = Math.max(value, faceValue);
		}
		return value;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p2) {
		return new TileEntityLightSensor();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

}
