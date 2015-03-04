package co.therealm.nothanks;

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
}
