package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCerisiumIce extends Block {
	IIcon[] icons = new IIcon[2];

	public BlockCerisiumIce() {
		super(Material.packedIce);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (world.getStrongestIndirectPower(x, y, z) > 0);
		if (meta == 0 && powered) {
			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
			this.slipperiness = 0.98f;
		}
		else if (meta == 1 && !powered) {
			world.setBlockMetadataWithNotify(x, y, z, 0, 3);
			this.slipperiness = 0.6f;
		}
		super.onNeighborBlockChange(world, x, y, z, block);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta < icons.length && meta >= 0) {
			return this.icons[meta];
		}
		else {
			return this.icons[0];
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		icons[0] = register.registerIcon(this.getTextureName() + "_off");
		icons[1] = register.registerIcon(this.getTextureName() + "_on");
	}

}
