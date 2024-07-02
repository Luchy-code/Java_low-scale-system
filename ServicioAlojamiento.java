package Proyecto;

public class ServicioAlojamiento extends ServicioSimple {
	private String pais;
    private String ciudad; 
    private String nomHotel; 
    private int cantDias;
    private double costoTraslado;
    
    public ServicioAlojamiento(int codigo, double costoBase, int cantPersonas, String pais, String ciudad, String fechaIn,
            String fechaOut, String nomHotel, double costoTraslado) {
    	//int codigo, double costoBase, int cantPersonas, String fecha
        super(codigo,costoBase,cantPersonas,fechaIn,fechaOut,"Alojamiento");
        if(cantPersonas >= 5) {
            throw new RuntimeException("No es posible más de 5 personas");
        }
        if(pais == null || ciudad == null || fechaIn == null || fechaOut == null || nomHotel == null || costoTraslado <= 0) {
            throw new RuntimeException("Datos inválidos");
        }
        this.pais = pais;
        this.ciudad = ciudad;
        this.nomHotel = nomHotel;
        this.cantDias = diferenciasFechas(fechaIn,fechaOut);
        this.costoTraslado = costoTraslado;
    }
    
    @Override
	protected String DatosAPagar() {
		return "Tipo: "+super.obtenerNombreServicio()+" costo:"+super.obtenerCostoBase()+" cantPeronas:"+super.obtenerCantPersonas()+" cantDias: "+this.cantDias+" costoTrnas: "+costoTraslado+" tot="+this.totalApagar();
		}

    
    @Override
    public double totalApagar() {
    	double costoPersona=(super.obtenerCostoBase() * this.cantDias);
    	if(super.obtenerCantPersonas()<=2) {
    		return costoPersona + costoTraslado;
    	}
    	else if(super.obtenerCantPersonas()>2 && super.obtenerCantPersonas()<=4) {
    		return costoPersona*2 + costoTraslado;
    	}
        return costoPersona*2.5 + costoTraslado;
    }
    
    @Override
    public String toString() {
        return "Alojamiento [pais=" + pais + ", ciudad=" + ciudad + ", nomHotel=" + nomHotel + ", cantDias=" + cantDias + ", costoTraslado="
                + costoTraslado + ", datos=" + super.toString() + "]";
    }

	
    
    
    
}
