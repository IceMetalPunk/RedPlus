package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.tileentities.TileEntityCerisiumCropland;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCerisiumSoil extends Block implements ITileEntityProvider {

	protected BlockCerisiumSoil() {
		super(Material.ground);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
		TileEntityCerisiumCropland tileEntity = (TileEntityCerisiumCropland) world.getTileEntity(x, y, z);
		int growth = tileEntity.getGrowth();
		int maxGrowth = tileEntity.getMaxGrowth();
		int power = (15 * growth) / maxGrowth;
		return power;
	}

	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
			IPlantable plantable) {
		Block plant = plantable.getPlant(world, x, y + 1, z);
		EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);

		if (plantable instanceof BlockBush || plantType == EnumPlantType.Crop || plantType == EnumPlantType.Plains) {
			return true;
		}
		else if (plantType == EnumPlantType.Beach) {
			boolean hasWater = (world.getBlock(x - 1, y, z).getMaterial() == Material.water || world.getBlock(x + 1, y, z).getMaterial() == Material.water || world.getBlock(x, y, z - 1).getMaterial() == Material.water || world.getBlock(x, y, z + 1).getMaterial() == Material.water);
			return hasWater;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCerisiumCropland();
	}

}
