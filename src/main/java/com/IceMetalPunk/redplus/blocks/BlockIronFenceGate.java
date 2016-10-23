package com.IceMetalPunk.redplus.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockIronFenceGate extends BlockFenceGate {

	public BlockIronFenceGate() {
		super();
		this.setHarvestLevel("pickaxe", 2);
		this.setBlockTextureName("iron_block");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.iron_block.getBlockTextureFromSide(p_149691_1_);
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
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		return false;
	}

}