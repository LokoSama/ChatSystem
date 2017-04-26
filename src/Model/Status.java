package Model;

public enum Status {
	Online,
	Busy,
	Away,
	Offline;

	public String toString() {
		if (this == Online)
			return "Online";
		else if (this == Busy)
			return "Busy";
		else if (this == Away)
			return "Away";
		else
			return "Offline";
	}
	
	
}
