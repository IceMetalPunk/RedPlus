package com.IceMetalPunk.redplus.blocks;

import com.IceMetalPunk.redplus.RedPlus;
import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispenseBlocks;
import com.IceMetalPunk.redplus.tileentities.TileEntityPlacer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class BlockPlacer extends BlockDropper {
	private final IBehaviorDispenseItem placeAction = new RedPlusDispenseBlocks();

	public BlockPlacer() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityPlacer();
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
		TileEntityPlacer tileEntity = (TileEntityPlacer) world.getTileEntity(x, y, z);
		if (tileEntity != null) {
			player.openGui(RedPlus.instance, 0, world, x, y, z);
		}
		return true;
	}

	@Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_)
    {
        return Container.calcRedstoneFromInventory((IInventory)p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
    }
	
	@Override
	protected void func_149941_e(World world, int x, int y, int z) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, x, y, z);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();

		if (tileentitydispenser != null) {
			int l = tileentitydispenser.func_146017_i();

			if (l < 0) {
				world.playAuxSFX(1001, x, y, z, 0);
			}
			else {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
				int i1 = world.getBlockMetadata(x, y, z) & 7;
				IInventory iinventory = TileEntityHopper.func_145893_b(world, (double) (x + Facing.offsetsXForSide[i1]),
						(double) (y + Facing.offsetsYForSide[i1]), (double) (z + Facing.offsetsZForSide[i1]));
				ItemStack itemstack1;

				if (iinventory != null) {
					itemstack1 = TileEntityHopper.func_145889_a(iinventory, itemstack.copy().splitStack(1),
							Facing.oppositeSide[i1]);

					if (itemstack1 == null) {
						itemstack1 = itemstack.copy();

						if (--itemstack1.stackSize == 0) {
							itemstack1 = null;
						}
					}
					else {
						itemstack1 = itemstack.copy();
					}
				}
				else {
					itemstack1 = this.placeAction.dispense(blocksourceimpl, itemstack);

					if (itemstack1 != null && itemstack1.stackSize == 0) {
						itemstack1 = null;
					}
				}

				tileentitydispenser.setInventorySlotContents(l, itemstack1);
			}
		}
	}

}
