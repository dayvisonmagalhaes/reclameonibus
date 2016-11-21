package mobile.br.com.reclameonibus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Matheus on 21/09/2016.
 */
public class ReclamacoesReceiver extends BroadcastReceiver {
    private final long TIME_LOOP = 1000 * 10;// * 60 * 24; // Millisec * Second * Minute * Hour
    private final int RANDOM_ID = 192837472;

    public ReclamacoesReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        startChecking(context);
        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, ReclamacoesReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIME_LOOP, pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, ReclamacoesReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void startChecking(Context context) {
        Log.d("ReclamacoesService", "startChecking");
        new AsyncSeachReclamacoesNovas(context).execute();
    }

    private Intent prepareContentIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private boolean needCheck(Context context) {
        Calendar cal = Calendar.getInstance();
        int currentWeekOfYear = cal.get(Calendar.DAY_OF_YEAR);

        SharedPreferences sharedPreferences = context.getSharedPreferences("appInfo", 0);
        int weekOfYear = sharedPreferences.getInt("dayOfYear", 0);

        if (weekOfYear != currentWeekOfYear) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("dayOfYear", currentWeekOfYear);
            editor.commit();
            // Your once a day code here
            return true;
        }
        return false;
    }

    private class AsyncSeachReclamacoesNovas extends AsyncTask<Void, Void, Boolean> {

        String message;
        Context context;

        public AsyncSeachReclamacoesNovas(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

//            Log.i("OportunidadesService", "checking");
//
//            ArrayList<NameValuePair> paramsData = new ArrayList<NameValuePair>();
//            paramsData.add(new BasicNameValuePair("action", "getNews"));
//
//            JSONObject jObjectResponse = JsonParser.postDataObject(Connection.REC_URL, JsonParser.POST, paramsData);
//
//            if(jObjectResponse!=null) {
//                try {
//                    message = jObjectResponse.getString("message");
//                    //Just to catch the response=null.
//                    jObjectResponse.getJSONArray("response");
//
//                    return jObjectResponse.getBoolean("error");
//                } catch (JSONException e) {
//                    //e.printStackTrace();
//                    message = e.getMessage();
//                    return true;
//                }
//            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean error) {


        }
    }
}
