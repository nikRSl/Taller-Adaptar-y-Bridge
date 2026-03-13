package bridge;

import energy.SistemaEnergia;

public abstract class Drone {

    protected SistemaEnergia energy;

    public Drone(SistemaEnergia energia) {
        this.energy = energia;
    }

    public abstract void fly();

    public abstract void land();

    public void checkBattery() {
        System.out.println("Energy level: " + energy.energyLevel() + "%");
    }
}
