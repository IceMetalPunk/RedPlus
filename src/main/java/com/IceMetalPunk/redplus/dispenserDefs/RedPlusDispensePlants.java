package com.IceMetalPunk.redplus.dispenserDefs;

import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class RedPlusDispensePlants implements IBehaviorDispenseItem {

	public RedPlusDispensePlants() {
		Iterator iterator = GameData.getItemRegistry().iterator();
		while (iterator.hasNext()) {
			Object item = iterator.next();
			if (item instanceof IPlantable) {
				BlockDispenser.dispenseBehaviorRegistry.putObject(item, this);
			}
		}
	}

	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		EnumFacing facing = BlockDispenser.func_149937_b(source.getBlockMetadata());
		double offX = source.getX() + (double) facing.getFrontOffsetX();
		double offY = (double) ((float) source.getYInt() + facing.getFrontOffsetY());
		double offZ = source.getZ() + (double) facing.getFrontOffsetZ();

		int bX = (int) Math.floor(offX);
		int bY = (int) Math.floor(offY);
		int bZ = (int) Math.floor(offZ);

		if (facing.getFrontOffsetY() > 0) {
			++bY;
		}

		Block ground = source.getWorld().getBlock(bX, bY - 1, bZ);

		World world = source.getWorld();
		boolean isAir = world.isAirBlock(bX, bY, bZ);
		boolean canPlant = world.getBlock(bX, bY - 1, bZ).canSustainPlant(world, bX, bY - 1, bZ, ForgeDirection.UP, (IPlantable) stack.getItem());
		if (canPlant && isAir) {
			world.setBlock(bX, bY, bZ, ((IPlantable) stack.getItem()).getPlant(world, bX, bY, bZ));
			stack.splitStack(1);
		}

		return stack;
	}

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		return this.dispenseStack(source, stack);
	}
};