package com.IceMetalPunk.redplus.dispenserDefs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedPlusDispenseBlocks implements IBehaviorDispenseItem {

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		Block block = Block.getBlockFromItem(stack.getItem());

		if (block != Blocks.air) {

			EnumFacing facing = BlockDispenser.func_149937_b(source.getBlockMetadata());
			double offX = source.getX() + (double) facing.getFrontOffsetX();
			double offY = (double) ((float) source.getYInt() + facing.getFrontOffsetY());
			double offZ = source.getZ() + (double) facing.getFrontOffsetZ();

			int bX = (int) Math.floor(offX);
			int bY = (int) Math.floor(offY);
			int bZ = (int) Math.floor(offZ);

			World world = source.getWorld();

			if (block.canPlaceBlockAt(world, bX, bY, bZ)) {
				EntityPlayer player = world.getClosestPlayer(bX, bY, bZ, -1);
				if (player != null) {
					stack.getItem().onItemUse(stack, world.getClosestPlayer(bX, bY, bZ, -1), world, bX, bY - 1, bZ, 1, bX, bY - 1, bZ);
				}
			}
		}

		return stack;
	}

}
