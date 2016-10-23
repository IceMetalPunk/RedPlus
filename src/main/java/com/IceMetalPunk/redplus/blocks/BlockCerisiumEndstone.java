package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCerisiumEndstone extends Block {
	IIcon[] icons = new IIcon[4];

	public BlockCerisiumEndstone() {
		super(Material.rock);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (world.getStrongestIndirectPower(x, y, z) > 0);
		if (meta == 0 && powered) {
			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
		}
		else if (meta == 1 && !powered) {
			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
		}
		super.onNeighborBlockChange(world, x, y, z, block);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 0) {
			if (side < 2) {
				return icons[2];
			}
			else {
				return icons[0];
			}
		}
		else {
			if (side < 2) {
				return icons[3];
			}
			else {
				return icons[1];
			}
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons[0] = register.registerIcon(this.getTextureName() + "_side_off");
		icons[1] = register.registerIcon(this.getTextureName() + "_side_on");
		icons[2] = register.registerIcon(this.getTextureName() + "_off");
		icons[3] = register.registerIcon(this.getTextureName() + "_on");
	}

}
