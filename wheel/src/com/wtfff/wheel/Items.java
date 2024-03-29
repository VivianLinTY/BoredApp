package com.wtfff.wheel;

public class Items {
	private int first;
	
	private int count;

    public Items() {
        this(0, 0);
    }
	public Items(int first, int count) {
		this.first = first;
		this.count = count;
	}

	public int getFirst() {
		return first;
	}
	
	public int getLast() {
		return getFirst() + getCount() - 1;
	}

	public int getCount() {
		return count;
	}

	public boolean contains(int index) {
		return index >= getFirst() && index <= getLast();
	}
}