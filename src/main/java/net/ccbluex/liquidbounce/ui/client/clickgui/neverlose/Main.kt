package net.ccbluex.liquidbounce.ui.client.neverloseimport me.aquavit.liquidsense.utils.render.Translateimport net.ccbluex.liquidbounce.features.module.Moduleimport net.ccbluex.liquidbounce.features.module.ModuleCategoryimport net.ccbluex.liquidbounce.ui.client.clickgui.neverlose.About.aboutMainimport net.ccbluex.liquidbounce.ui.client.clickgui.neverlose.config.drawConfigimport net.ccbluex.liquidbounce.ui.client.clickgui.neverlose.module.drawModuleimport net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesignerimport net.ccbluex.liquidbounce.ui.client.neverlose.Impl.coordinateXimport net.ccbluex.liquidbounce.ui.client.neverlose.Impl.coordinateYimport net.ccbluex.liquidbounce.ui.font.Fontsimport net.ccbluex.liquidbounce.ui.font.GameFontRendererimport net.ccbluex.liquidbounce.utils.render.RenderUtilsimport net.minecraft.client.gui.GuiScreenimport net.minecraft.client.renderer.GlStateManagerimport org.lwjgl.input.Keyboardimport org.lwjgl.input.Mouseimport org.lwjgl.opengl.GL11import java.awt.Coloropen class Main : GuiScreen() {    var modules = emptyList<Module>()    private var rendercategory: ArrayList<Category> = ArrayList()    private var hovermove = false    private var ismove = false    private var lastmouseX = 0    private var lastmousey = 0    private var x = 0f    private var y = 0f    val lwheeltranslate = Translate(0f, 0f)    val rwheeltranslate = Translate(0f, 0f)    var mouseLDown = false    var mouseRDown = false    init {        var posy = 0        for (the in 0 until ModuleCategory.values().size) {            val category = Category(23f, 50f + posy, ModuleCategory.values()[the])            rendercategory.add(category)            posy+= (if(ModuleCategory.values()[the].displayName == "Misc") 30 else 18)        }    }    private fun renderHead() {        RenderUtils.drawRect(coordinateX , coordinateY + 305 , coordinateX + 94 , coordinateY + 306 , Color(16,31,33).rgb)        GL11.glPushMatrix()        GL11.glTranslated(coordinateX + 10.0, coordinateY + 310.0, 0.0)        GL11.glColor3f(1f, 1f, 1f)        val playerInfo = mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID)        if (playerInfo != null) {            val locationSkin = playerInfo.locationSkin            mc.textureManager.bindTexture(locationSkin)            RenderUtils.drawScaledCustomSizeModalRect(2, 2, 8F, 8F, 8, 8, 25, 25, 64F, 64F)            GL11.glColor4f(1F, 1F, 1F, 1F)        }        for (i in 0..18) {            RenderUtils.drawCircle(14.5f , 14.75f, 12.5f + (0.275f * i), -180, 180, Color(6,16,28))            GlStateManager.resetColor()        }        GL11.glPopMatrix()    }    fun drag(mouseX: Int, mouseY: Int) {        if (hovertoFloatL(coordinateX, coordinateY, coordinateX + 95f, coordinateY + 35f, mouseX, mouseY, false) && Mouse.isButtonDown(0)) {            hovermove = true            if (!ismove) {                lastmouseX = mouseX                lastmousey = mouseY                x = coordinateX                y = coordinateY                ismove = true            }        }        if (hovermove) {            if (!Mouse.isButtonDown(0)) {                hovermove = false                ismove = false            }            else {                coordinateX = mouseX.toFloat() - (lastmouseX - x)                coordinateY = mouseY.toFloat() - (lastmousey - y)            }        }    }    override fun drawScreen(mouseX: Int, mouseY: Int, tick: Float) {        drag(mouseX , mouseY)        RenderUtils.drawNLRect(coordinateX, coordinateY, coordinateX + 95f, coordinateY + 345f, 3f, Color(6, 16, 28).rgb)        RenderUtils.drawNLRect(coordinateX + 95f, coordinateY, coordinateX + 435f, coordinateY + 345f, 3f, Color(7, 7, 11).rgb)        RenderUtils.drawRect(coordinateX + 93f, coordinateY, coordinateX + 94f, coordinateY + 345f, Color(16, 31, 33).rgb)        RenderUtils.drawRect(coordinateX + 94f, coordinateY, coordinateX + 97f, coordinateY + 345f, Color(7, 7, 11).rgb)        RenderUtils.drawRect(coordinateX + 93f, coordinateY + 34f, coordinateX + 435f, coordinateY + 35f, Color(16, 31, 33).rgb)        Fonts.fontBold30.drawString("liquidsense".toUpperCase(), coordinateX + 4.5f, coordinateY + 13f, Color(11, 160, 255).rgb)        Fonts.fontBold30.drawString("liquidsense".toUpperCase(), coordinateX + 5, coordinateY + 13f, -1)        RenderUtils.drawNLRect(coordinateX + 1f, coordinateY + 280, coordinateX + 92f, coordinateY + 303f, 3f, Color(4, 120, 176).rgb)        Fonts.font20.drawCenteredString("manager".toUpperCase(), coordinateX + 45, coordinateY + 290f, -1 , false)        if (hoverConfig(coordinateX + 5f, coordinateY + 280, coordinateX + 90f, coordinateY + 305f, mouseX, mouseY, true)) {           mc.displayGuiScreen(GuiHudDesigner())        }        renderHead()        for (render in rendercategory)            render.drawCategory(mouseX, mouseY, this)        drawConfig.flie(mouseX, mouseY , this)        drawModule.drawModule(mouseX , mouseY , this)        aboutMain.drawabout(mouseX , mouseY , this)        mouseLDown = Mouse.isButtonDown(0)        mouseRDown = Mouse.isButtonDown(1)    }    var openConfig = false    fun drawText(name: String, size: Int, font: GameFontRenderer, positionX: Int, positionY: Int, color: Int) {        var newstring = ""        val oldstring = name.toCharArray()        for (i in 0..oldstring.lastIndex) {            if (i < size) {                newstring += oldstring[i]            }        }        font.drawString(newstring, positionX, positionY, color)    }    fun hovertoFloatL(xOne: Float, yOne: Float, xTwo: Float, yTwo: Float, mouseX: Int, mouseY: Int, click: Boolean): Boolean {        val hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo        return ((click && !mouseLDown && Mouse.isButtonDown(0) && hoverSystem) || (!click && hoverSystem)) && !openConfig    }    fun hovertoFloatR(xOne: Float, yOne: Float, xTwo: Float, yTwo: Float, mouseX: Int, mouseY: Int, click: Boolean): Boolean {        val hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo        return ((click && !mouseRDown && Mouse.isButtonDown(1) && hoverSystem) || (!click && hoverSystem)) && !openConfig    }    fun hoverConfig(xOne: Float, yOne: Float, xTwo: Float, yTwo: Float, mouseX: Int, mouseY: Int, click: Boolean): Boolean {        val hoverSystem = mouseX >= xOne && mouseX <= xTwo && mouseY >= yOne && mouseY <= yTwo        return ((click && !mouseLDown && Mouse.isButtonDown(0) && hoverSystem) || (!click && hoverSystem))    }}