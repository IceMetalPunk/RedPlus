package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlipGlass extends Block {

	private IIcon[] icons = new IIcon[16];

	protected BlockFlipGlass() {
		super(Material.glass);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		this.onNeighborBlockChange(world, x, y, z, this);
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		int power = world.getStrongestIndirectPower(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, power, 3);
	}

	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return 15 - meta;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return this.icons[meta];
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		for (int p = 0; p <= 15; ++p) {
			// System.out.println("Trying to register "+this.getTextureName() +
			// "_" + p);
			icons[p] = register.registerIcon(this.getTextureName() + "_" + p);
		}
	}

}
