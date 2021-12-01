package me.aquavit.liquidsense.modules.movement

import me.aquavit.liquidsense.event.EventTarget
import me.aquavit.liquidsense.event.events.MoveEvent
import me.aquavit.liquidsense.event.events.Render3DEvent
import me.aquavit.liquidsense.modules.combat.Aura
import me.aquavit.liquidsense.utils.entity.MovementUtils
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import me.aquavit.liquidsense.utils.client.RotationUtils
import me.aquavit.liquidsense.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@ModuleInfo(name = "TargetStrafe", description = "Rotate around with entity", category = ModuleCategory.COMBAT) class TargetStrafe : Module() {

    private val thirdPersonView = BoolValue("ThirdPersonView", false)
    private val radiusValue = FloatValue("Radius", 0.1f, 0.1f, 5.0f)
    private val otPosValue = FloatValue("OtPos", 0.1f, -2.0f, 2.0f)
    private val keeprangeValue = BoolValue("KeepRange", true)
    private val radiusturnValue = ListValue("Radiusturn", arrayOf("Player", "None"), "None")
    private val modeValue = ListValue("KeyMode", arrayOf("Jump", "None"), "Jump")
    private val radiusMode = ListValue("RadiusMode", arrayOf("TrueRadius", "Simple"), "TrueRadius")
    private val drawMode = BoolValue("DrawCircle", false)
    private val shape = IntegerValue("DrawShape", 14, 3, 32)
    private val killAura = LiquidBounce.moduleManager.getModule(Aura::class.java) as Aura
    private val speed = LiquidBounce.moduleManager.getModule(Speed::class.java) as Speed
    private val fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly

    private val oldPer = mc.gameSettings.thirdPersonView
    private var consts = 0
    private var lastDist = 0.0

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        killAura.target ?: return
        var x: Double =
            (killAura.target!!.lastTickPosX + (killAura.target!!.posX - killAura.target!!.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosX)
        var y: Double =
            (killAura.target!!.lastTickPosY + (killAura.target!!.posY - killAura.target!!.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosY)
        var z: Double =
            (killAura.target!!.lastTickPosZ + (killAura.target!!.posZ - killAura.target!!.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.viewerPosZ)
        if (drawMode.get()) {
            if (canStrafe) {
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get() - 0.013, shape.get(), Color(0, 0, 0).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get() + 0.013, shape.get(), Color(0, 0, 0).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() - 0.005, shape.get(), Color(75, 250, 75).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() - 0.0025, shape.get(), Color(75, 250, 75).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble(), shape.get(), Color(75, 250, 75).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() + 0.0025, shape.get(), Color(75, 250, 75).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() + 0.005, shape.get(), Color(75, 250, 75).rgb)
            }
            if (!canStrafe && killAura.target != null) {
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get() - 0.013, shape.get(), Color(0, 0, 0).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get() + 0.013, shape.get(), Color(0, 0, 0).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() - 0.005, shape.get(), Color(255, 255, 255).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() - 0.0025, shape.get(), Color(255, 255, 255).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble(), shape.get(), Color(255, 255, 255).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() + 0.0025, shape.get(), Color(255, 255, 255).rgb)
                RenderUtils.TScylinder(killAura.target, x, y, z, radiusValue.get().toDouble() + 0.005, shape.get(), Color(255, 255, 255).rgb)
            }
        }
    }

    @EventTarget
    fun movestrafe(event: MoveEvent) {
        if (radiusValue.get() <= 0F) return

        val xDist = event.x
        val zDist = event.z
        lastDist = sqrt(xDist * xDist + zDist * zDist)

        if (canStrafe) {
            val yaw = RotationUtils.getRotations(killAura.target, if (keeprangeValue.get()) otPosValue.get() else 0.0f)
            setStrafe(event, lastDist, yaw, radiusValue.get(), 1.0)
        }

        if (!thirdPersonView.get()) return

        for (i in 0..mc.gameSettings.thirdPersonView) {
            if (canStrafe) mc.gameSettings.thirdPersonView = 4
            else if (mc.gameSettings.thirdPersonView > 3) mc.gameSettings.thirdPersonView = oldPer
        }
    }

    private val keyMode: Boolean
        get() = when (modeValue.get().toLowerCase()) {
            "jump" -> mc.gameSettings.keyBindJump.isKeyDown && MovementUtils.isMoving()
            "none" -> MovementUtils.isMoving()
            else -> false
        }

    private val canStrafe: Boolean
        get() = (killAura.state && killAura.target != null && (speed.state || fly.state) && !mc.thePlayer.isSneaking && keyMode)

    private val cansize: Float
        get() = when {
            radiusMode.get().toLowerCase() == "simple" -> 45f / mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ).toFloat()
            else -> 45f
        }

    private val enemydistance: Double
        get() = mc.thePlayer!!.getDistance(killAura.target!!.posX, mc.thePlayer!!.posY, killAura.target!!.posZ)

    private val algorithm: Float
        get() = (enemydistance - radiusValue.get()).coerceAtLeast(enemydistance - (enemydistance - radiusValue.get() / (radiusValue.get() * 2)))
            .toFloat()

    private fun setStrafe(moveEvent: MoveEvent, moveSpeed: Double, pseudoYaw: Float, pseudoStrafe: Float, pseudoForward: Double) {
        var yaw = pseudoYaw
        var forward = pseudoForward
        var strafe = pseudoStrafe
        var strafe2 = 0f
        check()
        when {
            radiusturnValue.get().toLowerCase().equals("player") -> strafe = pseudoStrafe * mc.thePlayer.movementInput.moveStrafe * consts
            radiusturnValue.get().toLowerCase().equals("none") -> strafe = consts.toFloat()
        }
        if (forward != 0.0) {
            if (strafe > 0.0) {
                if (keeprangeValue.get()) yaw += (if (forward > 0.0) -cansize else cansize)
                strafe2 += (if (forward > 0.0) -45 / algorithm else 45 / algorithm)
            }
            else if (strafe < 0.0) {
                if (keeprangeValue.get()) yaw += (if (forward > 0.0) cansize else -cansize)
                strafe2 += (if (forward > 0.0) 45 / algorithm else -45 / algorithm)
            }
            strafe = 0.0f
            if (forward > 0.0) {
                forward = 1.0
            }
            else if (forward < 0.0) {
                forward = -1.0
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0f
        }
        else if (strafe < 0.0) {
            strafe = -1.0f
        }

        if (!keeprangeValue.get()) strafe2 *= radiusValue.get()
        val mx = cos(Math.toRadians(yaw + 90.0 + strafe2))
        val mz = sin(Math.toRadians(yaw + 90.0 + strafe2))
        moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz
        moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx
    }

    private fun check() {
        if (mc.thePlayer!!.isCollidedHorizontally || checkVoid()) {
            if (consts < 2) consts += 1
            else {
                consts = -1
            }
        }
        when (consts) {
            0 -> consts = 1
            2 -> consts = -1
        }
    }

    private fun checkVoid(): Boolean {
        for (x in -1..0) {
            for (z in -1..0) {
                if (isVoid(x, z)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isVoid(X: Int, Z: Int): Boolean {
        val fly = LiquidBounce.moduleManager.getModule(Fly::class.java) as Fly
        if (fly.state) {
            return false
        }
        if (mc.thePlayer!!.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < mc.thePlayer!!.posY.toInt() + 2) {
            val bb: AxisAlignedBB = mc.thePlayer!!.entityBoundingBox.offset(X.toDouble(), (-off).toDouble(), Z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer as Entity, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }

}