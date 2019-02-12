package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Encoder {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {

		//counts for symbols
		int[] counts = new int[256];
		
		//list to store nodes in the minimum variance tree
		List<MVNode> mvnode = new ArrayList<MVNode>();
		
		//file that is going to be compressed
		String path = "data/decodedfile.txt";
		File file = new File(path);
		int fileLength = (int) file.length();
		InputStream inputStream = new FileInputStream(path);
		InputStreamBitSource inputStreamBitSource = new InputStreamBitSource(inputStream);
		
		//getting counts for every symbol
		for (int i = 0; i < fileLength; i++) {
			counts[inputStreamBitSource.next(8)]++;
		}
		
		//adding nodes into the minimum variance tree list
		for (int i = 0; i < counts.length; i++) {
			mvnode.add(new MVNode(counts[i], i, 0));
		}
		
		//sorting the list of nodes in minimum variance list by number of counts
		MVNodeComparator mvncomparator = new MVNodeComparator();
		mvnode.sort(mvncomparator);
		
		//creating list of just the leaf nodes
		List<MVNode> leafList = new ArrayList<MVNode>(mvnode);
		
		//constructing the minimum variance tree
		while (mvnode.size() != 1) {
			MVNode right = mvnode.remove(mvnode.size() -1);
			MVNode left = mvnode.remove(mvnode.size() -1);
			int maxLR = Math.max(right.getHeight(), left.getHeight());
			int maxHeight = maxLR + 1;
			MVNode branch = new MVNode(right.getCounts() + left.getCounts(), left.getSymbol(), maxHeight);
			branch.left = left;
			branch.right = right;
			mvnode.add(branch);
			mvnode.sort(mvncomparator);
		}
		
		//getting lengths of the leaf nodes (leaf nodes are the nodes with symbols)
		mvnode.get(0).setLengths(0);
		
		//root node of canonical huffman tree
		Node root = new Node(0,0);
		
		//hashmap to store symbol(Integer) and its codeword(String)
		HashMap<Integer, String> symToCodeword = new HashMap<Integer, String>();
		
		//sorting list based on lengths of each symbol
		leafList.sort(new MVNodeCanonicalComparator());
		
		//constructing the canonical huffman tree, getting codewords for each symbol, and putting it into hashmap
		for (int i = 0; i < leafList.size(); i++) {
			String codeword = root.insertGetCode(leafList.get(i).getLength(), leafList.get(i).getSymbol(), "");
			symToCodeword.put(leafList.get(i).getSymbol(), codeword);
		}
		
		//output file path
		String outpath = "data/compressed.dat";
		FileOutputStream fos = new FileOutputStream(outpath);
		OutputStreamBitSink outputStreamBitSink = new OutputStreamBitSink(fos);
		
		//writing lengths of symbol
		for (int i = 0; i < 256; i++) {
			outputStreamBitSink.write(padZeros(8, Integer.toBinaryString(symToCodeword.get(i).length())));
		}
		
		//file length written
		outputStreamBitSink.write(padZeros(32, Integer.toBinaryString(fileLength)));
		
		//reopening and rereading file
		inputStream = new FileInputStream(path);
		InputStreamBitSource inputStreamBitSource2 = new InputStreamBitSource(inputStream);
		
		//writing codewords for each symbol read
		for (int i = 0; i < fileLength; i++) {
			String code = symToCodeword.get(inputStreamBitSource2.next(8));
			outputStreamBitSink.write(code);
		}
		
		outputStreamBitSink.padToWord();
	}
	
	//adding zeros to begining of binary strings based on number specified
	public static String padZeros(int numbits, String bits) {
		int numPad = numbits - bits.length();
		for (int i = 0; i < numPad; i++) {
			bits = "0" + bits;
		}
		return bits;
	}

}
