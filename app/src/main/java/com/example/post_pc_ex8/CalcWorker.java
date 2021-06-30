package com.example.post_pc_ex8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.Data;

public class CalcWorker extends Worker {
    Data.Builder builder;
    public CalcWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("progress", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        builder = new Data.Builder();
        long startingTime = System.currentTimeMillis();
        long number = getInputData().getLong("number", 0);
        long currentNumber = getInputData().getLong("current_number", 2);
        for (long i = currentNumber; i < number / 2 + 1; i++)
        {
            long timePassed = System.currentTimeMillis() - startingTime;
            int curProgress = (int) (currentNumber / number) * 100;
            setProgressAsync(new Data.Builder().putInt("progress", curProgress).build());
            if (timePassed > 60000)
            {
                this.builder.putLong("number", number);
                this.builder.putLong("current_number", i);
                this.builder.putInt("progress", curProgress);
                return Result.failure(this.builder.build());
            }

            if (number % i == 0)
            {
                this.builder.putLong("root1", i);
                this.builder.putLong("root2", number / i);
                this.builder.putLong("number", number);
                this.builder.putInt("progress", curProgress);
                this.builder.putBoolean("isPrime", false);
                return Result.success(this.builder.build());
            }


        }
        this.builder.putLong("number", number);
        this.builder.putBoolean("isPrime", true);
        return Result.failure(this.builder.build());

    }
}
