package com.example.post_pc_ex8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    CalcAdapter adapter;
    CalcItemsHolder holder;
    EditText insertCalc;
    FloatingActionButton addCalcButton;
    Data.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.builder = new Data.Builder();
        this.insertCalc = findViewById(R.id.insertCalc);
        this.addCalcButton = findViewById(R.id.newCalcButton);
        this.recycler = findViewById(R.id.recyclerCalcItem);
        this.holder = new CalcItemsHolder(MainActivity.this);
        this.holder.initCalcList();
        this.adapter = new CalcAdapter(WorkManager.getInstance(MainActivity.this) ,this.holder);


        this.recycler.setAdapter(this.adapter);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.recycler.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        this.addCalcButton.setOnClickListener(v ->
        {
            try
            {
                long number = Long.parseLong(this.insertCalc.getText().toString());
                boolean isNumberExists = this.holder.checkIfCalcExists(number);
                if (!isNumberExists)
                {
                    calculate(new CalcItem(number), true);
                }
                else
                {
                    Toast.makeText(this, "Calc already exists!", Toast.LENGTH_SHORT).show();
                }
            }
            catch (NumberFormatException exception)
            {
                Toast.makeText(this, "Format Error", Toast.LENGTH_SHORT).show();
            }
        });

        for (CalcItem item: holder.items)
        {
            if (!item.status.equals("finished"))
            {
                calculate(item, false);
            }
        }
    }

    protected void calculate(CalcItem item, boolean newItem)
    {
        if (newItem)
        {
            this.holder.addCalcItem(item);
            this.holder.saveItemsList(this.holder.items);
            this.adapter.notifyDataSetChanged();
        }

        this.builder.putLong("number", item.getNumToCalc());
        this.builder.putLong("current_number", item.getCurNumber());
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CalcWorker.class).setInputData(this.builder.build()).build();
        item.setWorkerId(request.getId());
        LiveData<WorkInfo> info = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(request.getId());
        info.observeForever(v ->
        {
            if (v != null)
            {
                WorkInfo.State state = v.getState();
                Data data = v.getOutputData();
                int progress = data.getInt("progress", 0);
                if (state == WorkInfo.State.SUCCEEDED)
                {
                    item.setRoot1(data.getLong("root1", 0));
                    item.setRoot1(data.getLong("root2", 0));
                    item.setStatus("finished");
                    item.setProgress(100);
                    Collections.sort(holder.items);
                    holder.saveItemsList(holder.items);
                    adapter.notifyDataSetChanged();
                    CalcViewHolder calcView = (CalcViewHolder) recycler.findViewHolderForLayoutPosition(holder.items.indexOf(item));
                    if (calcView != null)
                    {
                        calcView.setCalcCompleted(item);
                    }
                }
                else
                {
                    boolean isPrime = data.getBoolean("isPrime", false);
                    if (isPrime)
                    {
                        adapter.notifyDataSetChanged();
                        item.setPrime(true);
                        holder.saveItemsList(holder.items);
                        CalcViewHolder calcView = (CalcViewHolder) recycler.findViewHolderForLayoutPosition(holder.items.indexOf(item));
                        if (calcView != null)
                        {
                            calcView.setCalcCompleted(item);
                        }
                    }
                }
            }
        });

    }
}