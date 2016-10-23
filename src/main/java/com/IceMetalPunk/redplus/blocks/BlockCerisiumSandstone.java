package com.IceMetalPunk.redplus.blocks;

import java.util.List;

import com.IceMetalPunk.redplus.tileentities.TileEntityCerisiumCropland;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCerisiumSandstone extends BlockSandStone implements ITileEntityProvider {

	IIcon[] icons = new IIcon[3];

	protected BlockCerisiumSandstone() {
		super();
	}

	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
			IPlantable plantable) {
		Block plant = plantable.getPlant(world, x, y + 1, z);
		EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);

		if (plantType == EnumPlantType.Desert) {
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

	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		return true;
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
	public IIcon getIcon(int side, int meta) {
		if (side > 2) {
			side = 2;
		}
		return this.icons[side];
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		// No sub-blocks, unlike our parent sandstone, so don't add any!
		list.add(new ItemStack(item, 1, 0));
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.icons[0] = register.registerIcon(this.getTextureName() + "_bottom");
		this.icons[1] = register.registerIcon(this.getTextureName() + "_top");
		this.icons[2] = register.registerIcon(this.getTextureName() + "_side");
	}

}
