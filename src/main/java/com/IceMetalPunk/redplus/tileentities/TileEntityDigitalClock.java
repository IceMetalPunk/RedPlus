package com.IceMetalPunk.redplus.tileentities;

import com.IceMetalPunk.redplus.blocks.BlockDigitalClock;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDigitalClock extends TileEntity {
	public TileEntityDigitalClock() {
		super();
	}

	@Override
	public void updateEntity() {
		if (this.worldObj.getTotalWorldTime() % 20L == 0) {
			Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
			if (block instanceof BlockDigitalClock) {
				((BlockDigitalClock) block).setTime(this.worldObj.getWorldTime());
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, block);
			}
		}
	}
}
