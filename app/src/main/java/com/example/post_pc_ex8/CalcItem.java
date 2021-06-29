package com.example.post_pc_ex8;

import java.io.Serializable;

public class CalcItem implements Comparable<CalcItem>, Serializable {

    long numToCalc;
    long root1;
    long root2;
    int progress;
    String status;
    boolean isPrime;

    public CalcItem(long numToCalc)
    {
        this.numToCalc = numToCalc;
        this.status = "waiting";
        this.progress = 0;
        this.root1 = -1;
        this.root2 = -1;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public boolean isPrime()
    {
        return this.isPrime;
    }

    public String getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public long getNumToCalc() {
        return numToCalc;
    }

    public long getRoot1() {
        return root1;
    }

    public long getRoot2() {
        return root2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRoot1(long root1) {
        this.root1 = root1;
    }

    public void setRoot2(long root2) {
        this.root2 = root2;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void addOneToProgress()
    {
        this.progress += 1;
    }


    @Override
    public int compareTo(CalcItem o) {
        if (this.status.equals("finished") && !o.status.equals("finished"))
        {
            return 1;
        }

        else if (!this.status.equals("finished") && o.status.equals("finished"))
        {
            return -1;
        }

        else if (this.status.equals("in progress") && !o.status.equals("in progress"))
        {
            return -1;
        }

        else if (!this.status.equals("in progress") && o.status.equals("in progress"))
        {
            return 1;
        }

        return -1;
    }
}
