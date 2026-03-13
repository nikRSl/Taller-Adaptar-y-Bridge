package adapter;

import legacy.DroneAntiguo;

public class DroneAdapter implements ControlDrone {

    private DroneAntiguo droneAntiguo;

    public DroneAdapter(DroneAntiguo droneAntiguo) {
        this.droneAntiguo = droneAntiguo;
    }

    public void encender() {
        droneAntiguo.iniciarMotor();
    }

    public void volar() {
        droneAntiguo.despegarAntiguo();
    }

    public void aterrizar() {
        System.out.println("Drone antiguo aterrizando manualmente.");
    }

    public void verificarBateria() {
        System.out.println("Combustible restante: " + droneAntiguo.revisarCombustible() + "%");
    }
}
