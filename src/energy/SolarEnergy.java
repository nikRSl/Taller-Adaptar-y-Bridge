package energy;

public class SolarEnergy implements SistemaEnergia {

    private int carga = 60;

    public void powerOnSystem() {
        System.out.println("Solar panels deployed.");
    }

    public int energyLevel() {
        carga += 2;
        return carga;
    }
}
