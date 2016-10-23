package com.IceMetalPunk.redplus.dispenserDefs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedPlusDispenseCartOverride implements IBehaviorDispenseItem {
	private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

	public RedPlusDispenseCartOverride() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.chest_minecart, this);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.hopper_minecart, this);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.furnace_minecart, this);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.command_block_minecart, this);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.tnt_minecart, this);
	}

	public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
		EnumFacing enumfacing = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
		World world = p_82487_1_.getWorld();
		double d0 = p_82487_1_.getX() + (double) ((float) enumfacing.getFrontOffsetX() * 1.125F);
		double d1 = p_82487_1_.getY() + (double) ((float) enumfacing.getFrontOffsetY() * 1.125F);
		double d2 = p_82487_1_.getZ() + (double) ((float) enumfacing.getFrontOffsetZ() * 1.125F);
		int i = p_82487_1_.getXInt() + enumfacing.getFrontOffsetX();
		int j = p_82487_1_.getYInt() + enumfacing.getFrontOffsetY();
		int k = p_82487_1_.getZInt() + enumfacing.getFrontOffsetZ();
		Block block = world.getBlock(i, j, k);
		double d3;

		if (BlockRailBase.func_150051_a(block)) {
			d3 = 0.0D;
		}
		else {
			if (block.getMaterial() != Material.air || !BlockRailBase.func_150051_a(world.getBlock(i, j - 1, k))) {
				return this.behaviourDefaultDispenseItem.dispense(p_82487_1_, p_82487_2_);
			}

			d3 = -1.0D;
		}

		EntityMinecart entityminecart = EntityMinecart.createMinecart(world, d0, d1 + d3, d2, ((ItemMinecart) p_82487_2_.getItem()).minecartType);

		if (p_82487_2_.stackTagCompound != null) {
			entityminecart.readFromNBT(p_82487_2_.stackTagCompound);
		}

		if (p_82487_2_.hasDisplayName()) {
			entityminecart.setMinecartName(p_82487_2_.getDisplayName());
		}

		entityminecart.posX = d0;
		entityminecart.posY = d1 + d3;
		entityminecart.posZ = d2;
		entityminecart.motionX = 0.0;
		entityminecart.motionY = 0.0;
		entityminecart.motionZ = 0.0;

		world.spawnEntityInWorld(entityminecart);
		p_82487_2_.splitStack(1);
		return p_82487_2_;
	}

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		return this.dispenseStack(source, stack);
	}

	protected void playDispenseSound(IBlockSource p_82485_1_) {
		p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
	}

}
