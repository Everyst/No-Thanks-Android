package co.therealm.framework;

import co.therealm.framework.Graphics.ImageFormat;

/**
 * Created by GeoffF on 3/03/2015.
 */
public interface Image {

    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();

}
