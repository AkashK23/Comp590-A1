package io;

import java.util.List;

public class MVNode {
	int counts;
	int symbol;
	int height;
	int length;
	String name;
	MVNode left;
	MVNode right; 
	public MVNode(int counts, int symbol, int height) {
		this.counts = counts;
		this.symbol = symbol;
		this.height = height;
	}
	
	public int getCounts() {
		return counts;
	}
	public int getSymbol() {
		return symbol;
	}
	public int getHeight() {
		return height;
	}
	public int getLength() {
		return length;
	}
	
	// giving lengths to each leaf
	public void setLengths(int length) {
		if (left != null && right != null) {
			left.setLengths(length + 1);
			right.setLengths(length + 1);
		} else if (left != null) {
			left.setLengths(length + 1);
		} else if (right != null) {
			right.setLengths(length + 1);
		} else {
			this.length = length;
		}
	}
}
