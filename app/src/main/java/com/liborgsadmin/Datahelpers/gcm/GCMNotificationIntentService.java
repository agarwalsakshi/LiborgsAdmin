package com.liborgsadmin.Datahelpers.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.liborgsadmin.android.BooksDataActivity;
import com.liborgsadmin.android.R;
import com.liborgsadmin.utils.ApplicationConstants;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty() && GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

            String type = extras.getString("type");
            String bookTitile = extras.getString("book_titile");
            String issuer_name = extras.getString("issuer_name");

            if ((ApplicationConstants.GCM_REQUEST_BOOK_TYPE).equalsIgnoreCase(type)) {
                sendNotification(issuer_name + " has requested " + bookTitile + ".");
            }

            else if (ApplicationConstants.GCM_RETURN_BOOK_TYPE.equalsIgnoreCase(type)) {

                sendNotification(issuer_name + " has returned " + bookTitile + ".");
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, BooksDataActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.library_icon)
                .setContentTitle(getString(R.string.notification))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}