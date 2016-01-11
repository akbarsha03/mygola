package in.shaapps.mygola;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * My Gola
 * --
 * Created by Akbar Sha Ebrahim on 11/8/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());

    }
}
