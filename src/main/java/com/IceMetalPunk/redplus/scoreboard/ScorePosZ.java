package com.IceMetalPunk.redplus.scoreboard;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.util.MathHelper;

public class ScorePosZ extends ScoreDummyCriteria {

	public ScorePosZ(String p_i2312_1_) {
		super(p_i2312_1_);
	}

	public int func_96635_a(List p_96635_1_) {
		double f = 0.0F;
		Entity entityplayer;

		for (Iterator iterator = p_96635_1_.iterator(); iterator.hasNext(); f += entityplayer.posZ) {
			entityplayer = (Entity) iterator.next();
		}

		if (p_96635_1_.size() > 0) {
			f /= (double) p_96635_1_.size();
		}

		return MathHelper.ceiling_double_int(f);
	}

	public boolean isReadOnly() {
		return true;
	}
}