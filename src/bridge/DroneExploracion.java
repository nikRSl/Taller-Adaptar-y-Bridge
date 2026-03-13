package bridge;

import energia.SistemaEnergia;

public class DroneExploracion extends Drone {

    public DroneExploracion(SistemaEnergia energia) {
        super(energia);
    }

    public void volar() {
        energia.encenderSistema();
        System.out.println("Drone explorando zona remota...");
    }

    public void aterrizar() {
        System.out.println("Drone explorador regresando a base.");
    }
}
