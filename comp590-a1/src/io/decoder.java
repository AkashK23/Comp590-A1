package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class decoder {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		//list of symbols
		List<Symbol> symlist = new ArrayList<Symbol>();
		
		// root node of canonical huffman tree
		Node root = new Node(0,0);
		
		// input compressed data
		String path = "data/compressed (2).dat";
		String path2 = "data/compressed.dat";
		InputStream inputStream = new FileInputStream(path);
		InputStreamBitSource inputStreamBitSource = new InputStreamBitSource(inputStream);
		
		//getting symbol length, creating symbol object, and adding to symbol list
		for (int i = 0; i < 256; i++) {
			int length = inputStreamBitSource.next(8);
			Symbol symbol = new Symbol(length, i);
			symlist.add(symbol);
		}
		
		//sorting symbol list
		symlist.sort(new SymListComparator());
		
		//adding symbols to create canonical tree
		for (int i = 0; i < 256; i++) {
			root.insert(symlist.get(i).length, symlist.get(i).symbol);
		}
		
		//number of symbols to decode in file
		int numSymbols = inputStreamBitSource.next(32);
		
		//file where output is
		String outpath = "data/decodedfile.txt";
		FileOutputStream fos = new FileOutputStream(outpath);
		OutputStreamBitSink outputStreamBitSink = new OutputStreamBitSink(fos);
		
		//decoding code words into proper symbols using canonical huffman tree
		for (int i = 0; i < numSymbols; i++) {
			boolean foundLeaf = false;
			Node currentNode = root;
			while(!foundLeaf) {
				currentNode = currentNode.decodeSymbol(inputStreamBitSource.next(1));
				if (currentNode.isLeaf()) {
					foundLeaf = true;		
					String bits = padZeros(Integer.toBinaryString(currentNode.getSymbol()));
					outputStreamBitSink.write(bits);
				}
			}
		}
		outputStreamBitSink.padToWord();
	}
	
	
	//making binary values into bytes
	public static String padZeros(String bits) {
		int numPad = 8 - bits.length();
		for (int i = 0; i < numPad; i++) {
			bits = "0" + bits;
		}
		return bits;
	}
}
