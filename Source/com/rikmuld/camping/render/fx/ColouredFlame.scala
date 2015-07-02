package com.rikmuld.camping.render.fx

import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.FMLClientHandler
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.fml.relauncher.Side
import com.rikmuld.camping.Lib.TextureInfo
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.entity.Entity

object ColouredFlame {
  final val TEX = new ResourceLocation("minecraft:textures/particle/particles.png")
}

@SideOnly(Side.CLIENT)
class ColouredFlame(world: World, x: Double, y: Double, z: Double, mX: Double, mY: Double, mZ: Double, color: Int) extends EntityFX(world, x, y, z, mX, mY, mZ) {
  motionX = (motionX * 0.009999999776482582D) + mX
  motionY = (motionY * 0.009999999776482582D) + mY
  motionZ = (motionZ * 0.009999999776482582D) + mZ
  particleMaxAge = rand.nextInt(15) + 10
  particleScale = (rand.nextInt(40) + 60F) / 100F
  particleTextureIndexX = color % 16
  particleTextureIndexY = color / 16

  override def getBrightness(par1: Float): Float = {
    var var2 = (particleAge + par1) / particleMaxAge
    if (var2 < 0.0F) var2 = 0.0F
    if (var2 > 1.0F) var2 = 1.0F
    val var3 = super.getBrightness(par1)
    (var3 * var2) + (1.0F - var2)
  }
  override def getBrightnessForRender(par1: Float): Int = {
    var var2 = (particleAge + par1) / particleMaxAge
    if (var2 < 0.0F) var2 = 0.0F
    if (var2 > 1.0F) var2 = 1.0F
    val var3 = super.getBrightnessForRender(par1)
    var var4 = var3 & 255
    val var5 = (var3 >> 16) & 255
    var4 += (var2 * 15.0F * 16.0F).toInt
    if (var4 > 240) var4 = 240
    var4 | (var5 << 16)
  }
  override def getFXLayer(): Int = 0
  override def onUpdate() {
    prevPosX = posX
    prevPosY = posY
    prevPosZ = posZ
    particleAge += 1;

    if (particleAge >= particleMaxAge) setDead()
    moveEntity(motionX, motionY, motionZ)

    motionX *= 0.9599999785423279D
    motionY *= 0.9599999785423279D
    motionZ *= 0.9599999785423279D
    if (onGround) {
      motionX *= 0.699999988079071D
      motionZ *= 0.699999988079071D
    }
  }
  override def func_180434_a(render: WorldRenderer, entity:Entity, par2: Float, par3: Float, par4: Float, par5: Float, par6: Float, par7: Float) {
    val tessellator1 = Tessellator.getInstance
    tessellator1.draw()
    tessellator1.getWorldRenderer.startDrawingQuads()
    tessellator1.getWorldRenderer.setBrightness(getBrightnessForRender(par2))
    val mc = FMLClientHandler.instance().getClient
    mc.renderEngine.bindTexture(new ResourceLocation(TextureInfo.SPRITE_FX))
    val f8 = 1.0F
    GL11.glPushMatrix()
    tessellator1.getWorldRenderer.setColorOpaque_F(particleRed * f8, particleGreen * f8, particleBlue * f8)
    super.func_180434_a(render, entity, par2, par3, par4, par5, par6, par7)
    tessellator1.draw()
    GL11.glPopMatrix()
    Minecraft.getMinecraft.renderEngine.bindTexture(ColouredFlame.TEX)
    tessellator1.getWorldRenderer.startDrawingQuads()
  }
}