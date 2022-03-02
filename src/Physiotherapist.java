
public class Physiotherapist {
	
	// fields
	private int id;
	private float serviceTime;
	private boolean availability;
	
	// constructor
	public Physiotherapist(int id, float serviceTime) {
		this.id = id;
		this.serviceTime = serviceTime;
		this.availability = true;
	}
	
	// getters
	public boolean isAvailable() {
		return this.availability;
	}
	
	public float getServiceTime() {
		return this.serviceTime;
	}
	
	// setters
	public void setAvailability(boolean situation) {
		this.availability = situation;
	}
}
