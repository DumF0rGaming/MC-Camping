package com.rikmuld.camping

import java.util.ArrayList
import java.util.Random
import java.util.UUID
import scala.collection.JavaConversions._
import com.rikmuld.camping.CampingMod._
import com.rikmuld.camping.Lib._
import com.rikmuld.camping.Utils._
import com.rikmuld.corerm.CoreUtils._
import com.rikmuld.camping.inventory.container.InventoryCampinv
import com.rikmuld.camping.inventory.gui.ButtonItem
import com.rikmuld.camping.inventory.gui.GuiMapHUD
import com.rikmuld.camping.objs.BlockDefinitions._
import com.rikmuld.camping.objs.ItemDefinitions._
import com.rikmuld.camping.objs.Objs
import com.rikmuld.camping.objs.block.Hemp
import com.rikmuld.camping.objs.block.Tent
import com.rikmuld.camping.objs.entity.Mountable
import com.rikmuld.camping.objs.misc.KeyData
import com.rikmuld.camping.objs.misc.MapData
import com.rikmuld.camping.objs.misc.NBTPlayer
import com.rikmuld.camping.objs.misc.OpenGui
import com.rikmuld.camping.objs.tile.Roaster
import com.rikmuld.camping.objs.tile.TileTrap
import com.rikmuld.corerm.RMMod
import com.rikmuld.corerm.misc.WorldBlock._
import com.rikmuld.corerm.network.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import net.minecraft.util.math.Vec3d

class EventsS {
  var tickLight: Int = 0
  var marshupdate: Float = 0
  val UUIDSpeedCamping = new UUID(new Random(83746763).nextLong, new Random(28647556).nextLong)

