package cm2Java;

import java.text.DecimalFormat;

public class Block {
	public int blockID;
	public boolean state;
	public double x,y,z;
	public double[] properties;
	public boolean invalid = false;
	
	public static final DecimalFormat df = new DecimalFormat("0.#");
	
	public Block(int blockID, boolean state,double x,double y,double z) {
		this(blockID,state,x,y,z,null);
	}

	public Block(int blockID, boolean state,double x,double y,double z,double[] properties) {
		this.blockID = blockID;
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
		this.properties = properties;
	}
	
	public String toSaveString() {
		StringBuilder string = new StringBuilder(30);

		string.append(blockID).append(",");
		string.append(state ? "1":"").append(",");
		string.append(x>0? df.format(x):"").append(",");
		string.append(y>0? df.format(y):"").append(",");
		string.append(z>0? df.format(z):"").append(",");

		if (properties != null && properties.length > 0) {
			for (int i=0; i<properties.length; i++) {
				string.append(properties[i] > 0 ? df.format(properties[i]) : "");
				if (i < properties.length-1) {
					string.append("+");
				}
			}
		}
		
		return string.toString();
	}

}