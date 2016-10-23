/* TODO: 2.5 Items 
 * -Cerisium Alarm -- See if you can get the message for only the player with the monitor in their inventory
 * -Cerisium Piston -- Fix rendering
 */

/* Release Notes:
 * -Villagers can't open golden/diamond doors (they look specifically for wooden ones)
 */

/* FIXME: 3 Fixes Needed
 * -If possible, fix iron, gold, and diamond fence gates' rendering too-dark shadows
 * -Fix redstone bridges; their placement doesn't match their texture orientation
 * -Fix cerisium piston rendering
 */

/* Changes:
 * -Digital Clock (4 cerisium ingots, 4 iron ingots, 1 clock; outputs redstone based on time of day, linearly, with no sun needed)
 * -Cerisium Ice (3 cerisium ingots, 3 packed ice; slippery when powered only)
 * -Cerisium Infused Endstone (3 cerisium ingots, 3 ender pearls, 3 end stone; prevents endermen from teleporting while on powered ones)
 * -Cerisium Infused Soul Sand (3 cerisium ingots, 3 soul sand; only slows entities down when powered)
 * -Quartz Golem, to make quartz renewable
 * --> Summoned like iron golem, but only in Nether and with quartz blocks instead of iron blocks
 * --> Can't be summoned outside the Nether, via dispenser, or naturally spawned; no spawn egg
 * --> Hostile to nearby players over anything else
 * --> Immune to fire damage
 * --> Take 4 quartz blocks to make, drop minimum 4 quartz blocks, plus potential bonus blocks and quartz pieces
 * ----> So break even or net profit every time!
 * -Glass buttons (for secret entrances!)
 * -Glass pressure plates (player only, and for traps!)
 * -Flipglass (transparency=max redstone input)
 * -Light Sensor block light detector (output=max block light on all 6 sides)
 * -Itemizer "Prickly" rail (when powered, carts passing over it break but drop full-NBT items instead of components+contents)
 * -Iron, gold, and diamond fence gates (expected behavior; except they block light. Unfixable without updating to 1.8 or ASM coremodding; minor glitch)
 * -Diamond door (redstone signal locks it into its current state)
 * -Diamond trapdoor (redstone signal locks it into its current state)
 * -Iron trapdoor (like the 1.8 version, but in 1.7.10)
 * -Gold trapdoor (unaffected by redstone signal)
 * -Gold door (unaffected by redstone signal)
 * -SmartPlates (heavy and light; count items in stacks)
 * -Block placer
 * -Block breaker
 * -Dispenser:
 * --> Fills water bottles
 * --> Plants crops/saplings/etc.
 * -Bonemeal works on cacti and Nether wart
 * --> Player *and* dispensed
 * -Cerisium Ingot (3 redstone dust = 1 ingot, 3 ingots = 1 redstone block) 
 * 
 */

package com.IceMetalPunk.redplus;

import java.util.List;

import com.IceMetalPunk.redplus.blocks.RedPlusBlocks;
import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispenseBonemeal;
import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispenseCartOverride;
import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispensePlants;
import com.IceMetalPunk.redplus.dispenserDefs.RedPlusDispenseWaterBottles;
import com.IceMetalPunk.redplus.entity.EntityQuartzGolem;
import com.IceMetalPunk.redplus.handlers.RedPlusCommonEventHandler;
import com.IceMetalPunk.redplus.handlers.RedPlusEventHandler;
import com.IceMetalPunk.redplus.handlers.RedPlusGuiHandler;
import com.IceMetalPunk.redplus.items.RedPlusItems;
import com.IceMetalPunk.redplus.render.RenderQuartzGolem;
import com.IceMetalPunk.redplus.scoreboard.ScorePosX;
import com.IceMetalPunk.redplus.scoreboard.ScorePosY;
import com.IceMetalPunk.redplus.scoreboard.ScorePosZ;
import com.IceMetalPunk.redplus.scoreboard.ScoreRotationPitch;
import com.IceMetalPunk.redplus.scoreboard.ScoreRotationYaw;
import com.IceMetalPunk.redplus.tileentities.TileEntityBreaker;
import com.IceMetalPunk.redplus.tileentities.TileEntityCerisiumCropland;
import com.IceMetalPunk.redplus.tileentities.TileEntityDigitalClock;
import com.IceMetalPunk.redplus.tileentities.TileEntityLightSensor;
import com.IceMetalPunk.redplus.tileentities.TileEntityPlacer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = RedPlus.MODID, name = RedPlus.MODNAME, version = RedPlus.VERSION)
public class RedPlus {
	public static final String MODID = "redplus";
	public static final String VERSION = "1.0";
	public static final String MODVER = VERSION;
	public static final String MODNAME = "Red+";

