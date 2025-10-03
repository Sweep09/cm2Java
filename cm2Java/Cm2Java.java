package cm2Java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/** use Eclipse IDE for larger saves for better performance if using VSCode*/
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
	public int createBlock(int blockID, boolean state, float x, float y, float z) {
	    return createBlock(blockID, state, x, y, z, null);
	}

	/** Returns block index on success otherwise -1*/
	public int createBlock(int blockID, boolean state,float x,float y,float z,float[] properties) {
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
				blocks[i].toSaveString(saveString);
				if (i != blockCount-1)
					saveString.append(";");
			}
		}
		saveString.append("?");

		for (int i=0; i < connectionCount; i++) {
			if (!connections[i].invalid) {
				connections[i].toSaveString(saveString);
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
			StringBuilder saveString = new StringBuilder(blockCount*70 + connectionCount*20+3);

			for (int i=0; i < blockCount; i++) {
				if (!blocks[i].invalid) {
					blocks[i].toSaveString(saveString);
					if (i != blockCount-1)
						saveString.append(";");
				
				}
			}
			saveString.append("?");

			for (int i=0; i < connectionCount; i++) {
				if (!connections[i].invalid) {
					connections[i].toSaveString(saveString);
					if (i != connectionCount-1)
						saveString.append(";");
				}
			}
			saveString.append("?");
//		skip buildings
			saveString.append("?");

			writer.write(saveString.toString());
		}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	class Block {
		public int blockID;
		public boolean state;
		public float x,y,z;
		public float[] properties;
		public boolean invalid = false;
		
		public static final DecimalFormat df = new DecimalFormat("0.#");
		
		public Block(int blockID, boolean state,float x,float y,float z) {
			this(blockID,state,x,y,z,null);
		}

		public Block(int blockID, boolean state,float x,float y,float z,float[] properties) {
			this.blockID = blockID;
			this.state = state;
			this.x = x;
			this.y = y;
			this.z = z;
			this.properties = properties;
		}
		
		public void toSaveString(StringBuilder string) {
			string.append(blockID).append(",");
			string.append(state ? "1":"").append(",");
			string.append(x>0? df.format(x):"").append(",");
			string.append(y>0? df.format(y):"").append(",");
			string.append(z>0? df.format(z):"").append(",");

			if (properties != null && properties.length > 0) {
				for (int i=0; i<properties.length; i++) {
					string.append(properties[i] > 0 ? df.format(properties[i]) : "");
					if (i < properties.length-1)
						string.append("+");
				}
			}
		}

	}
	
	class Connection {
		// should be block index in blocks array
		int target;
		int source;
		boolean invalid = false;

		public Connection(int source, int target) {
			this.target = target;
			this.source = source;
		}

		public void toSaveString(StringBuilder string) {
			string.append(source + "," + target);
		}
	}

}
