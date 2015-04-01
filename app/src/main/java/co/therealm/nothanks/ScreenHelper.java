package co.therealm.nothanks;

import android.graphics.Color;
import android.graphics.Paint;

import co.therealm.framework.Graphics;
import co.therealm.framework.Input;

/**
 * Created by Geoffrey on 4/03/2015.
 */
public class ScreenHelper{

    public static boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean inButtonBounds(Input.TouchEvent event, int[] button){
        return inBounds(event, button[0], button[1], button[2], button[3]);
    }

    public static void drawButton(Graphics graphics, Paint paint, int[] bounds, String text){
        graphics.drawRect(bounds[0], bounds[1], bounds[2], bounds[3], Color.DKGRAY);
        graphics.drawString(text, bounds[0] + (bounds[2] / 2), bounds[1] + (bounds[3] / 2), paint);
    }
}
