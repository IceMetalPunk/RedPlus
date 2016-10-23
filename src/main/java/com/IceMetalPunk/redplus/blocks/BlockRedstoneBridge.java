package com.IceMetalPunk.redplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRedstoneBridge extends BlockRedstoneDiode {

	public BlockRedstoneBridge() {
		super(false);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		world.notifyBlocksOfNeighborChange(x, y, z, this);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side) {
		World world = (World) access;
		int out1 = (world.getBlockMetadata(x, y, z));

		int out2 = ForgeDirection.VALID_DIRECTIONS[out1].getRotation(ForgeDirection.UP).ordinal();

		if (side == out1) {
			return getInputStrength(world, x, y, z, out1) - 1;
		}
		if (side == out2) {
			return getInputStrength(world, x, y, z, out2) - 1;
		}
		return 0;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 2, 2);
		}

		if (l == 1) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 5, 2);
		}

		if (l == 2) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 3, 2);
		}

		if (l == 3) {
			p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 4, 2);
		}
	}

	@Override
	public int getLightOpacity() {
		return 0;
	}

	protected int getInputStrength(World world, int x, int y, int z, int side) {
		ForgeDirection i1 = ForgeDirection.VALID_DIRECTIONS[side];
		int j1 = x + i1.offsetX;
		int k1 = z + i1.offsetZ;

		int l1 = world.getIndirectPowerLevelTo(j1, y, k1, i1.ordinal());
		return l1 >= 15	? l1
						: Math.max(l1, world.getBlock(j1, y, k1) == Blocks.redstone_wire	? world.getBlockMetadata(j1, y, k1)
																							: 0);
	}

	@Override
	protected int func_149901_b(int p_149901_1_) {
		return 0;
	}

	@Override
	protected BlockRedstoneDiode getBlockPowered() {
		return this;
	}

	@Override
	protected BlockRedstoneDiode getBlockUnpowered() {
		return this;
	}
}