	@Instance(value = RedPlus.MODID)
	public static RedPlus instance;

	/* Register dispenser behaviors for actual dispensers */
	public static final RedPlusDispensePlants plantDispenseAction = new RedPlusDispensePlants();
	public static final RedPlusDispenseWaterBottles bottleDispenseAction = new RedPlusDispenseWaterBottles();
	public static final RedPlusDispenseBonemeal bonemealDispenseAction = new RedPlusDispenseBonemeal();
	public static final RedPlusDispenseCartOverride cartOverrideDispenseAction = new RedPlusDispenseCartOverride();

	/* Register event handler */
	public static final RedPlusEventHandler eventHandler = new RedPlusEventHandler();
	public static final RedPlusCommonEventHandler commonEventHandler = new RedPlusCommonEventHandler();

	/*
	 * Instantiate renderers TODO: Fix rendering, then we'll add this in.
	 */
	// public static final RenderCerisiumPiston cerisiumPistonRenderer = new
	// RenderCerisiumPiston(RenderingRegistry.getNextAvailableRenderId());

	/* Register scoreboard objectives */
	public static final IScoreObjectiveCriteria scorePosX = new ScorePosX("posX");
	public static final IScoreObjectiveCriteria scorePosY = new ScorePosY("posY");
	public static final IScoreObjectiveCriteria scorePosZ = new ScorePosZ("posZ");
	public static final IScoreObjectiveCriteria scoreRotationYaw = new ScoreRotationYaw("rotationYaw");
	public static final IScoreObjectiveCriteria scoreRotationPitch = new ScoreRotationPitch("rotationPitch");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		/* Register items */
		RedPlusItems.cerisiumIngot.setCreativeTab(tabRedPlus).setHasSubtypes(false).setUnlocalizedName("cerisium_ingot").setTextureName("redplus:cerisium_ingot");
		GameRegistry.registerItem(RedPlusItems.cerisiumIngot, MODID + "_cerisiumIngot");
		RedPlusItems.goldDoor.setCreativeTab(tabRedPlus).setHasSubtypes(false).setUnlocalizedName("gold_door").setTextureName("redplus:gold_door");
		GameRegistry.registerItem(RedPlusItems.goldDoor, MODID + "_goldDoorItem");
		RedPlusItems.diamondDoor.setCreativeTab(tabRedPlus).setHasSubtypes(false).setUnlocalizedName("diamond_door").setTextureName("redplus:diamond_door");
		GameRegistry.registerItem(RedPlusItems.diamondDoor, MODID + "_diamondDoorItem");

