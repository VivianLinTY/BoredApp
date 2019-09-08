package com.wtfff.wheel.listener;

import com.wtfff.wheel.WheelView;

public interface WheelChangeListener {

	void onChanged(WheelView wheel, int oldValue, int newValue);
}
