package ph.edu.mobapde.meditake.meditake;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.activity.DrinkMedicineActivity;
import ph.edu.mobapde.meditake.meditake.activity.WelcomeScreenActivity;

/**
 * Created by DELL-PC on 06/04/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HELLO","HELLO");
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.aspirins_black);

        Intent intentNext = new Intent(context,WelcomeScreenActivity.class);
        PendingIntent pendingNext= PendingIntent.getActivity(context
                , DrinkMedicineActivity.PENDING_NEXT_ACTIVITY
                ,intentNext
                ,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        Notification.Builder notifBuilder= new Notification.Builder(context)
                .setContentTitle("Snooze")
                .setContentText("You missed an alarm!")
                .setSmallIcon(R.drawable.aspirins_black)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setTicker("We have an announcement")
                .setContentIntent(pendingNext)
                ;

        notificationManager.notify(DrinkMedicineActivity.NOTIFICATION_ANNOUNCEMENT,notifBuilder.build());
    }
}