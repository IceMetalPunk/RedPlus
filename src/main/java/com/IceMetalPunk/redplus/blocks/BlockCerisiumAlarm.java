package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.tileentities.TileEntityCerisiumAlarm;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCerisiumAlarm extends Block implements ITileEntityProvider {

	public BlockCerisiumAlarm() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCerisiumAlarm();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (world.getStrongestIndirectPower(x, y, z) > 0);
		TileEntityCerisiumAlarm tileEntity = (TileEntityCerisiumAlarm) world.getTileEntity(x, y, z);
		if (meta == 0 && powered) {
			// FIXME: Cerisium Alarm is not complete!
		}
	}

}
