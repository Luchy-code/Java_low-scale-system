package proyecto;

public class ServicioExcursion extends ServicioSimple {
	private String ciudad;
    private int descuento;
    private boolean diaCompleto;

    public ServicioExcursion(int codigo, double costoBase, int cantPersonas, String ciudad, String fechaIn, boolean diaCompleto) {
        super(codigo,costoBase,cantPersonas,fechaIn," ","Excursion");
        if(ciudad == null || fechaIn == null || cantPersonas < 1 || cantPersonas > 4) {
            throw new RuntimeException("Datos inválidos");
        }
        this.ciudad = ciudad;
        this.diaCompleto = diaCompleto;
        
        if(cantPersonas == 2) {
            this.descuento = 5;
        } else if(cantPersonas > 2 && cantPersonas <= 4) {
            this.descuento = 10;
        } else {
            this.descuento = 0;
        }
    }
    
    @Override
	protected String DatosAPagar() {
		return "Tipo: "+super.obtenerNombreServicio()+" costo:"+super.obtenerCostoBase()+" cantPersonas:"+super.obtenerCantPersonas()+" dia completo?"+this.diaCompleto+" descuento:"+descuento+" tot="+this.totalApagar();
	}

    @Override
    public double totalApagar() {
        double total = super.obtenerCostoBase() * super.obtenerCantPersonas();
        total -= total * (descuento / 100.0);
        if(diaCompleto) {
            return total*2;
        }
        return total;
    }

    @Override
    public String toString() {
        return "Excursion [ciudad=" + ciudad + ", descuento=" + descuento + ", diaCompleto=" + 
        		diaCompleto + ", datos=" + super.toString()+ "]";
    }
    
}
