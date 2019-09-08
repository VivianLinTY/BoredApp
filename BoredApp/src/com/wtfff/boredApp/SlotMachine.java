package com.wtfff.boredApp;

import com.wtfff.boredApp.utility.StatusRecord;
import com.wtfff.wheel.WheelView;
import com.wtfff.wheel.adapters.WheelAdapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class SlotMachine{
	private static final String TAG="WTFFF_SlotMachine";
    Context mContext;
    int height = 0;
    float density=0;
    public void initWheel(WheelView wheel, Context context, float density, int high) {
    	this.mContext=context;
    	this.density=density;
    	height=Math.round(high*8/10);
    	wheel.setItemHeight(height);
        wheel.setViewAdapter(new SlotMachineAdapter());
        int location = (int)(Math.random() * 10);
        wheel.setCurrentItem(location);
        
        wheel.setCyclic(true);
        wheel.setEnabled(false);
    }
    
    public String getString (int location){
    	return StatusRecord.do_list_slot.get(location).getTitle();
    }
   
    private class SlotMachineAdapter extends WheelAdapter {
        public SlotMachineAdapter() {
        }

        @Override
        public int getItemsCount() {
            return StatusRecord.do_list_slot.size();
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
        	float dp = height/density;
        	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            TextView tv;
            String title=StatusRecord.do_list_slot.get(index).getTitle();
            if (cachedView != null) {
            	tv = (TextView) cachedView;
            } else {
            	tv = new TextView(mContext);
            }
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            int margin_h = Math.round(height/10);
            tv.setPadding(margin_h, 0, margin_h, 0);
            tv.setTextColor(Color.parseColor("#727272"));
            tv.setEllipsize(TruncateAt.END);
            tv.setMaxEms(8);
            tv.setTextSize(dp/3);
            if(title.getBytes().length>54)
            {
            	title=title.substring(0, 16)+"...";
            }
        	tv.setText(title);
            return tv;
        }
    }
}
