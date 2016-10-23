package com.IceMetalPunk.redplus.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.IceMetalPunk.redplus.RedPlus;
import com.IceMetalPunk.redplus.properties.RedPlusEntityProperties;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class RedPlusCommonEventHandler {
	@SubscribeEvent
	public void handleScoreboardUpdates(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			return;
		}

		MinecraftServer mc = MinecraftServer.getServer();
		if (mc == null) {
			return;
		}

		World world = mc.getEntityWorld();
		if (world == null) {
			return;
		}

		if ((world.getTotalWorldTime() % 15) != 0) {
			return;
		}

		List entities = world.loadedEntityList;

		Entity entity = null;
		for (Object entry : entities) {
			entity = (Entity) entry;
			world = entity.worldObj;

			if (world.isRemote) {
				continue;
			}

			RedPlusEntityProperties props = RedPlusEntityProperties.get(entity);
			if (props == null) {
				return;
			}

			// Update X-positions
			Collection collection = world.getScoreboard().func_96520_a(RedPlus.scorePosX);
			Iterator iterator = collection.iterator();

			// Rotation pitch = y, yaw = x
			if (props.lastX != entity.posX) {
				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					world.getScoreboard().func_96529_a(entity.getCommandSenderName(), scoreobjective).func_96651_a(Arrays.asList(new Entity[] {
							entity }));
				}
			}

			// Update Y-positions
			if (props.lastY != entity.posY) {
				collection = world.getScoreboard().func_96520_a(RedPlus.scorePosY);
				iterator = collection.iterator();

				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					world.getScoreboard().func_96529_a(entity.getCommandSenderName(), scoreobjective).func_96651_a(Arrays.asList(new Entity[] {
							entity }));
				}
			}

			// Update Z-positions
			if (props.lastZ != entity.posZ) {
				collection = world.getScoreboard().func_96520_a(RedPlus.scorePosZ);
				iterator = collection.iterator();
				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					world.getScoreboard().func_96529_a(entity.getCommandSenderName(), scoreobjective).func_96651_a(Arrays.asList(new Entity[] {
							entity }));
				}
			}

			// Update yaw-rotations
			if (props.lastYaw != entity.rotationYaw) {
				collection = world.getScoreboard().func_96520_a(RedPlus.scoreRotationYaw);
				iterator = collection.iterator();
				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					world.getScoreboard().func_96529_a(entity.getCommandSenderName(), scoreobjective).func_96651_a(Arrays.asList(new Entity[] {
							entity }));
				}
			}

			// Update pitch-rotations
			if (props.lastPitch != entity.rotationPitch) {
				collection = world.getScoreboard().func_96520_a(RedPlus.scoreRotationPitch);
				iterator = collection.iterator();
				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					world.getScoreboard().func_96529_a(entity.getCommandSenderName(), scoreobjective).func_96651_a(Arrays.asList(new Entity[] {
							entity }));
				}
			}

			// Update the properties
			props.update();
		}
	}
}
