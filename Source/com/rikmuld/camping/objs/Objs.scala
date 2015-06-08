package com.rikmuld.camping.objs

import scala.collection.JavaConversions.mapAsScalaMap
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.CampingMod
import com.rikmuld.camping.Lib.EntityInfo
import com.rikmuld.camping.Lib.TextureInfo
import com.rikmuld.camping.inventory.container.ContainerCampinv
import com.rikmuld.camping.inventory.gui.GuiCampinginv
import com.rikmuld.camping.inventory.objs.BackpackContainer
import com.rikmuld.camping.inventory.objs.BackpackGui
import com.rikmuld.camping.inventory.objs.KitContainer
import com.rikmuld.camping.inventory.objs.KitGui
import com.rikmuld.camping.misc.CookingEquipment
import com.rikmuld.camping.misc.Grill
import com.rikmuld.camping.misc.Pan
import com.rikmuld.camping.misc.Spit
import com.rikmuld.camping.render.models.BearModel
import com.rikmuld.camping.render.models.FoxModel
import com.rikmuld.camping.objs.entity.Bear
import com.rikmuld.camping.objs.entity.BearRenderer
import com.rikmuld.camping.objs.entity.Camper
import com.rikmuld.camping.objs.entity.CamperRender
import com.rikmuld.camping.objs.entity.Fox
import com.rikmuld.camping.objs.entity.FoxRenderer
import com.rikmuld.camping.objs.item.Kit
import com.rikmuld.camping.objs.misc.ItemsData
import com.rikmuld.camping.objs.misc.KeyData
import com.rikmuld.camping.objs.misc.MapData
import com.rikmuld.camping.objs.misc.NBTPlayer
import com.rikmuld.camping.objs.misc.OpenGui
import com.rikmuld.camping.objs.tile.TileLantern
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.corerm.CoreUtils
import com.rikmuld.corerm.RMMod
import com.rikmuld.corerm.misc.ModRegister
import com.rikmuld.corerm.misc.Rotation
import com.rikmuld.corerm.objs.ObjInfo
import com.rikmuld.corerm.objs.Properties._
import com.rikmuld.corerm.objs.RMCoreBlock
import com.rikmuld.corerm.objs.RMCoreItem
import com.rikmuld.corerm.objs.RMItem
import com.rikmuld.corerm.objs.RMItemArmor
import com.rikmuld.corerm.objs.RMItemFood
import net.minecraft.block.material.Material
import net.minecraft.client.model.ModelBiped
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityList.EntityEggInfo
import net.minecraft.entity.EnumCreatureType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks._
import net.minecraft.init.Items._
import net.minecraft.item.ItemArmor.ArmorMaterial
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.BiomeDictionary.Type
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import com.rikmuld.camping.objs.block.Lantern
import com.rikmuld.camping.objs.block.LanternItem
import com.rikmuld.corerm.objs.RMBlock
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.camping.objs.block.Logseat
import net.minecraft.block.Block
import com.rikmuld.camping.objs.entity.Mountable
import com.rikmuld.camping.objs.misc.PlayerExitLog
import com.rikmuld.camping.objs.tile.TileLogseat
import com.rikmuld.corerm.objs.RMBlockContainer
import net.minecraft.util.AxisAlignedBB
import com.rikmuld.corerm.objs.RMTile
import com.rikmuld.camping.objs.tile.TileLight
import net.minecraft.dispenser.IBlockSource
import net.minecraft.block.state.IBlockState
import com.rikmuld.corerm.objs.WithModel
import net.minecraft.init.Blocks
import com.rikmuld.camping.objs.block.SleepingBag
import com.rikmuld.camping.objs.block.SleepingBagItem
import com.rikmuld.camping.world.WorldGenerator
import com.rikmuld.camping.objs.block.Trap
import com.rikmuld.camping.inventory.objs.ContainerTrap
import com.rikmuld.camping.inventory.objs.GuiTrap
import net.minecraft.util.DamageSource
import net.minecraft.potion.Potion
import com.rikmuld.camping.objs.misc.DamageSourceBleeding
import com.rikmuld.camping.objs.misc.PotionBleeding
import com.rikmuld.camping.Lib.DamageInfo
import com.rikmuld.camping.Lib.PotionInfo
import com.rikmuld.camping.objs.tile.TileTrap
import net.minecraftforge.fml.client.registry.ClientRegistry
import com.rikmuld.camping.render.objs.TrapRender
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraft.item.Item
import com.rikmuld.camping.objs.block.CampfireCook
import com.rikmuld.camping.objs.block.Campfire
import com.rikmuld.camping.inventory.objs.ContainerCampfire
import com.rikmuld.camping.inventory.objs.ContainerCampfireCook
import com.rikmuld.camping.inventory.objs.GuiCampfireCook
import com.rikmuld.camping.inventory.objs.GuiCampfire
import com.rikmuld.camping.objs.tile.TileCampfire
import com.rikmuld.camping.objs.tile.TileCampfireCook
import com.rikmuld.camping.render.objs.CampfireRender
import com.rikmuld.camping.render.objs.CampfireCookRender
import com.rikmuld.camping.objs.block.Tent
import com.rikmuld.corerm.objs.PropType
import net.minecraft.init.Items
import com.rikmuld.camping.objs.tile.TileTent
import com.rikmuld.camping.render.objs.TentRender
import com.rikmuld.camping.inventory.objs.GuiTent
import com.rikmuld.camping.inventory.objs.GuiTentSleeping
import com.rikmuld.camping.inventory.objs.ContainerTentChests
import com.rikmuld.camping.inventory.objs.ContainerTentLanterns
import com.rikmuld.camping.inventory.objs.GuiTentChests
import com.rikmuld.camping.inventory.objs.GuiTentLanterns
import com.rikmuld.corerm.bounds.TileBounds
import com.rikmuld.corerm.bounds.BoundsData
import com.rikmuld.corerm.bounds.BoundsStructure
import com.rikmuld.camping.objs.block.TentBounds
import com.rikmuld.camping.objs.misc.PlayerSleepInTent

