package com.IceMetalPunk.redplus.blocks;

import java.util.Random;

import com.IceMetalPunk.redplus.items.RedPlusItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockGoldDoor extends BlockDoor {

	public BlockGoldDoor() {
		super(Material.iron);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py,
			float pz) {
		int i1 = this.func_150012_g(world, x, y, z);
		int j1 = i1 & 7;
		j1 ^= 4;

		if ((i1 & 8) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, j1, 2);
			world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}
		else {
			world.setBlockMetadataWithNotify(x, y - 1, z, j1, 2);
			world.markBlockRangeForRenderUpdate(x, y - 1, z, x, y, z);
		}

		world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int l = world.getBlockMetadata(x, y, z);

		if ((l & 8) == 0) {
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
					this.dropBlockAsItem(world, x, y, z, l, 0);
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

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return (meta & 8) != 0 ? null : RedPlusItems.goldDoor;
	}

}
