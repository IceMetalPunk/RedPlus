package com.IceMetalPunk.redplus.tileentities.containers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BlockSlot extends Slot {

	public BlockSlot(IInventory inventory, int x, int y, int z) {
		super(inventory, x, y, z);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isValidBlock(stack);
	}

	public static boolean isValidBlock(ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());
		boolean isBlock = (block != Blocks.air);
		return (Block.getBlockFromItem(stack.getItem()) != Blocks.air);
	}
}
