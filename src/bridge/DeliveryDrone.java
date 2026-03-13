package bridge;

import energy.SistemaEnergia;

public class DeliveryDrone extends Drone {

    public DeliveryDrone(SistemaEnergia energia) {
        super(energia);
    }

    public void fly() {
        energy.powerOnSystem();
        System.out.println("Delivery drone transporting package...");
    }

    public void land() {
        System.out.println("Drone landing in safe zone.");
    }
}
