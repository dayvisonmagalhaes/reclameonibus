package mobile.br.com.reclameonibus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Matheus on 24/10/2016.
 */
public class AndroidUtils {


    public static boolean isNetworkAvailable(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager == null){
                return false;
            }else{
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if(info != null){
                    for (int i = 0; i < info.length; i++){
                            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                                return true;
                            }
                    }
                }
            }
        } catch (SecurityException e){
            alertDialog(context, e.getClass().getSimpleName(), e.getMessage());
        }
        return false;
    }

    public static void alertDialog (final Context context, final String title, final String mensagem) {

        try {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                    title).setMessage(mensagem)
                    .create();

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                    public void  onClick(DialogInterface dialog, int which) {
                    }
            });
            dialog.show();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}


