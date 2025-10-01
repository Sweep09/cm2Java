package cm2Java;

public class Connection {
	// should be block index in blocks array
	int target;
	int source;
	boolean invalid = false;

	public Connection(int source, int target) {
		this.target = target;
		this.source = source;
	}

	public String toSaveString() {
		return source + "," + target;
	}
}
