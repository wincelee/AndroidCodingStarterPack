package manu.apps.androidcodingstarterpack.workmanagers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

import manu.apps.androidcodingstarterpack.R;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Bundle clickBundle = new Bundle();

//        Intent intentWithData = new Intent(getApplicationContext(), TokenBroadcastReceiver.class);
//        intentWithData.putExtra(Constants.ID,id);
//        intentWithData.putExtra(Constants.POSITION, finalI);

        Random random = new Random();

        int randomId =  random.nextInt(80 - 65) + 65;

        showNotification(String.valueOf(randomId), clickBundle);

        return Result.success();
    }

    private void showNotification(String channelId,Bundle bundle) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Setting Notification priority to high may sometimes not clear the notification
            NotificationChannel channel =
                    new NotificationChannel(channelId, "Test Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Test Channel");
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        PendingIntent pendingIntent = new NavDeepLinkBuilder(getApplicationContext())
                .setGraph(R.navigation.navigation_graph)
                //.setDestination()
                .setArguments(bundle)
                // Set Bundle Arguments Here
                //.setArguments()
                .createPendingIntent();


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        // Icon to the right of the notification
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getApplicationContext().getResources(), R.drawable.ic_launcher_foreground))
                        // Icon next to the notification title
                        .setSmallIcon(R.drawable.ic_phone)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle("Displaying Information")
                        // setTicker will show text on status bar, even without having to pull down
                        // text passed onto setTicker() will also be audibly announced.
                        .setTicker("Displaying Information")
                        .setContentText("This is a test notification lorem ipsum lorem ipsum lorem ipsum lorem ipsum ")
                        // Multiline content text style
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("This is a test notification lorem ipsum lorem ipsum lorem ipsum lorem ipsum "))

                        // Setting to ongoing will make the notification to never cancel
                        //.setOngoing(true)
                        //Automatically cancel notification on user intent click
                        .setAutoCancel(true)
                        // Add your notification action buttons here
                        .addAction(R.drawable.ic_phone, "View", pendingIntent)
                        .setLights(Color.GREEN, 500, 500)

                        // Setting action without action button in notification
                        //.setContentIntent(pendingIntent)
                        // set color of addAction title
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.purple_500))
                        .setPriority(NotificationCompat.PRIORITY_MAX);

        //notificationBuilder.setLights(0xff00ff00, 300, 100);

        // Set intent to the notification
        notificationBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setGroup("Test Notification Group");
        }


        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle
                .bigPicture(BitmapFactory.decodeResource(
                        getApplicationContext().getResources(), R.drawable.ic_launcher_foreground))
                //.bigLargeIcon(bitmap)
                .build();

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle
                .bigText("This is a test notification lorem ipsum lorem ipsum lorem ipsum lorem ipsum ")
                .build();

        notificationBuilder.setStyle(bigPictureStyle);
        notificationBuilder.setStyle(bigTextStyle);


        vibration();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        // Notification Lights
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        notificationManager.notify(Integer.parseInt(channelId), notificationBuilder.build());

        // Setting Flag Defaults for compatibility
        //.setDefaults(Notification.FLAG_INSISTENT);


    }

    private void vibration() {

        // Get instance of Vibrator from current Context
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Vibrate for 200 milliseconds
        // Sleep for 200 milliseconds
        //long[] VIBRATE_PATTERN = {0, 200, 200};
        long[] VIBRATE_PATTERN = {0, 300, 300};

        if (vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26 and above
                // Repeat pattern is how many times the vibration will occur -1 means it will occur only once
                vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
            } else {
                // Below API 26
                // Repeat pattern is how many times the vibration will occur -1 means it will occur only once
                vibrator.vibrate(VIBRATE_PATTERN, 0);
            }
        }

        // Stop the vibration after some time
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(vibrator::cancel, 1000);
    }
}
