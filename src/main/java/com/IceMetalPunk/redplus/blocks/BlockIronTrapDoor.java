package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockIronTrapDoor extends BlockTrapDoor {

	protected BlockIronTrapDoor() {
		super(Material.iron);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py,
			float pz) {
		return false;
	}

}
