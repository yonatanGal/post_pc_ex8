package com.example.post_pc_ex8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.Data;

public class CalcWorker extends Worker {
    public CalcWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("progress", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        Data.Builder builder = new Data.Builder();
        

    }
}
