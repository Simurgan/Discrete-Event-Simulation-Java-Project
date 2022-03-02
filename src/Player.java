
public class Player implements Comparable<Player>{
	
	// fields
	private int id;
	private int skillLevel;
	private double lastTrainingTime;
	private double lastTrainingQueueEntranceTime;
	private double lastPhysQueueEntranceTime;
	private double lastMassageQueueEntranceTime;
	private double cumulativeTrainingTime;
	private int massageCounter;
	private boolean availability;
	private double leastMassageQueueTime;
	private double totalPhysQueueTime;
	
	// constructor(s)
	public Player(int id, int skillLevel) {
		this.id = id;
		this.skillLevel = skillLevel;
		this.lastTrainingTime = 0;
		this.lastMassageQueueEntranceTime = 0;
		this.lastPhysQueueEntranceTime = 0;
		this.lastTrainingQueueEntranceTime = 0;
		this.cumulativeTrainingTime = 0;
		this.massageCounter = 0;
		this.availability = true;
		this.leastMassageQueueTime = 0;
		this.totalPhysQueueTime = 0;
	}
	
	public void train(double duration) {
		this.lastTrainingTime = duration;
		this.cumulativeTrainingTime += duration;
	}
	
	public void massage() {
		this.massageCounter++;
	}
	
	public boolean isMassagable() {
		return this.massageCounter < 3;
	}
	
	// setters
	public void setAvailability(boolean situation) {
		this.availability = situation;
	}
	
	public void setLastTrainingQueueEntranceTime(double time) {
		this.lastTrainingQueueEntranceTime = time;
	}
	
	public void setLastMassageQueueEntranceTime(double time) {
		this.lastMassageQueueEntranceTime = time;
	}
	
	public void setLastPhysQueueEntranceTime(double time) {
		this.lastPhysQueueEntranceTime = time;
	}
	
	public void setLeastMassageQueueTime(double duration) {
		this.leastMassageQueueTime += duration;
	}
	
	public void incrementTotalPhysQueueTime(double duration) {
		this.totalPhysQueueTime += duration;
	}
	
	// getters
	public double getTotalPhysQueueTime() {
		return this.totalPhysQueueTime;
	}
	
	public double getLeastMassageQueueTime() {
		return this.leastMassageQueueTime;
	}
	
	public double getLastTrainingQueueEntranceTime() {
		return this.lastTrainingQueueEntranceTime;
	}
	
	public double getLastMassageQueueEntranceTime() {
		return this.lastMassageQueueEntranceTime;
	}
	
	public double getLastPhysQueueEntranceTime() {
		return this.lastPhysQueueEntranceTime;
	}
	
	public boolean isAvailable() {
		return this.availability;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getSkillLevel() {
		return this.skillLevel;
	}
	
	public double getLastTrainingTime() {
		return this.lastTrainingTime;
	}

	@Override
	public int compareTo(Player o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
	
	
	
}
