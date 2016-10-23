package com.IceMetalPunk.redplus.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCerisiumCropland extends TileEntity {

	private int growth;
	private int maxGrowth;

	public TileEntityCerisiumCropland() {
		super();
		this.growth = 0;
		this.maxGrowth = 7;
	}

	public int getGrowth() {
		return this.growth;
	}

	public int getMaxGrowth() {
		return this.maxGrowth;
	}

	@Override
	public void updateEntity() {
		if (this.worldObj.getTotalWorldTime() % 20L == 0) {
			int newGrowth = 0;
			Block above = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord);

			if (above == Blocks.nether_wart) {
				this.maxGrowth = 3;
			}
			else {
				this.maxGrowth = 7;
			}

			if (above == Blocks.reeds || above == Blocks.cactus) {
				this.maxGrowth = 3;
				int y = this.yCoord + 1;
				for (; y <= this.yCoord + 3; ++y) {
					if (this.worldObj.getBlock(this.xCoord, y, this.zCoord) != above) {
						--y;
						break;
					}
				}
				newGrowth = y - this.yCoord;
			}
			else if (!(above instanceof IGrowable) && above != Blocks.nether_wart) {
				newGrowth = 0;
			}
			else {
				newGrowth = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
			}

			if (newGrowth != this.growth) {
				this.growth = newGrowth;
				Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
				this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, block);
			}
			else {
				this.growth = newGrowth;
			}

		}
	}
}
