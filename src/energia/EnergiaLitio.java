package energia;

public class EnergiaLitio implements SistemaEnergia {

    private int bateria = 80;

    public void encenderSistema() {
        System.out.println("Sistema de Litio encendido.");
    }

    public int nivelEnergia() {
        bateria -= 5;
        return bateria;
    }
}