		/* Register blocks */
		RedPlusBlocks.blockPlacer.setCreativeTab(tabRedPlus).setHardness(3.5F).setStepSound(Block.soundTypePiston).setBlockName("redPlusPlacer").setBlockTextureName("redplus:placer");
		GameRegistry.registerBlock(RedPlusBlocks.blockPlacer, MODID + "_placer");
		RedPlusBlocks.blockBreaker.setCreativeTab(tabRedPlus).setHardness(3.5F).setStepSound(Block.soundTypePiston).setBlockName("redPlusBreaker").setBlockTextureName("redplus:breaker");
		GameRegistry.registerBlock(RedPlusBlocks.blockBreaker, MODID + "_breaker");
		RedPlusBlocks.lightSmartPlate.setCreativeTab(tabRedPlus).setHardness(0.5F).setStepSound(Block.soundTypeWood).setBlockName("smartPlate_light");
		GameRegistry.registerBlock(RedPlusBlocks.lightSmartPlate, MODID + "_lightSmartPlate");
		RedPlusBlocks.heavySmartPlate.setCreativeTab(tabRedPlus).setHardness(0.5F).setStepSound(Block.soundTypeWood).setBlockName("smartPlate_heavy");
		GameRegistry.registerBlock(RedPlusBlocks.heavySmartPlate, MODID + "_heavySmartPlate");
		RedPlusBlocks.goldDoor.setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("gold_door").setBlockTextureName("redplus:gold_door");
		GameRegistry.registerBlock(RedPlusBlocks.goldDoor, MODID + "_goldDoor");
		RedPlusBlocks.diamondDoor.setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("diamond_door").setBlockTextureName("redplus:diamond_door");
		GameRegistry.registerBlock(RedPlusBlocks.diamondDoor, MODID + "_diamondDoor");
		RedPlusBlocks.goldTrapDoor.setCreativeTab(tabRedPlus).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("gold_trap_door").setBlockTextureName("redplus:gold_trap_door");
		GameRegistry.registerBlock(RedPlusBlocks.goldTrapDoor, MODID + "_goldTrapDoor");
		RedPlusBlocks.ironTrapDoor.setCreativeTab(CreativeTabs.tabRedstone).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("iron_trap_door").setBlockTextureName("redplus:iron_trap_door");
		GameRegistry.registerBlock(RedPlusBlocks.ironTrapDoor, MODID + "_ironTrapDoor");
		RedPlusBlocks.diamondTrapDoor.setCreativeTab(tabRedPlus).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("diamond_trap_door").setBlockTextureName("redplus:diamond_trap_door");
		GameRegistry.registerBlock(RedPlusBlocks.diamondTrapDoor, MODID + "_diamondTrapDoor");
		RedPlusBlocks.railItemizer.setCreativeTab(tabRedPlus).setHardness(0.7F).setStepSound(Block.soundTypeMetal).setBlockName("itemizer_rail").setBlockTextureName("redplus:rail_itemizer");
		GameRegistry.registerBlock(RedPlusBlocks.railItemizer, MODID + "_railItemizer");
		RedPlusBlocks.ironFenceGate.setCreativeTab(tabRedPlus).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("iron_fence_gate").setBlockTextureName("iron_block");
		GameRegistry.registerBlock(RedPlusBlocks.ironFenceGate, MODID + "_ironFenceGate");
		RedPlusBlocks.goldFenceGate.setCreativeTab(tabRedPlus).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("gold_fence_gate").setBlockTextureName("gold_block");
		GameRegistry.registerBlock(RedPlusBlocks.goldFenceGate, MODID + "_goldFenceGate");
		RedPlusBlocks.diamondFenceGate.setCreativeTab(tabRedPlus).setHardness(5.0F).setStepSound(Block.soundTypeMetal).setBlockName("diamond_fence_gate").setBlockTextureName("diamond_block");
		GameRegistry.registerBlock(RedPlusBlocks.diamondFenceGate, MODID + "_diamondFenceGate");
		RedPlusBlocks.lightSensor.setCreativeTab(tabRedPlus).setHardness(1.0F).setStepSound(Block.soundTypeGlass).setBlockName("light_sensor").setBlockTextureName("redplus:light_sensor");
		GameRegistry.registerBlock(RedPlusBlocks.lightSensor, MODID + "_lightSensor");
		RedPlusBlocks.flipGlass.setCreativeTab(tabRedPlus).setHardness(1.0F).setStepSound(Block.soundTypeGlass).setBlockName("flip_glass").setBlockTextureName("redplus:flip_glass");
		GameRegistry.registerBlock(RedPlusBlocks.flipGlass, MODID + "_flipGlass");
		RedPlusBlocks.glassPressurePlate.setCreativeTab(tabRedPlus).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("glass_pressure_plate").setBlockTextureName("redplus:glass_pressure_plate");
		GameRegistry.registerBlock(RedPlusBlocks.glassPressurePlate, MODID + "_glassPressurePlate");
		RedPlusBlocks.glassButton.setCreativeTab(tabRedPlus).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("glass_button").setBlockTextureName("redplus:glass_pressure_plate");
		GameRegistry.registerBlock(RedPlusBlocks.glassButton, MODID + "_glassButton");
		RedPlusBlocks.cerisiumSoil.setCreativeTab(tabRedPlus).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setBlockName("cerisium_soil").setBlockTextureName("redplus:cerisium_soil");
		GameRegistry.registerBlock(RedPlusBlocks.cerisiumSoil, MODID + "_cerisiumSoil");
		RedPlusBlocks.cerisiumSandstone.setCreativeTab(tabRedPlus).setStepSound(Block.soundTypePiston).setHardness(0.8F).setBlockName("cerisium_sandstone").setBlockTextureName("redplus:cerisium_sandstone");
		GameRegistry.registerBlock(RedPlusBlocks.cerisiumSandstone, MODID + "_cerisiumSandstone");
		RedPlusBlocks.cerisiumSoulSand.setCreativeTab(tabRedPlus).setHardness(0.5F).setStepSound(Block.soundTypeSand).setBlockName("cerisium_soul_sand").setBlockTextureName("redplus:cerisium_soul_sand");
		GameRegistry.registerBlock(RedPlusBlocks.cerisiumSoulSand, MODID + "_cerisiumSoulSand");
		RedPlusBlocks.cerisiumEndstone.setCreativeTab(tabRedPlus).setHardness(3.0F).setResistance(15.0F).setStepSound(Block.soundTypePiston).setBlockName("cerisium_endstone").setBlockTextureName("redplus:cerisium_endstone");
		GameRegistry.registerBlock(RedPlusBlocks.cerisiumEndstone, MODID + "_cerisiumEndstone");
		RedPlusBlocks.cerisiumIce.setCreativeTab(tabRedPlus).setHardness(0.5F).setStepSound(Block.soundTypeGlass).setBlockName("cerisium_ice").setBlockTextureName("redplus:cerisium_ice");
		GameRegistry.registerBlock(RedPlusBlocks.cerisiumIce, MODID + "_cerisiumIce");
		RedPlusBlocks.digitalClock.setCreativeTab(tabRedPlus).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal).setBlockName("digital_clock").setBlockTextureName("redplus:digital_clock");
		GameRegistry.registerBlock(RedPlusBlocks.digitalClock, MODID + "_digitalClock");
		/*
		 * RedPlusBlocks.cerisiumPiston.setCreativeTab(tabRedPlus).setHardness(3
		 * .5F).setStepSound(Block.soundTypePiston).setBlockName(
		 * "cerisium_piston").setBlockTextureName("redplus:cerisium_piston");
		 * GameRegistry.registerBlock(RedPlusBlocks.cerisiumPiston, MODID +
		 * "_cerisiumPiston");
		 */

		// TODO: Uncomment this when redstone bridge placement is fixed
		/*
		 * RedPlusBlocks.redstoneBridge.setCreativeTab(tabRedPlus).setHardness(0
		 * .0F).setStepSound(Block.soundTypeWood).setBlockName("redstone_bridge"
		 * ).setBlockTextureName("redplus:redstone_bridge");
		 * GameRegistry.registerBlock(RedPlusBlocks.redstoneBridge, MODID +
		 * "_redstoneBridge");
		 */

		/* Register tile entities */
		GameRegistry.registerTileEntity(TileEntityPlacer.class, MODID + "_Placer");
		GameRegistry.registerTileEntity(TileEntityBreaker.class, MODID + "_Breaker");
		GameRegistry.registerTileEntity(TileEntityLightSensor.class, MODID + "_LightSensor");
		GameRegistry.registerTileEntity(TileEntityDigitalClock.class, MODID + "_DigitalClock");
		GameRegistry.registerTileEntity(TileEntityCerisiumCropland.class, MODID + "_CerisiumCropland");

		/* Register entities */
		EntityRegistry.registerGlobalEntityID(EntityQuartzGolem.class, "QuartzGolem", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityQuartzGolem.class, "QuartzGolem", 0, RedPlus.instance, 64, 20, true);

		/* Register renderers */
		RenderingRegistry.registerEntityRenderingHandler(EntityQuartzGolem.class, new RenderQuartzGolem());
		// RenderingRegistry.registerBlockHandler(cerisiumPistonRenderer);

		/* Register event handler */
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(commonEventHandler);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		/* Register GUI */
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new RedPlusGuiHandler());

		/* Register recipes */
		replaceRecipe(false, new ItemStack(Items.redstone, 9), new ItemStack(RedPlusItems.cerisiumIngot, 3), new ItemStack(Blocks.redstone_block));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.redstone, 3), new ItemStack(RedPlusItems.cerisiumIngot));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.redstone_block, 1), new ItemStack(RedPlusItems.cerisiumIngot), new ItemStack(RedPlusItems.cerisiumIngot), new ItemStack(RedPlusItems.cerisiumIngot));

		GameRegistry.addShapedRecipe(new ItemStack(RedPlusItems.cerisiumIngot, 1), "RRR", 'R', Items.redstone);

		GameRegistry.addShapelessRecipe(new ItemStack(RedPlusBlocks.lightSmartPlate, 1), Blocks.light_weighted_pressure_plate, Items.comparator);
		GameRegistry.addShapelessRecipe(new ItemStack(RedPlusBlocks.heavySmartPlate, 1), Blocks.heavy_weighted_pressure_plate, Items.comparator);
		GameRegistry.addShapelessRecipe(new ItemStack(RedPlusBlocks.lightSensor, 1), Blocks.glass, Items.comparator, Blocks.redstone_lamp, Blocks.daylight_detector);
		GameRegistry.addShapelessRecipe(new ItemStack(RedPlusBlocks.glassButton, 1), Blocks.glass);

		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.glassPressurePlate, 1), "GG ", 'G', Blocks.glass);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.glassPressurePlate, 1), " GG", 'G', Blocks.glass);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.diamondTrapDoor, 1), "DDD", "DDD", 'D', Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.ironTrapDoor, 1), "III", "III", 'I', Items.iron_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.goldTrapDoor, 1), "GGG", "GGG", 'G', Items.gold_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusItems.goldDoor, 1), "GG ", "GG ", "GG ", 'G', Items.gold_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusItems.goldDoor, 1), " GG", " GG", " GG", 'G', Items.gold_ingot);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusItems.diamondDoor, 1), "DD ", "DD ", "DD ", 'D', Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusItems.diamondDoor, 1), " DD", " DD", " DD", 'D', Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.ironFenceGate, 1), "IBI", "IBI", 'I', Items.iron_ingot, 'B', Blocks.iron_block);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.goldFenceGate, 1), "IBI", "IBI", 'I', Items.gold_ingot, 'B', Blocks.gold_block);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.diamondFenceGate, 1), "DBD", "DBD", 'D', Items.diamond, 'B', Blocks.diamond_block);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.railItemizer, 1), "C C", "CRC", "C C", 'C', Blocks.cactus, 'R', Blocks.activator_rail);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.blockBreaker, 1), "ISI", "IPI", "IRI", 'I', Items.iron_ingot, 'R', Items.redstone, 'S', Blocks.sticky_piston, 'P', new ItemStack(Items.diamond_pickaxe, 1, 0));
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.blockPlacer, 1), "IPI", "IRI", "III", 'I', Items.iron_ingot, 'R', Items.redstone, 'P', Blocks.piston);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.flipGlass, 1), "RIR", "IGI", "RIR", 'R', Items.redstone, 'G', Blocks.glass, 'I', new ItemStack(Items.dye, 1, 0));
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.cerisiumSoil, 1), "CCC", "DDD", 'C', RedPlusItems.cerisiumIngot, 'D', Blocks.dirt);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.cerisiumSandstone, 1), "CCC", "SSS", 'C', RedPlusItems.cerisiumIngot, 'D', Blocks.sandstone);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.cerisiumSoulSand, 1), "CCC", "SSS", 'C', RedPlusItems.cerisiumIngot, 'S', Blocks.soul_sand);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.cerisiumEndstone, 1), "CCC", "PPP", "EEE", 'P', Items.ender_pearl, 'C', RedPlusItems.cerisiumIngot, 'E', Blocks.end_stone);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.cerisiumIce, 1), "CCC", "III", 'C', RedPlusItems.cerisiumIngot, 'I', Blocks.packed_ice);
		GameRegistry.addShapedRecipe(new ItemStack(RedPlusBlocks.digitalClock, 1), "IRI", "RCR", "IRI", 'R', RedPlusItems.cerisiumIngot, 'I', Items.iron_ingot, 'C', Items.clock);

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Just in case I need it.
	}

	@EventHandler
	public void startSever(FMLServerStartingEvent event) {
		// event.registerServerCommand(new CommandTestForBiome());
	}

	/**
	 * Replaces an existing Minecraft recipe with a custom one based on its
	 * output
	 */
	private boolean replaceRecipe(boolean shaped, ItemStack oldOutput, ItemStack newOutput, Object... newRecipe) {
		ItemStack result = null;
		List recipes = CraftingManager.getInstance().getRecipeList();
		IRecipe recipe = null;
		boolean found = false;
		for (int i = 0; i < recipes.size(); ++i) {
			recipe = (IRecipe) recipes.get(i);
			result = recipe.getRecipeOutput();
			if (ItemStack.areItemStacksEqual(result, oldOutput)) {
				recipes.remove(i);
				found = true;
				break;
			}
		}
		if (!found) {
			return false;
		}

		if (shaped) {
			GameRegistry.addShapedRecipe(newOutput, newRecipe);
		}
		else {
			GameRegistry.addShapelessRecipe(newOutput, newRecipe);
		}

		return true;
	}

	public static final CreativeTabs tabRedPlus = new CreativeTabs(CreativeTabs.getNextID(), "redPlus") {
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return RedPlusItems.cerisiumIngot;
		}
	};
}
