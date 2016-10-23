package com.IceMetalPunk.redplus.blocks;

import java.util.List;

import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockLightSmartPlate extends BlockBasePressurePlate {

	protected BlockLightSmartPlate() {
		super("redplus:gold_smart_plate", Material.iron);
	}

	@Override
	protected int func_150065_e(World world, int x, int y, int z) {
		int count = 0;
		List ents = world.getEntitiesWithinAABB(Entity.class, this.func_150061_a(x, y, z));
		for (int p = 0; p < ents.size(); ++p) {
			if (ents.get(p) instanceof EntityItem) {
				ItemStack stack = ((EntityItem) (ents.get(p))).getEntityItem();
				count += stack.stackSize;
			}
			else {
				++count;
			}
		}

		int l = Math.min(count, 15);
		if (l <= 0) {
			return 0;
		}
		else {
			float f = (float) l / 15.0f;
			return MathHelper.ceiling_float_int(f * 15.0F);
		}
	}

	@Override
	protected int func_150060_c(int val) {
		return val;
	}

	@Override
	protected int func_150066_d(int val) {
		return val;
	}

	@Override
	public int tickRate(World world) {
		return 10;
	}

}