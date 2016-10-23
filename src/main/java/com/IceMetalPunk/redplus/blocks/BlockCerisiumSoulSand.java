package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.tileentities.TileEntityCerisiumCropland;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCerisiumSoulSand extends BlockSoulSand implements ITileEntityProvider {
	IIcon[] icons = new IIcon[2];

	public BlockCerisiumSoulSand() {
		super();
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (world.getStrongestIndirectPower(x, y, z) > 0) {
			entity.motionX *= 0.4D;
			entity.motionZ *= 0.4D;
		}
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
			IPlantable plantable) {
		EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
		return (plantType == EnumPlantType.Nether);
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
		TileEntityCerisiumCropland tileEntity = (TileEntityCerisiumCropland) world.getTileEntity(x, y, z);
		int growth = tileEntity.getGrowth();
		int maxGrowth = tileEntity.getMaxGrowth();
		int power = (15 * growth) / maxGrowth;
		// System.out.println(growth + " / " + maxGrowth + " * 15 = " + power);
		return power;
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

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCerisiumCropland();
	}

}
