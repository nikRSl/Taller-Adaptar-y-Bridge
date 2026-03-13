package bridge;

import energia.SistemaEnergia;

public class DroneEntrega extends Drone {

    public DroneEntrega(SistemaEnergia energia) {
        super(energia);
    }

    public void volar() {
        energia.encenderSistema();
        System.out.println("Drone de entrega transportando paquete...");
    }

    public void aterrizar() {
        System.out.println("Drone aterrizando en zona segura.");
    }
}
