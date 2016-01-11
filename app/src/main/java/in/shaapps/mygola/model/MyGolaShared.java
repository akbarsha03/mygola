package in.shaapps.mygola.model;

import org.jraf.android.prefs.DefaultInt;
import org.jraf.android.prefs.Prefs;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 11/8/2015.
 */
@Prefs
public class MyGolaShared {

    @DefaultInt(0)
    Integer sortBy;
}