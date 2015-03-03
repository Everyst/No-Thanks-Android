package co.therealm.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.SharedPreferences;

/**
 * Created by GeoffF on 3/03/2015.
 */
public interface FileIO {

    public InputStream readFile(String file) throws IOException;

    public OutputStream writeFile(String file) throws IOException;

    public InputStream readAsset(String file) throws IOException;

    public SharedPreferences getSharedPref();

}
