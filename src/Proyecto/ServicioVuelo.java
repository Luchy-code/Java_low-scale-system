package proyecto;

public class ServicioVuelo extends ServicioSimple {
	 
    private String pais;
    private String ciudad;
    private double tasaImpuesto;
    
    public ServicioVuelo(int codigo, double costoBase, int cantPersonas, String pais, String ciudad, String fechaIn, String fechaOut, double tasaImpuesto) {
        super(codigo,costoBase,cantPersonas,fechaIn,fechaOut,"Vuelo");
        if(pais == null || ciudad == null || fechaIn == null || fechaOut == null || tasaImpuesto <= 0) {
            throw new RuntimeException("Datos inválidos");
        }
        this.pais = pais;
        this.ciudad = ciudad;
        this.tasaImpuesto = tasaImpuesto;
    }
    
    @Override
	protected String DatosAPagar() {
		return "Tipo: "+super.obtenerNombreServicio()+" costo:"+super.obtenerCostoBase()+" cantPersonas:"+super.obtenerCantPersonas()+" impuesto:"+" "+tasaImpuesto+" tot="+this.totalApagar();
	}
    
    @Override
    public double totalApagar() {
        return super.obtenerCostoBase()*super.obtenerCantPersonas()* (1 + tasaImpuesto);
    }
    
    @Override
    public String toString() {
        return "PasajeAvion [pais=" + pais + ", ciudad=" + ciudad 
                + ", tasaImpuesto=" + tasaImpuesto + ", datos=" + super.toString() + "]";
    }
    
    
   
}
