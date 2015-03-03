package co.therealm.framework;

import java.util.List;

import android.view.View.OnTouchListener;

import co.therealm.framework.Input.TouchEvent;

/**
 * Created by GeoffF on 3/03/2015.
 */
public interface TouchHandler extends OnTouchListener {

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();

}
