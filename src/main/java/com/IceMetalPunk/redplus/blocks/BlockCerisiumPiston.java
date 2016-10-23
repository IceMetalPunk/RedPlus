package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispenseBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCerisiumPiston extends BlockDropper {

	private IIcon topIcon, innerTopIcon, bottomIcon;
	private final IBehaviorDispenseItem placeAction = new RedPlusDispenseBlocks();

	public BlockCerisiumPiston() {
		super();
	}

	@Override
	public int getRenderType() {
		return 21;
		// TODO: Uncomment this when piston rendering is fixed!
		// return RedPlus.cerisiumPistonRenderer.getRenderId();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float pX, float pY,
			float pZ) {
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return false;
	}

	@Override
	protected void func_149941_e(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		int facing = meta & 7;
		int distance = 1;
		for (distance = 1; distance <= 20; ++distance) {
			if (world.isAirBlock(x + Facing.offsetsXForSide[facing] * distance, y + Facing.offsetsYForSide[facing] * distance, z + Facing.offsetsZForSide[facing] * distance)) {
				break;
			}
			else {
				Block block = world.getBlock(x + Facing.offsetsXForSide[facing] * distance, y + Facing.offsetsYForSide[facing] * distance, z + Facing.offsetsZForSide[facing] * distance);
				if (block instanceof ITileEntityProvider) {
					return;
				}
			}
		}
		if (world.isAirBlock(x + Facing.offsetsXForSide[facing] * distance, y + Facing.offsetsYForSide[facing] * distance, z + Facing.offsetsZForSide[facing] * distance)) {
			--distance;
		}
		else {
			return;
		}

		world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
		world.setBlockMetadataWithNotify(x, y, z, meta | 8, 3);
		for (int p = distance; p >= 1; --p) {
			Block block = world.getBlock(x + Facing.offsetsXForSide[facing] * p, y + Facing.offsetsYForSide[facing] * p, z + Facing.offsetsZForSide[facing] * p);
			world.setBlockToAir(x + Facing.offsetsXForSide[facing] * p, y + Facing.offsetsYForSide[facing] * p, z + Facing.offsetsZForSide[facing] * p);
			world.setBlock(x + Facing.offsetsXForSide[facing] * (p + 1), y + Facing.offsetsYForSide[facing] * (p + 1), z + Facing.offsetsZForSide[facing] * (p + 1), block);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
		this.topIcon = p_149651_1_.registerIcon(this.getTextureName() + "_top");
		this.innerTopIcon = p_149651_1_.registerIcon(this.getTextureName() + "_inner");
		this.bottomIcon = p_149651_1_.registerIcon(this.getTextureName() + "_bottom");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		int k = (meta & 7);
		IIcon ret = (side == k	? ((meta & 8) == 0 && this.minX <= 0.0D && this.minY <= 0.0D && this.minZ <= 0.0D && this.maxX >= 1.0D && this.maxY >= 1.0D && this.maxZ >= 1.0D? this.topIcon
																																												: this.innerTopIcon)
								: (side == Facing.oppositeSide[k] ? this.bottomIcon : this.blockIcon));
		if (meta > 7) {
			System.out.print(meta + " = " + ret.getIconName());
		}
		return ret;
	}

}
