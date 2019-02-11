package io;

public class Node {
	boolean isLeaf;
	boolean isFull;
	Node left;
	Node right;
	int symbol;
	// int length;

	public Node(int length, int symbol) {
		// this.length = length;
		if (length == 0) {
			this.symbol = symbol;
		}
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public boolean isFull() {
		return isFull;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public int getSymbol() {
		return symbol;
	}

	public Node decodeSymbol(int bit) {
		// if (this.isLeaf) {
		// return this;
		// }
		if (bit == 0) {
			return left;
		} else {
			return right;
		}
	}

	public void insert(int length, int symbol) {
		if (length == 0) {
			isFull = true;
			isLeaf = true;
			return;
		}
		if (left == null) {
			left = new Node(length - 1, symbol);
			left.insert(length - 1, symbol);
			return;
		} else if (!left.isFull()) {
			left.insert(length - 1, symbol);
			return;
		} else if (right == null) {
			right = new Node(length - 1, symbol);
			right.insert(length - 1, symbol);
			if (right.isFull()) {
				isFull = true;
			}
			return;
		} else if (!right.isFull()) {
			right.insert(length - 1, symbol);
			if (right.isFull()) {
				isFull = true;
			}
			return;
		}
	}

	public String insertGetCode(int length, int symbol, String codeword) {
		if (length == 0) {
			isFull = true;
			isLeaf = true;
			return codeword;
		}
		if (left == null) {
			codeword = codeword + 0;
			left = new Node(length - 1, symbol);
			return left.insertGetCode(length - 1, symbol, codeword);

		} else if (!left.isFull()) {
			codeword = codeword + 0;
			return left.insertGetCode(length - 1, symbol, codeword);
		} else if (right == null) {
			right = new Node(length - 1, symbol);
			codeword = codeword + 1;
			String receivedCodeword = right.insertGetCode(length - 1, symbol, codeword);
			if (right.isFull()) {
				isFull = true;
			}
			return receivedCodeword;
		} else if (!right.isFull()) {
			codeword = codeword + 1;
			String receivedCodeword = right.insertGetCode(length - 1, symbol, codeword);
			if (right.isFull()) {
				isFull = true;
			}
			return receivedCodeword;
		}
		return "";
	}
}
