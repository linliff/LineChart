package com.linlif.linechart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.linlif.linechart.view.LineChartView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChartView lineChartView = findViewById(R.id.lineChart);


        List<String> xdate = getXData();


        List<Integer> ydata = new ArrayList<>();

        ydata.add(80);
        ydata.add(100);
        ydata.add(120);
        ydata.add(140);
        ydata.add(160);
        ydata.add(180);
        ydata.add(200);

        List<Float> data = new ArrayList<>();

        data.add(80f);
        data.add((float) 100);
        data.add((float) 180);
        data.add((float) 90);
        data.add((float) 100);
        data.add((float) 110);
        data.add((float) 120);
        data.add((float) 140);
        data.add((float) 170);


        lineChartView.setData(xdate, ydata, data);

    }

    /**
     * 获取X轴日期数据
     *
     * @param
     * @return
     */

    int mDays = 60;

    private List<String> getXData() {

        List<String> xdate = new ArrayList<>();

        long currentTime = System.currentTimeMillis();

        long lastTime = (long) 1572249131 * 1000;

        if (lastTime > (currentTime - mDays * ONE_DAY_MILLIS)) {

            mDays = (int) Math.ceil((double) (currentTime - lastTime) / ONE_DAY_MILLIS);

        }

        for (int i = 0; i < mDays; i++) {

            String time = formatMMDDTime(currentTime - i * ONE_DAY_MILLIS);
            xdate.add(time);
        }

        return xdate;
    }

    public static String formatMMDDTime(long mills) {

        return new SimpleDateFormat("MM-dd").format(mills);
    }
}
