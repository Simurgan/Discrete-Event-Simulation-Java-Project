
public class Event implements Comparable<Event>{
	
	// fields
	private String type;
	private Player player;
	private double time;
	private double duration;
	private int employeeIndex;
	
	// constructors
	public Event(String type, Player player, double time, double duration, int employeeIndex) {
		this.type = type;
		this.player = player;
		this.time = time;
		this.duration = duration;
		this.employeeIndex = employeeIndex;
	}

	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		double difference = this.time - o.time;
		
		if(Math.abs(difference) < 0.0000000001) {
			return this.player.compareTo(o.player);
		} else if(difference < 0) {
			return -1;
		} else {
			return 1;
		}
	}
	
	// getters
	public int getEmployeeIndex() {
		return this.employeeIndex;
	}
	public String getType() {
		return this.type;
	}
	
	public double getTime() {
		return this.time;
	}
	
	public double getDuration() {
		return this.duration;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
