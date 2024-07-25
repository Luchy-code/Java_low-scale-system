package proyecto;

public class ServicioAlquiler extends ServicioSimple {
	 private String pais;
	    private String ciudad;
	    private int cantDias;
	    private double garantia;
	    
	    public ServicioAlquiler(int codigo, double costoBase, int cantPersonas, String pais, String ciudad, 
	    		String fechaIn,String fechaOut, double garantia) {
	        super(codigo,costoBase, cantPersonas,fechaIn,fechaOut,"Alquiler");
	        if(cantPersonas >= 10) {
	            throw new RuntimeException("No es posible más de 10 personas");
	        }
	        if(pais == null || ciudad == null || garantia <= 0) {
	            throw new RuntimeException("Datos inválidos");
	        }
	        this.pais = pais;
	        this.ciudad = ciudad;
	        this.cantDias = diferenciasFechas(fechaIn,fechaOut);
	        this.garantia = garantia;
	    }
	    @Override
		protected String DatosAPagar() {
			return "Tipo: "+super.obtenerNombreServicio()+" costo:"+super.obtenerCostoBase()+" cantPeronas:"+super.obtenerCantPersonas()+" cantDias: "+this.cantDias+" costoTrnas: "+" "+garantia+" ="+this.totalApagar();
		}
		@Override
	    public double totalApagar() {
			double costoPersona=(super.obtenerCostoBase()* super.obtenerCantPersonas()* cantDias);
			if(super.obtenerCantPersonas()<=4) {
	    		return costoPersona + garantia;
	    	}
	    	else if(super.obtenerCantPersonas()>4 && super.obtenerCantPersonas()<=8) {
	    		return costoPersona*2 + garantia;
	    	}
	        return costoPersona*3 + garantia;
	    }
	    
	    @Override
	    public String toString() {
	        return "Transporte [pais=" + pais + ", ciudad=" + ciudad 
	                + ", cantDias=" + cantDias + ", garantia=" + garantia + ", datos=" + super.toString() + "]";
	    }

	   
}
