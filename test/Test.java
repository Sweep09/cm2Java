package test;

import cm2Java.Cm2Java;
import cm2Java.BlockTypes;

public class Test {
	public static void main(String[] args) {
		int blockSize = 50;
		Cm2Java save = new Cm2Java((int)Math.pow(blockSize, 3),(int)(Math.pow(blockSize, 3)*2));
		BlockTypes cm2 = new BlockTypes();

		long startTime = System.currentTimeMillis();
		for (int x=0; x<blockSize;x++) {
			for (int y=0;y<blockSize;y++) {
				for (int z=0;z<blockSize;z++) {
					save.createBlock(cm2.FLIPFLOP,true,x,y,z);
					//save.addConnection(x, y*2);
					//save.addConnection(y*2, z*3);
				}
			}
		}

		long creationEndTime = System.currentTimeMillis();
		save.exportSaveToFile("save.txt");
		long exportEndTime = System.currentTimeMillis();

		System.out.println("Blocks creation took: " + (creationEndTime - startTime)+ " ms");
	    System.out.println("Export took: " + (exportEndTime - creationEndTime) + " ms");
	    System.out.println("Total time: " + (exportEndTime - startTime) + " ms");
	}

}
