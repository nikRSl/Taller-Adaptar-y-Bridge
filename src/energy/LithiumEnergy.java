package energy;

public class LithiumEnergy implements SistemaEnergia {

    private int bateria = 80;

    public void powerOnSystem() {
        System.out.println("Lithium system powered on.");
    }

    public int energyLevel() {
        bateria -= 5;
        return bateria;
    }
}
