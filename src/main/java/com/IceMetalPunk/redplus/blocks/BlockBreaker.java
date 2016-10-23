package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.RedPlus;
import com.IceMetalPunk.redplus.tileentities.TileEntityBreaker;
import com.IceMetalPunk.redplus.tileentities.containers.BlockSlot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockBreaker extends BlockDropper {

	public BlockBreaker() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityBreaker();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("furnace_side");
		this.field_149944_M = register.registerIcon("furnace_top");
		this.field_149945_N = register.registerIcon(this.getTextureName() + "_front");
		this.field_149946_O = register.registerIcon(this.getTextureName() + "_front");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float pX, float pY,
			float pZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntityBreaker tileEntity = (TileEntityBreaker) world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			player.openGui(RedPlus.instance, 1, world, x, y, z);
		}
		return true;
	}

	@Override
	protected void func_149941_e(World world, int x, int y, int z) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, x, y, z);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();

		if (tileentitydispenser != null) {
			int front = world.getBlockMetadata(x, y, z) & 7;
			int bX = x + Facing.offsetsXForSide[front];
			int bY = y + Facing.offsetsYForSide[front];
			int bZ = z + Facing.offsetsZForSide[front];

			Block blockFacing = world.getBlock(bX, bY, bZ);
			int metaFacing = world.getBlockMetadata(bX, bY, bZ);
			Item blockItem = Item.getItemFromBlock(blockFacing);
			ItemStack blockStack = new ItemStack(blockItem, 1);
			if (blockFacing != null && BlockSlot.isValidBlock(blockStack)) {
				world.setBlockToAir(bX, bY, bZ);
				boolean foundEmpty = false;
				world.playSoundEffect((double) bX, (double) bY, (double) bZ, "redplus:breaker_grind", 0.5f, 1.0f);
				for (int slot = 0; slot < tileentitydispenser.getSizeInventory(); ++slot) {
					ItemStack stack = tileentitydispenser.getStackInSlot(slot);
					if (stack == null) {
						foundEmpty = true;
						tileentitydispenser.setInventorySlotContents(slot, new ItemStack(blockItem, 1));
						break;
					}
					else if (stack.getItem() == blockItem && stack.stackSize < stack.getMaxStackSize() && stack.stackSize < tileentitydispenser.getInventoryStackLimit()) {
						foundEmpty = true;
						++stack.stackSize;
						break;
					}
				}
				if (!foundEmpty) {
					ItemStack dropStack = new ItemStack(blockFacing.getItemDropped(metaFacing, world.rand, 0), blockFacing.quantityDropped(world.rand));
					EntityItem itemEntity = new EntityItem(world, bX + 0.5, bY + 0.5, bZ + 0.5, dropStack);
					itemEntity.motionX = 0.0;
					itemEntity.motionY = 0.0;
					itemEntity.motionZ = 0.0;
					world.spawnEntityInWorld(itemEntity);
				}
			}

		}
	}
}