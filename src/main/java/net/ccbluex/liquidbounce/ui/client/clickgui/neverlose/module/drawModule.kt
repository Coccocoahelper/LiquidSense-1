package net.ccbluex.liquidbounce.ui.client.clickgui.neverlose.moduleimport com.sun.javafx.scene.control.behavior.KeyBindingimport me.aquavit.liquidsense.utils.render.Translateimport net.ccbluex.liquidbounce.LiquidBounceimport net.ccbluex.liquidbounce.ui.client.hud.element.elements.KeyBindsimport net.ccbluex.liquidbounce.ui.client.neverlose.Implimport net.ccbluex.liquidbounce.ui.client.neverlose.Impl.openmidmangerimport net.ccbluex.liquidbounce.ui.client.neverlose.Mainimport net.ccbluex.liquidbounce.ui.font.Fontsimport net.ccbluex.liquidbounce.utils.render.RenderUtilsimport net.minecraft.client.renderer.GlStateManagerimport org.lwjgl.input.Keyboardimport org.lwjgl.input.Mouseimport java.awt.Colorimport javax.swing.text.JTextComponentimport kotlin.math.absobject drawModule {    var keyevent = 0    val translate = Translate(0f , 0f)    fun drawModule(mouseX: Int, mouseY: Int, main: Main) {        when (Impl.theType) {            Impl.Type.CLIENT -> {                var lmodulesize = 0                var rmodulesize = 0                var modulesize = 0                var line = 0                var line2 = 0                var positiony = 0                var positiony2 = 0                main.modules = LiquidBounce.moduleManager.modules.filter {                    ((it.name.indexOf(Impl.Search) != -1 || it.name.toLowerCase()                        .indexOf(Impl.Search) != -1 || it.name == Impl.Search || it.name.toLowerCase() == Impl.Search) && Impl.Search.isNotEmpty()) || (it.category.displayName == Impl.selectedCategory && Impl.Search.isEmpty()) || (Impl.Search == "enabler" && it.state)                }                main.modules.filter { main.modules.isNotEmpty() }.forEachIndexed { index, module ->                    if (index % 2 == 0) {                        if (index == 2 * line) line++                        if (index - 2 >= 0) positiony += main.modules[index - 2].values.size + main.modules[index - 2].outvalue                        val positionX = 0                        val positionY = Impl.coordinateY.toInt() + 40 + (25 * line) + positiony * 20 + main.lwheeltranslate.y.toInt()                        if (positionY < Impl.coordinateY.toInt() + 325 && positionY + (module.values.size + module.outvalue) * 20 > Impl.coordinateY.toInt()) NModule.drawModule(                                       Impl.coordinateX.toInt() + 103 + positionX, positionY, mouseX, mouseY, module, main)                        lmodulesize++                    }                    if (index % 2 != 0) {                        if (index >= 1 * line2) line2++                        if (index - 2 >= 0) positiony2 += main.modules[index - 2].values.size + main.modules[index - 2].outvalue                        val positionX = 168                        val positionY = Impl.coordinateY.toInt() + 40 + (25 * line2) + positiony2 * 20 + main.rwheeltranslate.y.toInt()                        if (positionY < Impl.coordinateY.toInt() + 325 && positionY + (module.values.size + module.outvalue) * 20 > Impl.coordinateY.toInt()) NModule.drawModule(                                       Impl.coordinateX.toInt() + 103 + positionX, positionY, mouseX, mouseY, module, main)                        rmodulesize++                    }                    modulesize++                }                try {                    if (!main.modules.isEmpty()) {                        val llastvaluesize =                            if ((if (lmodulesize + rmodulesize >= 2) modulesize else lmodulesize) % 2 == 0) main.modules[main.modules.lastIndex - 1].values.size + main.modules[main.modules.lastIndex - 1].outvalue                            else main.modules[main.modules.size - 1].values.size + main.modules[main.modules.size - 1].outvalue                        val rlastvaluesize =                            if ((if (lmodulesize + rmodulesize >= 2) modulesize else rmodulesize) % 2 == 0) main.modules[main.modules.size - 1].values.size + main.modules[main.modules.size - 1].outvalue                            else main.modules[main.modules.size - 2].values.size + main.modules[main.modules.size - 2].outvalue                        val lmax = line * 25 + positiony * 20 + if (llastvaluesize == 0) 50 else llastvaluesize * 20 + 50                        val rmax = line2 * 25 + positiony2 * 20 + if (rlastvaluesize == 0) 50 else rlastvaluesize * 20 + 50                        val maxWheel = lmax + rmax                        val idk =                            (285 - 85) * abs(                                           main.lwheeltranslate.y + main.rwheeltranslate.y) / (maxWheel - (650 - ((if (lmax < 350) 325 - lmax else 0) + (if (rmax < 350) 325 - rmax else 0))))                        RenderUtils.drawRect(Impl.coordinateX + 431, Impl.coordinateY + 65, Impl.coordinateX + 433, Impl.coordinateY + 335,                                             Color(29, 35, 37).rgb)                        RenderUtils.drawRect(Impl.coordinateX + 431, Impl.coordinateY + 65 + idk, Impl.coordinateX + 433,                                             Impl.coordinateY + 50 + 85 + idk, Color(21, 96, 135).rgb)                        main.lwheeltranslate.translate(0f, Impl.lwheel, 3.0)                        main.rwheeltranslate.translate(0f, Impl.rwheel, 3.0)                        val wtf = 320 - lmax                        if (abs(Impl.lwheel) > wtf && lmax > wtf && lmax < 320) Impl.lwheel = 0f                        val wtf2 = 320 - rmax                        if (abs(Impl.rwheel) > wtf2 && rmax > wtf2 && rmax < 320) Impl.rwheel = 0f                        if ((abs(Impl.lwheel) > lmax - 320 && lmax > 320)) Impl.lwheel = -(lmax - 320f)                        if (abs(Impl.rwheel) > rmax - 320 && rmax > 320) Impl.rwheel = -(rmax - 320f)                        if (main.hovertoFloatL(Impl.coordinateX + 95f, Impl.coordinateY, Impl.coordinateX + 430f, Impl.coordinateY + 345f, mouseX,                                               mouseY, false) ) {                            val dWheel = Mouse.getDWheel()                            if (lmax > 325) {                                for (i in 0 until 10) {                                    if (dWheel < 0 && abs(Impl.lwheel) < lmax - 325) {                                        Impl.lwheel -= i                                    }                                    else if (dWheel > 0) {                                        Impl.lwheel += i                                        if (Impl.lwheel > 0) Impl.lwheel = 0f                                    }                                }                            }                            if (rmax > 325) {                                for (i in 0 until 10) {                                    if (dWheel < 0 && abs(Impl.rwheel) < rmax - 325) {                                        Impl.rwheel -= i                                    }                                    else if (dWheel > 0) {                                        Impl.rwheel += i                                        if (Impl.rwheel > 0) Impl.rwheel = 0f                                    }                                }                            }                        }                    }                } catch (e: IndexOutOfBoundsException) {                    e.printStackTrace()                }            }        }        if(Impl.midmangermodule == null)            return        translate.translate(if(!Impl.midmangermodule!!.array) 10f else 0f, if(openmidmanger) 1f else 0f, 5.0)        if(translate.y > 0) {            val positionx = (Impl.coordinateX + Impl.midmangerPositionX) / translate.y            val positiony = (Impl.coordinateY + Impl.midmangerPositionY) / translate.y            GlStateManager.pushMatrix()            GlStateManager.scale(translate.y  , translate.y  , translate.y )            RenderUtils.drawShader(positionx , positiony , 125f , 100f , Color(30 , 190 , 255).rgb)            RenderUtils.drawShader(positionx , positiony , 125f , 100f , Color(30 , 190 , 255).rgb)            RenderUtils.drawRect(positionx , positiony , positionx + 125 , positiony + 100 , Color(0,14,26).rgb)            RenderUtils.drawRect(positionx + 2 , positiony + 15 , positionx + 123 , positiony + 16 , Color(16,31,33).rgb)            Fonts.font18.drawString(Impl.midmangermodule!!.name, positionx + 5 , positiony + 5 , Color(255 ,255 ,255).rgb)            // keybind            Fonts.font18.drawString("Keybind" , positionx + 5, positiony + 22 , -1)            val keyname = Keyboard.getKeyName(Impl.midmangermodule!!.keyBind)            val kbpositionx = positionx + 80f - 0.25f            val hoverkeybind = main.hoverConfig(kbpositionx , positiony +  19 , kbpositionx + 40f , positiony +  31f , mouseX , mouseY , false)            if(Keyboard.getEventKey() != keyevent && hoverkeybind && Keyboard.getKeyName(Keyboard.getEventKey()).length < 2 || Keyboard.getEventKey() == Keyboard.KEY_DELETE)                Impl.midmangermodule!!.keyBind = if(Keyboard.getEventKey() == Keyboard.KEY_DELETE) Keyboard.KEY_NONE else Keyboard.getEventKey()            keyevent = Keyboard.getEventKey()            RenderUtils.drawRoundedRect(kbpositionx , positiony +  19 - 0.25f, 40f - 0.5f , 13f - 0.5f ,2f , if(!hoverkeybind) Color(2,5,12).rgb else Color(8,15,24).rgb, 0.2f ,  Color(45,45,45).rgb)            Fonts.font17.drawString(keyname , positionx + 83, positiony + 23f, Color(175 , 175 ,175).rgb)            // hide            Fonts.font18.drawString("Hide" , positionx + 5, positiony + 40 , if(!Impl.midmangermodule!!.array) -1 else Color(175 ,175 ,175).rgb)            RenderUtils.drawNLRect(positionx + 90f ,  positiony + 41 , positionx + 110f , positiony + 47 , 2.5f, if(!Impl.midmangermodule!!.array) Color(3,23,46).rgb else Color(3, 5, 13).rgb)            RenderUtils.drawFullCircle(positionx + 95f + translate.x,  positiony + 44 , 4f , 0f ,if(!Impl.midmangermodule!!.array) Color(3,168,245) else  Color(74,87,97) )            GlStateManager.resetColor()            val hoverarray = main.hoverConfig(positionx + 90f ,  positiony + 38 , positionx + 110f , positiony + 50, mouseX , mouseY , true)            if(hoverarray) {                Impl.midmangermodule!!.array = !Impl.midmangermodule!!.array            }            Fonts.font18.drawString("SetName" , positionx + 5, positiony + 57 , -1)            RenderUtils.drawRoundedRect(positionx + 5 , positiony +  69 - 0.25f, 115f - 0.5f , 13f - 0.5f ,2f , Color(2,5,12).rgb , 0.2f ,  Color(45,45,45).rgb)            Fonts.font18.drawString(Impl.midmangerSetnameString , positionx + 8, positiony + 73 , -1)            GlStateManager.resetColor()            GlStateManager.popMatrix()        }    }}