package bridge;

import energia.SistemaEnergia;

public abstract class Drone {

    protected SistemaEnergia energia;

    public Drone(SistemaEnergia energia) {
        this.energia = energia;
    }

    public abstract void volar();

    public abstract void aterrizar();

    public void verificarBateria() {
        System.out.println("Nivel de energía: " + energia.nivelEnergia() + "%");
    }
}