  @SubscribeEvent
  def onBoneMealUsed(event: BonemealEvent) {
    if (event.getBlock.getBlock == Objs.hemp) {
      event.setResult(
          if (event.getBlock.getBlock.asInstanceOf[Hemp].grow((event.getWorld, event.getPos))) 
            Event.Result.ALLOW 
          else Event.Result.DENY
          )
    }
  }
  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
    if (eventArgs.getModID.equals(MOD_ID)) config.sync
  }
  @SubscribeEvent
  def onPlayerDeath(event: PlayerDropsEvent) {
    if (!config.coreOnly) InventoryCampinv.dropItems(event.getEntityPlayer)
  }
  @SubscribeEvent
  def onEntityDeath(event: LivingDeathEvent) {
    if (event.getEntity.isInstanceOf[EntityPlayer]) {
      if (event.getEntity.worldObj.getGameRules().getBoolean("keepInventory")) {
        val tag = event.getEntity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag("campInv")
        if (!event.getEntity.asInstanceOf[EntityPlayer].getEntityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) event.getEntity.asInstanceOf[EntityPlayer].getEntityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound())
        event.getEntity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setTag("campInv", tag)
      }
    }
  }
  @SubscribeEvent
  def onPlayerRespawn(event: PlayerRespawnEvent) {
    if (!config.coreOnly) {
      if (event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("campInv")) {
        event.player.getEntityData.setTag("campInv", event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("campInv"))
        event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).removeTag("campInv")
      }
    }
  }
  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent) = if (!config.coreOnly) Option(event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)) map (tag => PacketSender.to(new NBTPlayer(tag), event.player.asInstanceOf[EntityPlayerMP]))
  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent) {
    if(event.crafting.getItem == Objs.knife)event.player.addStat(Objs.achKnife)
    for (slot <- 0 until event.craftMatrix.getSizeInventory if Option(event.craftMatrix.getStackInSlot(slot)).isDefined) {
      val stackInSlot = event.craftMatrix.getStackInSlot(slot)
      val itemInSlot = Option(stackInSlot.getItem)
      if (itemInSlot.isDefined && itemInSlot.get.eq(Objs.knife)) {
        val returnStack = Option(stackInSlot.addDamage(1))
        returnStack map (stack => stack.stackSize += 1)
        event.craftMatrix.setInventorySlotContents(slot, returnStack.getOrElse(null))
      } else if (itemInSlot.isDefined && itemInSlot.get == Items.BOWL && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == Parts.MARSHMALLOW)) {
        val returnStack = new ItemStack(Items.BOWL, stackInSlot.stackSize + 1, 0)
        event.craftMatrix.setInventorySlotContents(slot, returnStack)
      } else if (itemInSlot.isDefined && itemInSlot.get == Items.POTIONITEM && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == Parts.MARSHMALLOW)) {
        if (!event.player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) 
          event.player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false)
      }
      if ((event.crafting.getItem() == Item.getItemFromBlock(Objs.lantern)) && (event.crafting.getItemDamage == Lantern.ON)) {
        event.crafting.setTagCompound(new NBTTagCompound())
        event.crafting.getTagCompound.setInteger("time", 1500)
      }
    }
  }
  def keyPressedServer(player: EntityPlayer, id: Int) {
    if(id==KeyInfo.INVENTORY_KEY)player.openGui(RMMod, Objs.guiCamping, player.worldObj, 0, 0, 0)
  }
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent) {
    val player = event.player
    val world = player.worldObj
    Objs.tent.asInstanceOf[Tent].facingFlag = player.facing.getHorizontalIndex
    
    if (!config.coreOnly) {
      if (event.phase.equals(Phase.START)) {
        if (player.getEntityData().getInteger("isInTrap") <= 0) {
          player.getEntityData().setInteger("isInTrap", player.getEntityData().getInteger("isInTrap") - 1)
        } else if (Option(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap)).isDefined) 
          player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(TileTrap.UUIDSpeedTrap))
      }
      if (!world.isRemote && player.hasLantarn()) {
        tickLight += 1
        if (tickLight >= 20) {
          tickLight = 0
          player.lanternTick()
          val bd = (player.worldObj, new BlockPos(player.posX.toInt, player.posY.toInt, player.posZ.toInt))
          if (bd.down.block == Blocks.AIR) bd.down.setState(Objs.light.getDefaultState)
          else if (bd.block == Blocks.AIR) bd.setState(Objs.light.getDefaultState)
          else if (bd.up.block == Blocks.AIR) bd.up.setState(Objs.light.getDefaultState)
        }
      }
      if (!world.isRemote && player.hasMap()) {
        val data = player.getCurrentMapData()
        PacketSender.to(new MapData(data.scale, data.xCenter, data.zCenter, data.colors), player.asInstanceOf[EntityPlayerMP])
      }
    }
    if (!world.isRemote) {
      val oldMarsh = marshupdate
      
      if(Option(player.inventory.getCurrentItem).isDefined){
        val mob = Option(player.getMOP)
        if (mob.isDefined) {
          val x = mob.get.getBlockPos.getX
          val y = mob.get.getBlockPos.getY
          val z = mob.get.getBlockPos.getZ
          val bd = (player.worldObj, mob.get.getBlockPos)
          val item = player.inventory.getCurrentItem
          
          if(bd.tile.isInstanceOf[Roaster] && (new Vec3d(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(new Vec3d(player.posX, player.posY, player.posZ)) <= 2.5F)){
            val roaster = bd.tile.asInstanceOf[Roaster]
            if(roaster.canRoast(item)){
              if(marshupdate > roaster.roastTime(item)){
                player.inventory.getCurrentItem.stackSize -= 1
                if (player.inventory.getCurrentItem.stackSize <= 0) player.setHeldItem(player.getActiveHand, null)
                
                val cooked = roaster.roastResult(item)
                
                if (!player.inventory.addItemStackToInventory(cooked)) player.dropItem(cooked, false)
                if(Option(player.getRidingEntity).isDefined && player.getRidingEntity.isInstanceOf[Mountable])player.addStat(Objs.achMarshRoast)
                marshupdate = 0
              } else marshupdate += roaster.roastSpeed(item)
            } 
          } 
        } 
      }
      
      if(marshupdate == oldMarsh)marshupdate = 0
    }
        
    if (!config.coreOnly) {
      var campNum = 0.0f
      for (i <- 0 until 4 if (player.inventory.armorInventory(i) != null && player.inventory.armorInventory(i).getItem.isInstanceOf[ItemArmor] && player.inventory.armorInventory(i).getItem.asInstanceOf[ItemArmor].getArmorMaterial.equals(Objs.fur))) campNum += 0.25f
      if(campNum == 1) player.addStat(Objs.achWildMan)
      if (player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(UUIDSpeedCamping) != null) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(UUIDSpeedCamping))
      player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(UUIDSpeedCamping, "camping.speedBoost", 0.04 * campNum, 0))
    }
  }
}

