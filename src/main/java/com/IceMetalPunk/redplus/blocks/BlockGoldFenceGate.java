package com.IceMetalPunk.redplus.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGoldFenceGate extends BlockFenceGate {

	public BlockGoldFenceGate() {
		super();
		this.setHarvestLevel("pickaxe", 2);
		this.setBlockTextureName("gold_block");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.gold_block.getBlockTextureFromSide(p_149691_1_);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

	@Override
	public int getLightOpacity() {
		return 1;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
	}

}