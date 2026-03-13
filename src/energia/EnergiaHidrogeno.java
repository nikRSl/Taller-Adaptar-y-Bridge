package energia;

public class EnergiaHidrogeno implements SistemaEnergia {

    private int tanque = 100;

    public void encenderSistema() {
        System.out.println("Sistema de Hidrógeno activado.");
    }

    public int nivelEnergia() {
        tanque -= 8;
        return tanque;
    }
}
