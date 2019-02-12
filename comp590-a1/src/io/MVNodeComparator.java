package io;

import java.util.Comparator;

public class MVNodeComparator implements Comparator<MVNode> {

	@Override
	public int compare(MVNode arg0, MVNode arg1) {
		if (arg0.getCounts() > arg1.getCounts()) {

			return -1;
		} else if (arg0.getCounts() == arg1.getCounts()) {
			if (arg0.getHeight() > arg1.getHeight()) {
				return -1;
			} else if (arg0.getHeight() < arg1.getHeight()) {
				return 1;
			} else if (arg0.getSymbol() > arg1.getSymbol()) {
				return 1;
			} else if (arg0.getSymbol() < arg1.getSymbol()) {
				return -1;
			}
			return 0;
		} else {
			return 1;
		}
	}

}
