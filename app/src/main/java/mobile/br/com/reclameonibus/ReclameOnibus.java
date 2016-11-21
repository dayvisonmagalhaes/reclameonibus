package mobile.br.com.reclameonibus;

import android.app.ActivityManager;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Matheus on 21/09/2016.
 */
public class ReclameOnibus extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (!isMyServiceRunning()) {
            Intent intentService = new Intent(this, ReclamacoesService.class);
            startService(intentService);
        }

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("mobile.br.com.reclameonibus.ReclamacoesService".equals(service.service.getClassName())) {
                Log.i("ReclameOnibus", "My Service is running");
                return true;
            }
        }
        Log.i("ReclameOnibus", "My Service is not running");
        return false;
    }
}
