package com.IceMetalPunk.redplus.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TileEntityLightSensor extends TileEntity {
	@Override
	public void updateEntity() {
		if (this.worldObj.getTotalWorldTime() % 10L == 0L) {
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		}
	}
}
