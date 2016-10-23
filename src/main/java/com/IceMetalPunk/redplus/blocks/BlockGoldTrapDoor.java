package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGoldTrapDoor extends BlockTrapDoor {

	protected BlockGoldTrapDoor() {
		super(Material.iron);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py,
			float pz) {
		int i1 = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, i1 ^ 4, 2);
		world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			int l = world.getBlockMetadata(x, y, z);
			int i1 = x;
			int j1 = z;

			if ((l & 3) == 0) {
				j1 = z + 1;
			}

			if ((l & 3) == 1) {
				--j1;
			}

			if ((l & 3) == 2) {
				i1 = x + 1;
			}

			if ((l & 3) == 3) {
				--i1;
			}

			if (!(isAttachableBlock(world.getBlock(i1, y, j1)) || world.isSideSolid(i1, y, j1, ForgeDirection.getOrientation((l & 3) + 2)))) {
				world.setBlockToAir(x, y, z);
				this.dropBlockAsItem(world, x, y, z, l, 0);
			}
		}
	}

	private boolean isAttachableBlock(Block block) {
		if (disableValidation) return true;
		return block.getMaterial().isOpaque() && block.renderAsNormalBlock() || block == Blocks.glowstone || block instanceof BlockSlab || block instanceof BlockStairs;
	}

}
