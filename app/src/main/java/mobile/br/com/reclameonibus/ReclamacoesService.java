package mobile.br.com.reclameonibus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Matheus on 21/09/2016.
 */
public class ReclamacoesService extends Service {

    private ReclamacoesReceiver receiver = new ReclamacoesReceiver();

    public ReclamacoesService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ReclamacoesService", "Service is running");
        receiver.onReceive(this, intent);
        receiver.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        receiver.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ReclamacoesService", "Service is dead");
    }
}
