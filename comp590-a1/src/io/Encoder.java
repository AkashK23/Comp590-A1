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
		// TODO Auto-generated method stub
		int[] counts = new int[256];
		List<MVNode> mvnode = new ArrayList<MVNode>();
		String path = "data/decodedfile.txt";
		File file = new File(path);
		int fileLength = (int) file.length();
		InputStream inputStream = new FileInputStream(path);
		InputStreamBitSource inputStreamBitSource = new InputStreamBitSource(inputStream);
		
		for (int i = 0; i < fileLength; i++) {
			counts[inputStreamBitSource.next(8)]++;
		}
		
		for (int i = 0; i < counts.length; i++) {
			mvnode.add(new MVNode(counts[i], i, 0));
		}
		
		MVNodeComparator mvncomparator = new MVNodeComparator();
		mvnode.sort(mvncomparator);
		List<MVNode> leafList = new ArrayList<MVNode>(mvnode);
		
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

		mvnode.get(0).setLengths(0);
		Node root = new Node(0,0);
		HashMap<Integer, String> symToCodeword = new HashMap<Integer, String>();
		
		leafList.sort(new MVNodeCanonicalComparator());
		
		for (int i = 0; i < leafList.size(); i++) {
			String codeword = root.insertGetCode(leafList.get(i).getLength(), leafList.get(i).getSymbol(), "");
			symToCodeword.put(leafList.get(i).getSymbol(), codeword);
		}
		
		
		String outpath = "data/compressed.dat";
		FileOutputStream fos = new FileOutputStream(outpath);
		OutputStreamBitSink outputStreamBitSink = new OutputStreamBitSink(fos);
		for (int i = 0; i < 256; i++) {
			outputStreamBitSink.write(padZeros(8, Integer.toBinaryString(symToCodeword.get(i).length())));
		}
		
		outputStreamBitSink.write(padZeros(32, Integer.toBinaryString(fileLength)));
		inputStream = new FileInputStream(path);
		InputStreamBitSource inputStreamBitSource2 = new InputStreamBitSource(inputStream);
		
		for (int i = 0; i < fileLength; i++) {
			String code = symToCodeword.get(inputStreamBitSource2.next(8));
			outputStreamBitSink.write(code);
		}
		
		outputStreamBitSink.padToWord();
	}
	
	public static String padZeros(int numbits, String bits) {
		int numPad = numbits - bits.length();
		for (int i = 0; i < numPad; i++) {
			bits = "0" + bits;
		}
		return bits;
	}

}
