package manu.apps.androidcodingstarterpack.workmanagers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ToastRestartWorker extends Worker {

    public ToastRestartWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Toast.makeText(getApplicationContext(), "Showing Toast After Restart", Toast.LENGTH_LONG).show();

        return Result.success();
    }
}
