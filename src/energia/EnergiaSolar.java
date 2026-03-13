package energia;

public class EnergiaSolar implements SistemaEnergia {

    private int carga = 60;

    public void encenderSistema() {
        System.out.println("Paneles solares desplegados.");
    }

    public int nivelEnergia() {
        carga += 2;
        return carga;
    }
}
