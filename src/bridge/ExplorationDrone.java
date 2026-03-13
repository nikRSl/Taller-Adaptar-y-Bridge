package bridge;

import energy.SistemaEnergia;

public class ExplorationDrone extends Drone {

    public ExplorationDrone(SistemaEnergia energia) {
        super(energia);
    }

    public void fly() {
        energy.powerOnSystem();
        System.out.println("Exploration drone surveying remote area...");
    }

    public void land() {
        System.out.println("Exploration drone returning to base.");
    }
}
