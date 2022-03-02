import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project2main {

	public static void main(String[] args) throws FileNotFoundException {
		//System.out.println(LocalTime.now()+" || Start");
		
		// get input data
		// scanner for input
		Scanner input = new Scanner(new File(args[0]));
		
		// record players to an array
		Player[] players = new Player[input.nextInt()];
		for(int i = 0; i < players.length; i++) {
			players[i] = new Player(input.nextInt(), input.nextInt());
		}
		
		// record events to a PQ
		PriorityQueue<Event> events = new PriorityQueue<Event>();
		for(int i = 0, s = input.nextInt(); i < s; i++) {
			input.nextLine();
			events.add(new Event("e"+input.next()+"q", players[input.nextInt()], Double.parseDouble(input.next()), Double.parseDouble(input.next()), -1));
		}
		
		// record the physiotherapists
		Physiotherapist[] physiotherapists = new Physiotherapist[input.nextInt()];
		for(int i = 0; i < physiotherapists.length; i++) {
			physiotherapists[i] = new Physiotherapist(i, Float.parseFloat(input.next()));
		}
		
		// record the coaches and masseurs and make all of them available (make true all indices)
		boolean[] coaches = new boolean[input.nextInt()];
		for(int i = 0; i < coaches.length; i++) {
			coaches[i] = true;
		}
		
		boolean[] masseurs = new boolean[input.nextInt()];
		for(int i = 0; i < masseurs.length; i++) {
			masseurs[i] = true;
		}
		
		// close input scanner
		input.close();
		
		// queues for training, massage, physiotherapy
		PriorityQueue<Event> trainingQueue = new PriorityQueue<Event>();
		PriorityQueue<Event> massageQueue = new PriorityQueue<Event>(1, new SkillLevelComparator());
		PriorityQueue<Event> physiotheraphyQueue = new PriorityQueue<Event>(1, new LastTrainingTimeComparator());
		
		// simulation
		
		double simulationClock = 0;
		int cancelledAttemptCounter = 0;
		int invalidAttemptCounter = 0;
		int maxOfTrainingQueue = 0;
		int maxOfPhysQueue = 0;
		int maxOfMassageQueue = 0;
		double totalTimeTraining = 0;
		double totalTimeMassage = 0;
		double totalTimePhys = 0;
		int totalCounterTraining = 0;
		int totalCounterMassage = 0;
		int totalCounterPhys = 0;
		double totalTimeTrainingQueue = 0;
		double totalTimeMassageQueue = 0;
		double totalTimePhysQueue = 0;
		double totalTimeTurnaround = 0;
		double maxOfPhysQueueTime = 0;
		int maxOfPhysQueuePlayerId = -1;
		int leastTimeThreeMassageId = -1;
		double leastTimeThreeMassageTime = -1;
		
		while(!events.isEmpty()) {
			// take next event
			Event currentEvent = events.poll();
			simulationClock = currentEvent.getTime();
			
			// execute the event
			switch(currentEvent.getType()) {
			case "etq":
				
				// check if player is available
				if(currentEvent.getPlayer().isAvailable()) {
					currentEvent.getPlayer().setAvailability(false);
					
					// keep statistics
					currentEvent.getPlayer().setLastTrainingQueueEntranceTime(simulationClock);
					
					// entering training queue operations
					
					// check whether queue is empty
					if(trainingQueue.isEmpty()) {
						
						// check employee available
						if(availableEmployee(coaches) != -1) {
							
							// add the start training event to the event queue
							events.add(new Event("st", currentEvent.getPlayer(), simulationClock, currentEvent.getDuration(), -1));
							
							break;
						}
					}
					
					// add to the queue
					trainingQueue.add(currentEvent);
				} else {
					cancelledAttemptCounter++;
				}
				
				break;
				
			case "emq":
				
				// check if attempt is not invalid
				if(currentEvent.getPlayer().isMassagable()) {
					
					// check if player is available
					if(currentEvent.getPlayer().isAvailable()) {
						currentEvent.getPlayer().setAvailability(false);
						
						// keep statistics
						currentEvent.getPlayer().setLastMassageQueueEntranceTime(simulationClock);
						
						// entering massage queue operations
						
						// check whether queue is empty
						if(massageQueue.isEmpty()) {
							
							// check employee available
							if(availableEmployee(masseurs) != -1) {
								
								// add the start training event to the event queue
								events.add(new Event("sm", currentEvent.getPlayer(), simulationClock, currentEvent.getDuration(), -1));
								currentEvent.getPlayer().massage();
								
								break;
							}
						}
						
						// add to the queue
						massageQueue.add(currentEvent);
						currentEvent.getPlayer().massage();
					} else {
						cancelledAttemptCounter++;
					}
				} else {
					invalidAttemptCounter++;
				}
				
				break;
				
			case "st":
				
				// starting training operations
				int availableCoachIndex = availableEmployee(coaches);
				coaches[availableCoachIndex] = false;
				
				// add the end training event to the event queue
				events.add(new Event("et", currentEvent.getPlayer(), simulationClock + currentEvent.getDuration(), 0, availableCoachIndex));
				currentEvent.getPlayer().train(currentEvent.getDuration());
				
				// statistics
				totalTimeTraining += currentEvent.getDuration();
				totalTimeTrainingQueue += (simulationClock - currentEvent.getPlayer().getLastTrainingQueueEntranceTime());
				totalCounterTraining++;
				
				break;
				
			case "sm":
				
				// starting massage operations
				int availableMasseurIndex = availableEmployee(masseurs);
				masseurs[availableMasseurIndex] = false;
				
				// add the end massage event to the event queue
				events.add(new Event("em", currentEvent.getPlayer(), simulationClock + currentEvent.getDuration(), 0, availableMasseurIndex));
				
				// statistics
				totalTimeMassage += currentEvent.getDuration();
				totalTimeMassageQueue += (simulationClock - currentEvent.getPlayer().getLastMassageQueueEntranceTime());
				currentEvent.getPlayer().setLeastMassageQueueTime(simulationClock - currentEvent.getPlayer().getLastMassageQueueEntranceTime());
				if(!currentEvent.getPlayer().isMassagable()) {
					///////
					if(leastTimeThreeMassageTime == -1) {
						leastTimeThreeMassageId = currentEvent.getPlayer().getId();
						leastTimeThreeMassageTime = currentEvent.getPlayer().getLeastMassageQueueTime();
					} else if(currentEvent.getPlayer().getLeastMassageQueueTime() < leastTimeThreeMassageTime) {
						leastTimeThreeMassageId = currentEvent.getPlayer().getId();
						leastTimeThreeMassageTime = currentEvent.getPlayer().getLeastMassageQueueTime();
					} else if(currentEvent.getPlayer().getLeastMassageQueueTime() == leastTimeThreeMassageTime) {
						if(currentEvent.getPlayer().getId() < leastTimeThreeMassageId)
							leastTimeThreeMassageId = currentEvent.getPlayer().getId();
					}
				}
				totalCounterMassage++;
				
				break;
				
			case "sp":
				
				// starting physiotheraphy operations
				int availablePhysiotherapistIndex = -1;
				for(int i = 0; i < physiotherapists.length; i++) {
					if(physiotherapists[i].isAvailable())
						availablePhysiotherapistIndex = i;
				}
				physiotherapists[availablePhysiotherapistIndex].setAvailability(false);
				
				// add the end physiotherapy event to the event queue
				events.add(new Event("ep", currentEvent.getPlayer(), simulationClock + physiotherapists[availablePhysiotherapistIndex].getServiceTime(), 0, availablePhysiotherapistIndex));
				
				// statistics
				totalTimePhys += physiotherapists[availablePhysiotherapistIndex].getServiceTime();
				totalTimePhysQueue += (simulationClock - currentEvent.getPlayer().getLastPhysQueueEntranceTime());
				currentEvent.getPlayer().incrementTotalPhysQueueTime(simulationClock - currentEvent.getPlayer().getLastPhysQueueEntranceTime());
				
				if(currentEvent.getPlayer().getTotalPhysQueueTime() > maxOfPhysQueueTime) {
					maxOfPhysQueueTime = currentEvent.getPlayer().getTotalPhysQueueTime();
					maxOfPhysQueuePlayerId = currentEvent.getPlayer().getId();
				} else if(currentEvent.getPlayer().getTotalPhysQueueTime() == maxOfPhysQueueTime) {
					if(maxOfPhysQueuePlayerId == -1 || currentEvent.getPlayer().getId() < maxOfPhysQueuePlayerId) {
						maxOfPhysQueuePlayerId = currentEvent.getPlayer().getId();
					}
				}
				totalCounterPhys++;
				
				break;
				
			case "et":
				
				// ending training operations
				coaches[currentEvent.getEmployeeIndex()] = true;
				
				// continue with the waiting event from the queue if exist
				if(!trainingQueue.isEmpty()) {
					Event waitingEvent = trainingQueue.poll();
					events.add(new Event("st", waitingEvent.getPlayer(), simulationClock, waitingEvent.getDuration(), -1));
				}
				
				// keep statistics
				currentEvent.getPlayer().setLastPhysQueueEntranceTime(simulationClock);
				
				// entering physiotherapy queue operations
				
				// check whether queue is empty
				if(physiotheraphyQueue.isEmpty()) {
					
					// check employee available
					int availableEmployeeIndex = -1;
					for(int i = 0; i < physiotherapists.length; i++) {
						if(physiotherapists[i].isAvailable())
							availableEmployeeIndex = i;
					}
					
					if(availableEmployeeIndex != -1) {
						
						// add the start physiotherapy event to the event queue
						events.add(new Event("sp", currentEvent.getPlayer(), simulationClock, physiotherapists[availableEmployeeIndex].getServiceTime(), -1));
						
						break;
					}
				}
				
				// add to the queue
				physiotheraphyQueue.add(currentEvent);
				
				break;
				
			case "em":
				
				// ending massage operations
				masseurs[currentEvent.getEmployeeIndex()] = true;
				currentEvent.getPlayer().setAvailability(true);
				
				// continue with the waiting event from the queue if exist
				if(!massageQueue.isEmpty()) {
					Event waitingEvent = massageQueue.poll();
					events.add(new Event("sm", waitingEvent.getPlayer(), simulationClock, waitingEvent.getDuration(), -1));
				}
				
				break;
				
			case "ep":
				
				// ending physiotheraphy operations
				physiotherapists[currentEvent.getEmployeeIndex()].setAvailability(true);
				currentEvent.getPlayer().setAvailability(true);
				
				// statistics
				totalTimeTurnaround += (simulationClock - currentEvent.getPlayer().getLastTrainingQueueEntranceTime());
				
				// continue with the waiting event from the queue if exist
				if(!physiotheraphyQueue.isEmpty()) {
					Event waitingEvent = physiotheraphyQueue.poll();
					events.add(new Event("sp", waitingEvent.getPlayer(), simulationClock, waitingEvent.getDuration(), -1));
				}
				
				break;
			}
			
			maxOfTrainingQueue = (trainingQueue.size() > maxOfTrainingQueue) ? trainingQueue.size() : maxOfTrainingQueue;
			maxOfMassageQueue = (massageQueue.size() > maxOfMassageQueue) ? massageQueue.size() : maxOfMassageQueue;
			maxOfPhysQueue = (physiotheraphyQueue.size() > maxOfPhysQueue) ? physiotheraphyQueue.size() : maxOfPhysQueue;
		}
		
		// output
		PrintStream output = new PrintStream(new File(args[1]));
		
		// test

		//System.out.println(LocalTime.now()+" || End");
		output.println(maxOfTrainingQueue);
		output.println(maxOfPhysQueue);
		output.println(maxOfMassageQueue);
		
		output.printf("%.3f\n", totalTimeTrainingQueue/totalCounterTraining);
		output.printf("%.3f\n", totalTimePhysQueue/totalCounterTraining);
		output.printf("%.3f\n", totalTimeMassageQueue/totalCounterMassage);
		
		output.printf("%.3f\n", totalTimeTraining/totalCounterTraining);
		output.printf("%.3f\n", totalTimePhys/totalCounterPhys);
		output.printf("%.3f\n", totalTimeMassage/totalCounterMassage);
		
		output.printf("%.3f\n", totalTimeTurnaround/totalCounterPhys);
		output.printf("%d %.3f\n", maxOfPhysQueuePlayerId, maxOfPhysQueueTime);
		if(leastTimeThreeMassageId == -1) {
			output.println(-1 + " " + -1);
		} else {
			output.printf("%d %.3f\n", leastTimeThreeMassageId, leastTimeThreeMassageTime);
		}
		
		output.println(invalidAttemptCounter);
		output.println(cancelledAttemptCounter);
		output.printf("%.3f\n", simulationClock);
	}
	
	public static int availableEmployee(boolean[] array) {
		for(int i = 0; i < array.length; i++) {
			if(array[i] == true)
				return i;
		}
		return -1;
	}
}

class SkillLevelComparator implements Comparator<Event>{

	@Override
	public int compare(Event o1, Event o2) {
		// TODO Auto-generated method stub
		if(o1.getPlayer().getSkillLevel() > o2.getPlayer().getSkillLevel()) {
			return -1;
		} else if (o1.getPlayer().getSkillLevel() < o2.getPlayer().getSkillLevel()) {
			return 1;
		} else {
			return o1.compareTo(o2);
		}
	}
	
}

class LastTrainingTimeComparator implements Comparator<Event>{

	@Override
	public int compare(Event o1, Event o2) {
		// TODO Auto-generated method stub
		double difference = o1.getPlayer().getLastTrainingTime() - o2.getPlayer().getLastTrainingTime();
		
		if(Math.abs(difference) < 0.0000000001) {
			return o1.compareTo(o2);
		} else if(difference < 0) {
			return 1;
		} else {
			return -1;
		}
	}
	
}