package io;

import java.util.Comparator;

public class MVNodeCanonicalComparator implements Comparator<MVNode>{

	@Override
	public int compare(MVNode arg0, MVNode arg1) {
		if (arg0.getLength() > arg1.getLength()) {
			return 1;
		}
		if (arg0.getLength() == arg1.getLength()) {
			if (arg0.getSymbol() > arg1.getSymbol()) {
				return 1;
			}
			if (arg0.getSymbol() < arg1.getSymbol()) {
				return -1;
			}
		}
		return 0;
	} 	
}
