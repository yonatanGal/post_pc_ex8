package com.example.post_pc_ex8;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class CalcItemsHolder extends Activity {
    ArrayList<CalcItem> items;
    SharedPreferences sp;
    Context context;


    public CalcItemsHolder(Context context)
    {
        this.context = context;
        this.sp = this.context.getSharedPreferences("calc_sp", Context.MODE_PRIVATE);
        initCalcList();
    }

    public void initCalcList()
    {
        this.items = new ArrayList<>();
        String itemsString = this.sp.getString("list", null);
        if (itemsString != null)
        {
            Type listType = new TypeToken<ArrayList<CalcItem>>(){}.getType();
            this.items = new Gson().fromJson(itemsString, listType);
        }
    }

    public void saveItemsList(ArrayList<CalcItem> calcItems)
    {
        this.items = calcItems;
        String itemsString = new Gson().toJson(calcItems);
        this.sp.edit().putString("list", itemsString).apply();
    }

    public void addCalcItem(CalcItem item)
    {
        this.items.add(item);
        Collections.sort(this.items);
        saveItemsList(this.items);
    }

    public void removeCalcItem(CalcItem item)
    {
        this.items.remove(item);
        Collections.sort(this.items);
        saveItemsList(this.items);
    }

    public boolean checkIfCalcExists(long number)
    {
        for (CalcItem item: this.items)
        {
            if (item.numToCalc == number)
            {
                return true;
            }
        }
        return false;
    }



}
