package com.wtfff.boredApp;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class StatistActivity extends AbstractActivity{
	
	private LinearLayout layout_renderer;
	int[] colors = new int[] { Color.RED, Color.BLUE};
	double[] values;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statist_main);
        layout_renderer=(LinearLayout) findViewById(R.id.layout_renderer);
        values=new double[] { 5 , 100 };
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setZoomButtonsVisible(false);
        renderer.setZoomEnabled(false);
        View view = ChartFactory.getPieChartView(this, buildCategoryDataset("Statist", values), renderer);
        view.setBackgroundColor(Color.BLACK);
        layout_renderer.addView(view);
	}
	
	private DefaultRenderer buildCategoryRenderer(int[] colors) {
		float rate = 0.7f;
        DefaultRenderer renderer = new DefaultRenderer();
//        renderer.setDisplayValues(true);
        renderer.setShowLabels(true);
        renderer.setLabelsColor(Color.WHITE);   //圖上數字的顏色
        renderer.setLabelsTextSize(40);  //圖上的文字
        renderer.setShowLegend(false);    //
//        renderer.setLegendTextSize(15);   //bar的文字
        renderer.setChartTitle("Statist");
        renderer.setChartTitleTextSize(80);
        renderer.setStartAngle(30f);
        renderer.setScale(rate);
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
	
	private CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        series.add(" choose("+Math.round(values[0])+") ",values[0]);
        series.add(" watch("+Math.round(values[1])+") ",values[1]);
//        int k = 0;
//        for (double value : values) {
//            series.add("Project " + ++k + " (" + value + ")", value);
//        }
        return series;
    }

}
