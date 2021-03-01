package manu.apps.androidcodingstarterpack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    NavController navController;

    //private final int UPDATE_REQUEST_CODE = 1111;

    private static final int UPDATE_REQUEST_CODE = 100;

    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_main_fragment, R.id.nav_sms_sender_one_time,
                R.id.nav_sms_retriever_one_time)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //checkAppUpdate();
        checkAppUpdate();
    }

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();

    }

//    private void checkAppUpdate(){
//
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
//
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // For a flexible update, use AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//
//                // request update
//                try {
//
//                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,
//                            MainActivity.this, UPDATE_REQUEST_CODE);
//
//                } catch (IntentSender.SendIntentException e) {
//
//                    e.printStackTrace();
//
//                }
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == UPDATE_REQUEST_CODE){
//
//            Log.wtf("AppUpdateStartDownloadStatus: +++++++++++++++++++++++++++", "success");
//
//            if (resultCode != RESULT_OK){
//
//                Log.wtf("AppUpdateStartDownloadProgressStatus: +++++++++++++++++++++++++++", "failed");
//
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        checkAppUpdate();
//    }

    private void checkAppUpdate(){

        Toast.makeText(this, "Check App Update Called", Toast.LENGTH_LONG).show();

        appUpdateManager = AppUpdateManagerFactory.create(this);

        appUpdateManager.registerListener(installStateUpdatedListener);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    // Test with flexible update
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {

                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE){

            Toast.makeText(this, "AppUpdateStartDownloadProgressStatus: success", Toast.LENGTH_LONG).show();

            Log.wtf("AppUpdateStartDownloadStatus: +++++++++++++++++++++++++++", "success");

            if (resultCode != RESULT_OK){

                Toast.makeText(this, "AppUpdateStartDownloadProgressStatus: failed", Toast.LENGTH_LONG).show();

                Log.wtf("AppUpdateStartDownloadProgressStatus: +++++++++++++++++++++++++++", "failed");

            }
        }
    }

    private final InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        // Show module progress, log state, or install the update.
        if(installState.installStatus() == InstallStatus.INSTALLING){

            Toast.makeText(this, "Update is installing", Toast.LENGTH_LONG).show();

        }

        else if (installState.installStatus() == InstallStatus.INSTALLED){

            Toast.makeText(this, "Update is installed", Toast.LENGTH_LONG).show();

        }

        // Update has been downloaded for flexible updates
//        else if (installState.installStatus() == InstallStatus.DOWNLOADED) {
//            // After the update is downloaded, show a notification
//            // and request user confirmation to restart the app.
//            Toast.makeText(this, "Update has been downloaded", Toast.LENGTH_LONG).show();
//        }
    };

    private void checkNewAppVersionState() {

        Toast.makeText(this, "Check New App Version State Called", Toast.LENGTH_LONG).show();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    //FLEXIBLE:
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
//                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                        popupAlerter();
//                        Log.d(TAG, "checkNewAppVersionState(): resuming flexible update. Code: " + appUpdateInfo.updateAvailability());
//                    }

                    if (appUpdateInfo.installStatus() == InstallStatus.INSTALLED){

                        Toast.makeText(this, "Update has been installed", Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void unregisterListener() {

        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);

    }

    @Override
    protected void onDestroy() {
        unregisterListener();
        super.onDestroy();
    }
    @Override
    protected void onResume() {

        super.onResume();
        checkNewAppVersionState();
    }
}