package adapter;

import legacy.LegacyDrone;

public class DroneAdapter implements DroneControl {

    private LegacyDrone droneAntiguo;

    public DroneAdapter(LegacyDrone droneAntiguo) {
        this.droneAntiguo = droneAntiguo;
    }

    public void turnOn() {
        droneAntiguo.startMotor();
    }

    public void fly() {
        droneAntiguo.legacyTakeOff();
    }

    public void land() {
        System.out.println("Drone antiguo aterrizando manualmente.");
    }

    public void checkBattery() {
        System.out.println("Combustible restante: " + droneAntiguo.checkFuel() + "%");
    }
}
