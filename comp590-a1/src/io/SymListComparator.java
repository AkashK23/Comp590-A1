package io;
import java.util.Comparator;

class SymListComparator implements Comparator<Symbol>{
		@Override
		public int compare(Symbol arg0, Symbol arg1) {
			if (arg0.getLength() > arg1.getLength()) {
				return 1;
			} else if (arg0.getLength() == arg1.getLength()) {
				if (arg0.getSymbol() > arg1.getSymbol()) {
					return 1;
				}
				return 0;
			} else {
				return -1;
			}
		} 	
}