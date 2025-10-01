package cm2Java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Cm2Java {

	private Block[] blocks;
	private Connection[] connections;

	private int blockCount = 0;
	private int connectionCount = 0;

	public int totalBlockAttempts = 0;
	public int totalConnectionAttempts = 0;

	public Cm2Java(int blockQuantity, int maxConnections) {
		if (blockQuantity <= 0) {
			System.err.print("Invalid block amount");
			System.exit(1);
		}
		this.blocks = new Block[blockQuantity];
		this.connections = new Connection[maxConnections];
	}

	/** Returns block index on success otherwise -1*/
	public int createBlock(int blockID, boolean state, double x, double y, double z) {
	    return createBlock(blockID, state, x, y, z, null);
	}

	/** Returns block index on success otherwise -1*/
	public int createBlock(int blockID, boolean state,double x,double y,double z,double[] properties) {
		totalBlockAttempts++;
		if (blockCount >= blocks.length) {
			return -1;
		}

		blocks[blockCount] = new Block(blockID,state,x,y,z,properties);
		return blockCount++;
	}

	/** Returns block index on success otherwise -1*/
	public int addConnection(int source, int target) {
		totalConnectionAttempts++;
		if (connectionCount >= connections.length)
			return -1;
//		0 indexed -> 1 indexed
		connections[connectionCount] = new Connection(source+1, target+1);
		return connectionCount++;
	}

	/** Returns block index on success otherwise -1*/
	public int addConnectionLast(int target) {
		return addConnection(blockCount-1, target);
	}

	/** May result in indexing errors */
	public void deleteConnection(int idx) {
		connections[idx].invalid = true;
	}

	/** Does not delete connections.
	 *  May result in indexing errors */
	public void deleteBlock(int idx) {
//		will this affect editing blocks?
		blocks[idx].invalid = true;
	}

	public String exportSave() {
		StringBuilder saveString = new StringBuilder(blockCount*70 + connectionCount*20+3);

		for (int i=0; i < blockCount; i++) {
			if (!blocks[i].invalid) {
				saveString.append(blocks[i].toSaveString());
				if (i != blockCount-1)
					saveString.append(";");
			}
		}
		saveString.append("?");

		for (int i=0; i < connectionCount; i++) {
			if (!connections[i].invalid) {
				saveString.append(connections[i].toSaveString());
				if (i != connectionCount-1)
					saveString.append(";");
			}
		}
		saveString.append("?");

//		skip buildings
		saveString.append("?");

		return saveString.toString();
	}

	/** Dev's recommendation*/
	public void exportSaveToFile(String filename) {
		int bufferSize = 16384;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename), bufferSize)) {

			for (int i=0; i < blockCount; i++) {
				if (!blocks[i].invalid) {
					writer.write(blocks[i].toSaveString());
					if (i != blockCount-1)
						writer.write(";");
				}
			}
			writer.write("?");

			for (int i=0; i < connectionCount; i++) {
				if (!connections[i].invalid) {
					writer.write(connections[i].toSaveString());
					if (i != connectionCount-1)
						writer.write(";");
				}
			}
			writer.write("?");

//		skip buildings
			writer.write("?");

		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
