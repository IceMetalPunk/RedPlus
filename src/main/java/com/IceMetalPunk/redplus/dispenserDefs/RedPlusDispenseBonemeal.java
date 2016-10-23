package com.IceMetalPunk.redplus.dispenserDefs;

import com.IceMetalPunk.redplus.RedPlus;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedPlusDispenseBonemeal implements IBehaviorDispenseItem {
	private boolean playSound = true;

	public RedPlusDispenseBonemeal() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, this);
	}

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		return this.dispenseStack(source, stack);
	}

	protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
		if (p_82487_2_.getItemDamage() == 15) {

			EnumFacing enumfacing = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
			World world = p_82487_1_.getWorld();
			int i = p_82487_1_.getXInt() + enumfacing.getFrontOffsetX();
			int j = p_82487_1_.getYInt() + enumfacing.getFrontOffsetY();
			int k = p_82487_1_.getZInt() + enumfacing.getFrontOffsetZ();
			
			if (ItemDye.func_150919_a(p_82487_2_, world, i, j, k)) {
				if (!world.isRemote) {
					world.playAuxSFX(2005, i, j, k, 0);
				}
			}
			else if (!RedPlus.eventHandler.fakeBonemeal(world, i, j, k, p_82487_2_)) {
				this.playSound = false;
			}

			return p_82487_2_;
		}
		else {
			return null;
		}
	}

	/**
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource p_82485_1_) {
		if (this.playSound) {
			p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
		}
		else {
			p_82485_1_.getWorld().playAuxSFX(1001, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
		}
	}

}
