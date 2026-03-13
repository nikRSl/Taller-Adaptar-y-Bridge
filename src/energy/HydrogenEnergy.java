package energy;

public class HydrogenEnergy implements SistemaEnergia {

    private int tanque = 100;

    public void powerOnSystem() {
        System.out.println("Hydrogen system activated.");
    }

    public int energyLevel() {
        tanque -= 8;
        return tanque;
    }
}
