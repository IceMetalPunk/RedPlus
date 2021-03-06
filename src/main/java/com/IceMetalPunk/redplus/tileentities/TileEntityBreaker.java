package com.IceMetalPunk.redplus.tileentities;

import com.IceMetalPunk.redplus.tileentities.containers.BlockSlot;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDropper;

public class TileEntityBreaker extends TileEntityDropper {
	public TileEntityBreaker() {
		super();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		return BlockSlot.isValidBlock(item);
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.field_146020_a : "container.breaker";
	}

}
