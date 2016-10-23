package com.IceMetalPunk.redplus.dispenserDefs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class RedPlusDispenseWaterBottles implements IBehaviorDispenseItem {

	PlayerUseItemEvent p;

	public RedPlusDispenseWaterBottles() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.glass_bottle, this);
	}

	@Override
	public ItemStack dispense(IBlockSource dispenser, ItemStack stack) {
		BehaviorDefaultDispenseItem defaultDispenser = new BehaviorDefaultDispenseItem();
		EnumFacing facing = BlockDispenser.func_149937_b(dispenser.getBlockMetadata());
		double offX = dispenser.getX() + (double) facing.getFrontOffsetX();
		double offY = (double) ((float) dispenser.getYInt() + facing.getFrontOffsetY());
		double offZ = dispenser.getZ() + (double) facing.getFrontOffsetZ();

		int bX = (int) Math.floor(offX);
		int bY = (int) Math.floor(offY);
		int bZ = (int) Math.floor(offZ);

		Block blockAt = dispenser.getWorld().getBlock(bX, bY, bZ);
		if (blockAt != Blocks.water && blockAt != Blocks.flowing_water) {
			return defaultDispenser.dispense(dispenser, stack);
		}

		stack.splitStack(1);
		TileEntityDispenser entity = (TileEntityDispenser) dispenser.getBlockTileEntity();
		ItemStack bottleStack = new ItemStack(Items.potionitem, 1);
		boolean found = false;
		for (int slot = 0; slot < entity.getSizeInventory(); ++slot) {
			if (entity.getStackInSlot(slot) == null && entity.isItemValidForSlot(slot, bottleStack)) {
				entity.setInventorySlotContents(slot, bottleStack);
				found = true;
				break;
			}
		}

		if (!found) {
			defaultDispenser.dispense(dispenser, bottleStack);
			return stack;
		}
		else {
			return stack;
		}
	}
}
