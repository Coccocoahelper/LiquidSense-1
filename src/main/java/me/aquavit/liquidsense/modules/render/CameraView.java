package me.aquavit.liquidsense.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;

@ModuleInfo(name = "CameraView", description = ":/", category = ModuleCategory.RENDER)
public class CameraView extends Module {
    public static final FloatValue fovValue = new FloatValue("FOV", 1f, 0f, 30f);
    public static final BoolValue clipValue = new BoolValue("CameraClip", false);
}