object Objs {
  var tab:CreativeTabs = _
  var fur: ArmorMaterial = _
  var knife, parts, backpack, kit, marshmallow, animalParts, furBoot, furLeg, furChest, furHead, venisonCooked, venisonRaw:RMCoreItem = _
  var hemp, lantern, tent, logseat, light, sleepingBag, trap, campfire, campfireCook, tentBounds:RMCoreBlock = _
  var guiBackpack, guiKit, guiCamping, guiTrap, guiCampfire, guiCampfireCook, guiTentSleep, guiTentChests, guiTentLantern, guiTent:Int = _
  var spit, grill, pan:CookingEquipment = _
  var bleeding: Potion = _
  var bleedingSource: DamageSource = _
  var tentStructure:Array[BoundsStructure] = _
  
  object ModItems extends ModRegister {
    import com.rikmuld.camping.objs.Objs.ModItems.MetaLookup._

    val KNIFE = new ObjInfo(Tab(tab), MaxDamage(config.toolUse), MaxStacksize(1), Name("knife"))
    val PARTS = new ObjInfo(Tab(tab), Name("parts"), Metadata("canvas", "stickIron", "pan", "ash", "marshmallows", "marshmallowStickRaw"))
    val BACKPACK = new ObjInfo(Tab(tab), MaxStacksize(1), Name("backpack"), ForceSubtype(true), GuiTrigger(guiBackpack))
    val KIT = new ObjInfo(Tab(tab), MaxStacksize(1), Name("kit"), ForceSubtype(true), GuiTrigger(guiKit), Metadata("kit", "spitKit", "grillKit", "panKit", "uselessKit"))
    val MARSHMALLOW = new ObjInfo(Tab(tab), Name("marshmallowStickCooked"), WolfMeat(false), Heal(config.marshHeal), Saturation(config.marshSaturation))
    val PARTS_ANIMAL = new ObjInfo(Tab(tab), Name("partsAnimal"), Metadata("furWhite", "furBrown"))
    val FUR_BOOT = new ObjInfo(Tab(tab), Name("armorFurBoots"), ArmorTyp(3), ArmorMateria(fur), ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
    val FUR_LEG = new ObjInfo(Tab(tab), Name("armorFurLeg"), ArmorTyp(2), ArmorMateria(fur), ArmorTexture(TextureInfo.ARMOR_FUR_LEG))
    val FUR_CHEST = new ObjInfo(Tab(tab), Name("armorFurChest"), ArmorTyp(1), ArmorMateria(fur), ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
    val FUR_HEAD = new ObjInfo(Tab(tab), Name("armorFurHelm"), ArmorTyp(0), ArmorMateria(fur), ArmorTexture(TextureInfo.ARMOR_FUR_MAIN))
    val VENISON_RAW = new ObjInfo(Tab(tab), Name("venisonRaw"), WolfMeat(true), Heal(config.venisonRawHeal), Saturation(config.venisonRawSaturation))
    val VENISON_COOKED = new ObjInfo(Tab(tab), Name("venisonCooked"), WolfMeat(true), Heal(config.venisonHeal), Saturation(config.venisonSaturation))

    override def register {      
      knife = new RMItem(MOD_ID, KNIFE){
        override def onItemUse(item: ItemStack, player: EntityPlayer, world: World, pos:BlockPos, sideHit: EnumFacing, xHit: Float, yHit: Float, zHit: Float): Boolean = {
          !world.isRemote && Rotation.rotateBlock(world, pos)
        }
      }
      parts = new RMItem(MOD_ID, PARTS)
      marshmallow = new RMItemFood(MOD_ID, MARSHMALLOW){
          override def onFoodEaten(stack: ItemStack, world: World, player: EntityPlayer) {
          if (!world.isRemote && (!player.inventory.addItemStackToInventory(nwsk(Objs.parts, 1, Parts.STICK_IRON)))) {
            player.dropPlayerItemWithRandomChoice(nwsk(Objs.parts, 1, Parts.STICK_IRON), false)
          }
        }
      }
      backpack = new RMItem(MOD_ID, BACKPACK)
      kit = new Kit(MOD_ID, KIT)
      
      if(!config.coreOnly){
        animalParts = new RMItem(MOD_ID, PARTS_ANIMAL)
        venisonRaw = new RMItemFood(MOD_ID, VENISON_RAW)
        venisonCooked = new RMItemFood(MOD_ID, VENISON_COOKED)
        furBoot = new RMItemArmor(MOD_ID, FUR_BOOT)
        furLeg = new RMItemArmor(MOD_ID, FUR_LEG)
        furChest = new RMItemArmor(MOD_ID, FUR_CHEST)
        furHead = new RMItemArmor(MOD_ID, FUR_HEAD)
      }
    }

    object MetaLookup {
      object Parts {
        final val CANVAS = 0
        final val STICK_IRON = 1
        final val PAN = 2
        final val ASH = 3
        final val MARSHMALLOW = 4
        final val MARSHMALLOWSTICK = 5
      }
      object Kit {
        final val EMPTY = 0
        final val SPIT = 1
        final val GRILL = 2
        final val PAN = 3
        final val USELESS = 4
      }
      object PartsAnimal {
        final val FUR_WHITE = 0
        final val FUR_BROWN = 1
      }
    }
  }
    
  object ModBlocks extends ModRegister {
    final val LANTERN = new ObjInfo(Tab(tab), Name("lantern"), Materia(Material.glass), Metadata("lanternOff", "lanternOn"), MaxStacksize(1), Hardness(.5f), LightOpacity(255), ItemBl(classOf[LanternItem]))
    final val HEMP = new ObjInfo(Tab(tab), Name("hemp"), Materia(Material.plants), Metadata("plantHemp_0", "plantHemp_1", "plantHemp_2", "plantHemp_3", "plantHemp_4", "plantHemp_5"))
    final val LOGSEAT = new ObjInfo(Tab(tab), Name("logseat"), Materia(Material.wood), StepSound(Block.soundTypeWood), Hardness(2.0f), HarvestLevel("axe", 0))
    final val LIGHT = new ObjInfo(Name("light"), Materia(Material.air), LightLevel(1.0f))
    final val SLEEPING_BAG = new ObjInfo(Tab(tab), Name("sleepingBag"), Materia(Material.cloth), Hardness(0.1f), MaxStacksize(4), ItemBl(classOf[SleepingBagItem]))
    final val TRAP = new ObjInfo(Tab(tab), Name("trap"), Materia(Material.iron), Hardness(1f), GuiTrigger(guiTrap))
    final val CAMPFIRE = new ObjInfo(Tab(tab), Name("campfire"), Materia(Material.fire), Hardness(2f), StepSound(Block.soundTypeStone), LightLevel(1.0f), GuiTrigger(guiCampfire))
    final val CAMPFIRE_COOK = new ObjInfo(Tab(tab), Name("campfireCook"), Materia(Material.fire), Hardness(2f), StepSound(Block.soundTypeStone), LightLevel(1.0f), GuiTrigger(guiCampfireCook))
    final val TENT = new ObjInfo(Tab(tab), Name("tent"), Materia(Material.cloth), Hardness(0.2f), GuiTrigger(guiTent))
    final val BOUNDS_TENT = new ObjInfo(Name("tentBounds"), Materia(Material.cloth))

    override def register {
      campfireCook = new CampfireCook(MOD_ID, CAMPFIRE_COOK)
      campfire = new Campfire(MOD_ID, CAMPFIRE)
      sleepingBag = new SleepingBag(MOD_ID, SLEEPING_BAG)
      tent = new Tent(MOD_ID, TENT)
      logseat = new Logseat(MOD_ID, LOGSEAT)
      lantern = new Lantern(MOD_ID, LANTERN)
      if(!config.coreOnly) trap = new Trap(MOD_ID, TRAP) 
      tentBounds = new TentBounds(MOD_ID, BOUNDS_TENT)
      hemp = new Hemp(MOD_ID, HEMP)
      light = new RMBlockContainer(MOD_ID, LIGHT) with WithModel {
        setBlockBounds(0, 0, 0, 0, 0, 0)
        
        override def createNewTileEntity(world: World, meta: Int): RMTile = new TileLight()
        override def getCollisionBoundingBox(world: World, pos:BlockPos, state:IBlockState): AxisAlignedBB = null
        override def getRenderType = -1
        override def isReplaceable(world: World, pos:BlockPos) = true
        override def canCollideCheck(state:IBlockState, hitIfLiquid:Boolean) = false
      }
    }
       
    override def registerClient {
      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTrap], new TrapRender)
      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfire], new CampfireRender)
      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCampfireCook], new CampfireCookRender)
      ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileTent], new TentRender)
    }
      
    object MetaLookup {
      object Lantern {
        final val OFF = 0
        final val ON = 1
      }
      object Hemp {
        final val INFANT_1 = 0
        final val INFANT_2 = 1
        final val INFANT_3 = 2
        final val GROWN_SMALL = 3
        final val GROWN_BIG_BOTTOM = 4
        final val GROWN_BIG_TOP = 5
      }
    }
  }
  
  object ModMisc extends ModRegister {
    import com.rikmuld.camping.objs.Objs.ModItems.MetaLookup._

    override def register {
      if(phase==ModRegister.PERI){
        val xLine = Array(1, -1, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0, 1, -1, 0)
        val yLine = Array(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        val zLine = Array(0, 0, 1, 1, 1, 2, 2, 2, 0, 0, 0, 1, 1, 1, 2, 2, 2)
        tentStructure = BoundsStructure.regsisterStructure(xLine, yLine, zLine, true)
      
        tab = new com.rikmuld.camping.objs.misc.Tab(MOD_ID)
        fur = EnumHelper.addArmorMaterial("FUR", "", 20, Array(2, 5, 4, 2), 20)
        
        RMMod.registerPacket(classOf[OpenGui])
        RMMod.registerPacket(classOf[NBTPlayer])
        RMMod.registerPacket(classOf[KeyData])
        RMMod.registerPacket(classOf[ItemsData])
        RMMod.registerPacket(classOf[MapData])
        RMMod.registerPacket(classOf[PlayerExitLog])  
        RMMod.registerPacket(classOf[BoundsData])
        RMMod.registerPacket(classOf[PlayerSleepInTent])
        
        GameRegistry.registerWorldGenerator(new WorldGenerator(), 9999) 
        
        if(!config.coreOnly){
          bleedingSource = new DamageSourceBleeding(DamageInfo.BLEEDING)
          bleeding = new PotionBleeding(PotionInfo.BLEEDING)
        }
      } else if(phase==ModRegister.POST){
        grill = new Grill(nwsk(kit, Kit.GRILL))
        spit = new Spit(nwsk(kit, Kit.SPIT))
        pan = new Pan(nwsk(kit, Kit.PAN))
        
        val ironStick = nwsk(parts, Parts.STICK_IRON)
        
        CookingEquipment.addEquipmentRecipe(Objs.spit, nwsk(stick), nwsk(stick), ironStick)
        CookingEquipment.addEquipmentRecipe(Objs.grill, nwsk(stick), nwsk(stick), nwsk(stick), nwsk(stick), ironStick, ironStick, nwsk(iron_bars))
        CookingEquipment.addEquipmentRecipe(Objs.pan, nwsk(stick), nwsk(stick), ironStick, nwsk(string), nwsk(Objs.parts, 1, Parts.PAN))
        
        grill.addFood(nwsk(fish), nwsk(cooked_fish))
        grill.addFood(nwsk(fish, 1), nwsk(cooked_fish, 1))
        grill.addFood(nwsk(beef), nwsk(cooked_beef))
        grill.addFood(nwsk(porkchop), nwsk(cooked_porkchop))
        grill.addFood(nwsk(venisonRaw), nwsk(venisonCooked))
        grill.addFood(nwsk(mutton), nwsk(cooked_mutton))
        pan.addFood(nwsk(potato), nwsk(baked_potato))
        pan.addFood(nwsk(rotten_flesh), nwsk(leather))
        spit.addFood(nwsk(chicken), nwsk(cooked_chicken))
        spit.addFood(nwsk(rabbit), nwsk(cooked_rabbit))
        spit.addFood(nwsk(fish), nwsk(cooked_fish))
        spit.addFood(nwsk(fish, 1), nwsk(cooked_fish, 1))
      }
    }
  }
  
  object ModTiles extends ModRegister {
    override def register {
      GameRegistry.registerTileEntity(classOf[TileLantern], MOD_ID + "_tileLantern")
      GameRegistry.registerTileEntity(classOf[TileLogseat], MOD_ID + "_tileLogseat")
      GameRegistry.registerTileEntity(classOf[TileLight], MOD_ID + "_tileLight")
      GameRegistry.registerTileEntity(classOf[TileTrap], MOD_ID + "_tileTrap")
      GameRegistry.registerTileEntity(classOf[TileCampfire], MOD_ID + "_tileCampfire")
      GameRegistry.registerTileEntity(classOf[TileCampfireCook], MOD_ID + "_tileCampfireCook")
      GameRegistry.registerTileEntity(classOf[TileBounds], MOD_ID + "_tileBounds")
      GameRegistry.registerTileEntity(classOf[TileTent], MOD_ID + "_tileTent")
    }
  }

  object ModGuis extends ModRegister{
    override def registerServer {
      guiBackpack = RMMod.proxy.registerGui(classOf[BackpackContainer], null)
      guiKit = RMMod.proxy.registerGui(classOf[KitContainer], null)
      guiCamping = RMMod.proxy.registerGui(classOf[ContainerCampinv], null)
      guiTrap = RMMod.proxy.registerGui(classOf[ContainerTrap], null)
      guiCampfire = RMMod.proxy.registerGui(classOf[ContainerCampfire], null)
      guiCampfireCook = RMMod.proxy.registerGui(classOf[ContainerCampfireCook], null)
      guiTent = RMMod.proxy.registerGui(null, null)
      guiTentSleep = RMMod.proxy.registerGui(null, null)
      guiTentChests = RMMod.proxy.registerGui(classOf[ContainerTentChests], null)
      guiTentLantern = RMMod.proxy.registerGui(classOf[ContainerTentLanterns], null)
    }
    override def registerClient {
      guiBackpack = RMMod.proxy.registerGui(classOf[BackpackContainer], classOf[BackpackGui])
      guiKit = RMMod.proxy.registerGui(classOf[KitContainer], classOf[KitGui])
      guiCamping = RMMod.proxy.registerGui(classOf[ContainerCampinv], classOf[GuiCampinginv])
      guiTrap = RMMod.proxy.registerGui(classOf[ContainerTrap], classOf[GuiTrap])
      guiCampfire = RMMod.proxy.registerGui(classOf[ContainerCampfire], classOf[GuiCampfire])
      guiCampfireCook = RMMod.proxy.registerGui(classOf[ContainerCampfireCook], classOf[GuiCampfireCook])
      guiTent = RMMod.proxy.registerGui(null, classOf[GuiTent])
      guiTentSleep = RMMod.proxy.registerGui(null, classOf[GuiTentSleeping])
      guiTentChests = RMMod.proxy.registerGui(classOf[ContainerTentChests], classOf[GuiTentChests])
      guiTentLantern = RMMod.proxy.registerGui(classOf[ContainerTentLanterns], classOf[GuiTentLanterns])
    }
  }
  
  object ModEntities extends ModRegister{
    override def register {
      if(phase==ModRegister.PERI){
        if(config.coreOnly==false){
          registerEntity(classOf[Bear].asInstanceOf[Class[Entity]], "bearGrizzly", EntityInfo.BEAR, true, 0x583B2D, 0xE2B572)
          registerEntity(classOf[Fox].asInstanceOf[Class[Entity]], "foxArctic", EntityInfo.FOX, true, 0xE0EEEE, 0x362819)
          registerEntity(classOf[Camper].asInstanceOf[Class[Entity]], "camper", EntityInfo.CAMPER, true, 0x747B51, 0x70471B)
        }
      } else if(phase==ModRegister.POST&&config.coreOnly==false){
        val forests = BiomeDictionary.getBiomesForType(Type.FOREST)
        val rivers = BiomeDictionary.getBiomesForType(Type.RIVER)
        val snow = BiomeDictionary.getBiomesForType(Type.SNOWY)
  
        if (config.useBears) {
          for (biome <- forests) EntityRegistry.addSpawn(classOf[Bear], 3, 2, 3, EnumCreatureType.CREATURE, biome)
          for (biome <- rivers) EntityRegistry.addSpawn(classOf[Bear], 4, 2, 4, EnumCreatureType.CREATURE, biome)
        }
        if (config.useFoxes) for (biome <- snow) EntityRegistry.addSpawn(classOf[Fox], 5, 2, 4, EnumCreatureType.CREATURE, biome)
      }
    }
    override def registerClient {
      if(!config.coreOnly){
        RenderingRegistry.registerEntityRenderingHandler(classOf[Bear], new BearRenderer(new BearModel))
        RenderingRegistry.registerEntityRenderingHandler(classOf[Fox], new FoxRenderer(new FoxModel))
        RenderingRegistry.registerEntityRenderingHandler(classOf[Camper], new CamperRender(new ModelBiped))
      }
    }
    private def registerEntity(entity: Class[Entity], name: String, id: Int, egg: Boolean, colour1: Int, colour2: Int) {
      EntityRegistry.registerModEntity(entity, name, id, CampingMod, 80, 3, false);
      if (egg) {
        val id2 = CoreUtils.getUniqueEntityId
        EntityList.idToClassMapping.asInstanceOf[java.util.Map[Int, Class[Entity]]](id2) = entity;
        EntityList.entityEggs.asInstanceOf[java.util.Map[Int, EntityEggInfo]](id2) = new EntityEggInfo(id2, colour1, colour2);
        Objs.tab.asInstanceOf[com.rikmuld.camping.objs.misc.Tab].eggIds.append(id2)
      }
    }
  }
  
  object ModRecipes extends ModRegister {
    import com.rikmuld.camping.objs.Objs.ModBlocks.MetaLookup._
    import com.rikmuld.camping.objs.Objs.ModItems.MetaLookup._

    override def register {
      val dye = Items.dye.getMetaCycle(16)
      val prts = parts.getMetaCycle(parts.getItemInfo.getValue(PropType.METADATA).asInstanceOf[Array[_]].length)
      val lanterns = lantern.getMetaCycle(2)
  
      GameRegistry.addRecipe(nwsk(backpack), "000", "0 0", "000", '0': Character, prts(Parts.CANVAS))
      GameRegistry.addRecipe(nwsk(knife), "010", "010", "010", '0': Character, dye(1), '1': Character, Items.iron_ingot)
      GameRegistry.addRecipe(nwsk(campfireCook), " 0 ", "0 0", " 0 ", '0': Character, Blocks.cobblestone)
      GameRegistry.addRecipe(nwsk(campfire), " 0 ", "010", "020", '0': Character, Items.stick, '1': Character, Items.flint, '2': Character, campfireCook)
      GameRegistry.addRecipe(nwsk(kit), "000", "111", "000", '0': Character, Items.iron_ingot, '1': Character, dye(11))
      GameRegistry.addRecipe(lanterns(ModBlocks.MetaLookup.Lantern.ON), "000", "010", "222", '1': Character, Items.glowstone_dust, '0': Character, Blocks.glass_pane, '2': Character, Items.gold_ingot)
      GameRegistry.addRecipe(lanterns(ModBlocks.MetaLookup.Lantern.OFF), "000", "0 0", "111", '1': Character, Items.gold_ingot, '0': Character, Blocks.glass_pane)
      GameRegistry.addRecipe(prts(Parts.STICK_IRON), "0", "0", '0': Character, Items.iron_ingot)
      GameRegistry.addRecipe(prts(Parts.PAN), " 0 ", "121", " 1 ", '0': Character, dye(1), '1': Character, Items.iron_ingot, '2': Character, Items.bowl)
      GameRegistry.addRecipe(prts(Parts.MARSHMALLOW).toStack(3), "010", "020", "030", '0': Character, Items.sugar, '1': Character, Items.potionitem, '2': Character, Items.egg, '3': Character, Items.bowl)
      GameRegistry.addShapelessRecipe(nwsk(log), nwsk(Blocks.log).getWildValue, nwsk(knife).getWildValue)
      GameRegistry.addShapelessRecipe(nwsk(log), nwsk(Blocks.log2).getWildValue, nwsk(knife).getWildValue)
      GameRegistry.addShapelessRecipe(lanterns(ModBlocks.MetaLookup.Lantern.ON), lanterns(ModBlocks.MetaLookup.Lantern.OFF), Items.glowstone_dust)
      GameRegistry.addShapelessRecipe(lanterns(ModBlocks.MetaLookup.Lantern.ON), lanterns(ModBlocks.MetaLookup.Lantern.ON), Items.glowstone_dust)
      GameRegistry.addShapelessRecipe(prts(Parts.MARSHMALLOWSTICK).toStack(3), prts(Parts.MARSHMALLOW), prts(Parts.STICK_IRON), prts(Parts.STICK_IRON), prts(Parts.STICK_IRON))
      GameRegistry.addRecipe(nwsk(tent), "000", "0 0", "1 1", '0': Character, prts(Parts.CANVAS), '1': Character, prts(Parts.STICK_IRON));
      GameRegistry.addRecipe(nwsk(sleepingBag), "1  ", "000", '0': Character, nwsk(Blocks.wool).getWildValue, '1': Character, nwsk(knife).getWildValue)
      GameRegistry.addRecipe(nwsk(sleepingBag), " 1 ", "000", '0': Character, nwsk(Blocks.wool).getWildValue, '1': Character, nwsk(knife).getWildValue)
      GameRegistry.addRecipe(nwsk(sleepingBag), "  1", "000", '0': Character, nwsk(Blocks.wool).getWildValue, '1': Character, nwsk(knife).getWildValue)
      GameRegistry.addShapelessRecipe(prts(Parts.CANVAS), hemp, nwsk(knife).getWildValue)
      GameRegistry.addShapelessRecipe(nwsk(tent), tent, nwsk(Items.dye).getWildValue)
  
      if (!config.coreOnly) {
        val partsAnimal = animalParts.getMetaCycle(animalParts.getItemInfo.getValue(PropType.METADATA).asInstanceOf[Array[_]].length)
  
        GameRegistry.addSmelting(nwsk(venisonRaw), nwsk(venisonCooked), 3)
        GameRegistry.addRecipe(nwsk(trap), " 1 ", "101", " 1 ", '0': Character, Items.iron_ingot, '1': Character, prts(Parts.STICK_IRON))
        GameRegistry.addRecipe(nwsk(furHead), "000", "010", '0': Character, partsAnimal(PartsAnimal.FUR_WHITE), '1': Character, Items.leather_helmet)
        GameRegistry.addRecipe(nwsk(furChest), "0 0", "010", "222", '0': Character, partsAnimal(PartsAnimal.FUR_WHITE), '1': Character, Items.leather_chestplate, '2': Character, partsAnimal(PartsAnimal.FUR_BROWN))
        GameRegistry.addRecipe(nwsk(furLeg), "000", "010", "0 0", '0': Character, partsAnimal(PartsAnimal.FUR_BROWN), '1': Character, Items.leather_leggings)
        GameRegistry.addRecipe(nwsk(furBoot), "0 0", "010", '0': Character, partsAnimal(PartsAnimal.FUR_BROWN), '1': Character, Items.leather_boots)
      }
    }
  }
}