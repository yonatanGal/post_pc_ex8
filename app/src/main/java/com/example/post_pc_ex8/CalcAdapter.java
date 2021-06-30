package com.example.post_pc_ex8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import java.io.Serializable;

class CalcViewHolder extends RecyclerView.ViewHolder implements Serializable {
    TextView calcText;
    ImageButton deleteButton;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;

    public CalcViewHolder(@NonNull View itemView) {
        super(itemView);
        this.calcText = itemView.findViewById(R.id.calc_text);
        this.deleteButton = itemView.findViewById(R.id.delete_button);
        this.progressBar = itemView.findViewById(R.id.progress_bar);
        this.constraintLayout = itemView.findViewById(R.id.calc_item);
    }

    public void setCalcCompleted(CalcItem item)
    {
        this.calcText.setText(item.getCalcDetails());
        this.progressBar.setVisibility(View.GONE);
    }

    public void setProgressBar(int progressLevel)
    {
        progressBar.setProgress(progressLevel);
    }
}

class CalcAdapter extends RecyclerView.Adapter<CalcViewHolder>
{

    public CalcItemsHolder itemsHolder;
    public WorkManager workManager;

    public CalcAdapter(WorkManager workManager, CalcItemsHolder itemsHolder)
    {
        this.itemsHolder = itemsHolder;
        this.workManager = workManager;
    }

    @NonNull
    @Override
    public CalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calc_item, parent, false);
        return new CalcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalcViewHolder holder, int position) {
        CalcItem item =this.itemsHolder.items.get(position);
        holder.calcText.setText(item.getCalcDetails());
        holder.deleteButton.setOnClickListener(v ->
        {
           if (!item.getStatus().equals("finished"))
           {
               workManager.cancelWorkById(item.getGetWorkerId());
           }
           this.itemsHolder.removeCalcItem(item);
           notifyDataSetChanged();
        });
        if (item.getStatus().equals("finished"))
        {
            holder.setCalcCompleted(item);
        }
        else
        {
            holder.setProgressBar(item.getProgress());
        }
    }

    @Override
    public int getItemCount() {
        return this.itemsHolder.items.size();
    }
}