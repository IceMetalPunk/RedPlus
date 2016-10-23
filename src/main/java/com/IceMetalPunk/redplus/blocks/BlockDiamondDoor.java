package com.IceMetalPunk.redplus.blocks;

import java.util.Random;

import com.IceMetalPunk.redplus.items.RedPlusItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockDiamondDoor extends BlockDoor {

	public BlockDiamondDoor() {
		super(Material.anvil);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py,
			float pz) {
		boolean thisPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
		boolean otherPowered = false;
		if ((world.getBlockMetadata(x, y, z) & 8) == 0) {
			otherPowered = world.isBlockIndirectlyGettingPowered(x, y + 1, z);
		}
		else {
			otherPowered = world.isBlockIndirectlyGettingPowered(x, y - 1, z);
		}

		if (thisPowered || otherPowered) {
			return false;
		}
		else {
			super.onBlockActivated(world, x, y, z, player, side, px, py, pz);
			return true;
		}
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return (meta & 8) != 0 ? null : RedPlusItems.diamondDoor;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int meta = world.getBlockMetadata(x, y, z);

		if ((meta & 8) == 0) {
			boolean flag = false;

			if (world.getBlock(x, y + 1, z) != this) {
				world.setBlockToAir(x, y, z);
				flag = true;
			}

			if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
				world.setBlockToAir(x, y, z);
				flag = true;

				if (world.getBlock(x, y + 1, z) == this) {
					world.setBlockToAir(x, y + 1, z);
				}
			}

			if (flag) {
				if (!world.isRemote) {
					this.dropBlockAsItem(world, x, y, z, meta, 0);
				}
			}
		}
		else {
			if (world.getBlock(x, y - 1, z) != this) {
				world.setBlockToAir(x, y, z);
			}

			if (block != this) {
				this.onNeighborBlockChange(world, x, y - 1, z, block);
			}
		}
	}

}
