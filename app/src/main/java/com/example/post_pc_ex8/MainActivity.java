package com.example.post_pc_ex8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        this.insertCalc = findViewById(R.id.insertCalc);
        this.addCalcButton = findViewById(R.id.newCalcButton);
        this.recycler = findViewById(R.id.recyclerCalcItem);
        this.adapter = new CalcAdapter(WorkManager.getInstance(MainActivity.this) ,this.holder);
        this.holder = new CalcItemsHolder(MainActivity.this);
        this.holder.initCalcList();

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
                    //todo: create worker, etc.
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
                //todo: continue calc
            }
        }
    }
}