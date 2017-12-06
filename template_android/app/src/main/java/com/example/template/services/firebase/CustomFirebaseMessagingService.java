package com.example.template.services.firebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.template.R;
import com.example.template.ui.TestRestApi.RestApiListAct;
import com.example.template.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import static com.example.template.utils.NotificationUtils.ANDROID_CHANNEL_ID;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    NotificationUtils mNotificationUtils;

    private static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();
    int notification_id = 77;
    Intent intent;
    PendingIntent contentIntent;
    NotificationCompat.Builder mBuilder;
    JSONObject jsonObject;
    String type;
    String message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mNotificationUtils = new NotificationUtils(this);
        Log.e("onMessageReceived", " onMessageReceived ");
        Log.e("MessageFcm", " Message Data: " + remoteMessage.getData().get("message"));
        try {

            String message = remoteMessage.getData().get("message");
            JSONObject jsonObject = new JSONObject(message);
            String firebase_json_message = jsonObject.getString("firebase_json_message");
            sendNotification(firebase_json_message);
        } catch (Exception e) {
            Log.e("Exceptio Message Received ", e.toString());
        }


    }

    private void sendNotification(String firebase_json_message) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        try {

            jsonObject = new JSONObject(firebase_json_message);
            type = jsonObject.getString("type");
            message = jsonObject.getString("message");


            switch (type) {
                case "normal":
                    intent = new Intent(this, RestApiListAct.class);
                    //intent.putExtra(getString(R.string.sourceObject),);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    contentIntent = PendingIntent.getActivity(this, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder = new NotificationCompat.Builder(
                            this, ANDROID_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("fcm normal message received")
                            .setContentText(message)
                            .setSound(defaultSoundUri)
                            .setAutoCancel(true);
                    // .setContentText()
                    ;
                    mBuilder.setContentIntent(contentIntent);
                    mNotificationUtils.getManager().notify(notification_id++, mBuilder.build());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