@SideOnly(Side.CLIENT)
class EventsC {
  var map: GuiMapHUD = _

  @SubscribeEvent
  def onKeyInput(event: KeyInputEvent) {
    if (!FMLClientHandler.instance.isGUIOpen(classOf[GuiChat])) {
      for (key <- 0 until KeyInfo.default.length) {
        if (Objs.keyOpenCamping.isPressed) {
          val id = KeyInfo.default.indexOf(KeyInfo.default.find { default => default == Objs.keyOpenCamping.getKeyCodeDefault }.get)
          keyPressedClient(id)
          PacketSender.toServer(new KeyData(id))
        }
      }
    }
  }
  def keyPressedClient(id: Int) {
    val player = Minecraft.getMinecraft.thePlayer
  }
  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if (!config.coreOnly) {
      if(event.getGui.isInstanceOf[GuiInventory]&&config.prmInv==0){
        if(Minecraft.getMinecraft.thePlayer.capabilities.isCreativeMode) return;
        event.setCanceled(true)
        PacketSender.toServer(new OpenGui(Objs.guiCamping, 0, 0, 0))
      }
    }
  }
  @SubscribeEvent
  def onClientTick(event: ClientTickEvent) {
    if (event.phase != TickEvent.Phase.END) return
    if (!config.coreOnly&&config.prmInv==1) {
      val mc = FMLClientHandler.instance().getClient
      if (mc.currentScreen.isInstanceOf[GuiInventory]) {
        if(mc.thePlayer.capabilities.isCreativeMode)return
        val list: ArrayList[GuiButton] = ReflectionHelper.getPrivateValue(classOf[GuiScreen], mc.currentScreen, 7)
        if(!list.asInstanceOf[java.util.List[GuiButton]].exists(_.id==10)){
          if(config.secInv==2)list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, mc.currentScreen.width-100-5, 5, 100, 10, "Camping Inventory"))
          else if(config.secInv==4)list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, mc.currentScreen.width-100-5, mc.currentScreen.height-10-5, 100, 10, "Camping Inventory"))
          else if(config.secInv==3)list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, 5, mc.currentScreen.height-10-5, 100, 10, "Camping Inventory"))
          else if(config.secInv==1) list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, 5, 5, 100, 10, "Camping Inventory"))  
          else {
            val left:Int = ReflectionHelper.getPrivateValue(classOf[GuiContainer], mc.currentScreen.asInstanceOf[GuiContainer], 4)
            val top:Int = ReflectionHelper.getPrivateValue(classOf[GuiContainer], mc.currentScreen.asInstanceOf[GuiContainer], 5)
            val width:Int = ReflectionHelper.getPrivateValue(classOf[GuiContainer], mc.currentScreen.asInstanceOf[GuiContainer], 1)
            list.asInstanceOf[java.util.List[GuiButton]].add(new ButtonItem(10, left+width-22, top+5, new ItemStack(Objs.backpack)))
          }
          ReflectionHelper.setPrivateValue(classOf[GuiScreen], mc.currentScreen, list, 7) 
        }
      }
    }
  }
  @SubscribeEvent
  def onPlayerJoinWorld(event: PlayerLoggedInEvent){
    if(config.welcomeMess){
      event.player.addChatComponentMessage(new TextComponentString("The Camping Iventory overides the normal inventory. To change this, " +
                                                                 "just press the configuration tab in the Camping Invenory; search for: " +
                                                                 "'primary inventory option'."))

      config.disableMess
    }
  }
  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent) {
    if (event.getType != ElementType.HOTBAR) return
    if (!config.coreOnly) {
      val mc = FMLClientHandler.instance().getClient
      if (map == null) map = new GuiMapHUD()
      if (mc.thePlayer.hasMap) {
        map.setWorldAndResolution(mc, event.getResolution.getScaledWidth, event.getResolution.getScaledHeight)
        map.drawScreen(0, 0, event.getPartialTicks)
      }
    }
  }
  @SubscribeEvent
  def buttonPressed(event: ActionPerformedEvent) {
    if (!config.coreOnly&&config.prmInv==1){
      if (event.getGui.isInstanceOf[GuiInventory]&&event.getButton.id==10){
          PacketSender.toServer(new OpenGui(Objs.guiCamping))
      } 
    }
  }
}