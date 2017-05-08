package Model;

public enum Status {
	Away,
	Busy,
	Offline,
	Online;

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
