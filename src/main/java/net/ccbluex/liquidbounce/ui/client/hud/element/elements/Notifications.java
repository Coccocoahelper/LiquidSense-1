package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.extend.ColorType;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.extend.FadeState;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.extend.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.extend.Print;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ElementInfo(name = "Notifications", single = true, disableScale = true)
public class Notifications extends Element {

    private final Notification exampleNotification = new Notification("Notification", "This is an example notification.", ColorType.INFO,1500,500);

    public Notifications() {
        super(0, 30, 1f, new Side(Side.Horizontal.LEFT, Side.Vertical.UP));
    }

    public Stream<Notification> notification;
    @Override
    @Nullable
    public Border drawElement() {
        notification = LiquidBounce.hud.getNotifications().stream();
        int index = 0;
        for (Notification notify : notification.collect(Collectors.toList())) {
            GL11.glPushMatrix();
            if (notify.drawNotification(index)) {
                notification.collect(Collectors.toList()).add(notify);
            }
            GL11.glPopMatrix();
            if (notify.getFadeState() == FadeState.END) {
                LiquidBounce.hud.notifications.remove(notify);
                --index;
            }
            ++index;
        }
        if (mc.currentScreen instanceof GuiHudDesigner) {
            if (!LiquidBounce.hud.notifications.contains(this.exampleNotification)) {
                LiquidBounce.hud.addNotification(this.exampleNotification);
            }
            this.exampleNotification.setFadeState(FadeState.STAY);
            this.exampleNotification.setDisplayTime(System.currentTimeMillis());
            return new Border(-this.exampleNotification.getWidth() - 21.5f, -this.exampleNotification.getHeight(), 0.5f, 0.0f);
        }
        return null;
    }
}