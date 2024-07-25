package proyecto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class ServicioSimple {
    private int codUnico;
    private double costoBase; 
    private int cantPersonas;
    private String fechaIn;
    private String fechaOut;
    private String nombre;
    
    public ServicioSimple(int codigo, double costoBase, int cantPersonas, String fecha1, String fecha2, String nom) {
    	 if(costoBase <= 0 || cantPersonas <= 0 || cantPersonas > 5 || codigo<0 || fecha1 == null || fecha2 == null || nom == null) {
             throw new RuntimeException("Datos inválidos");
         }
         this.fechaIn=fecha1;
         this.fechaOut=fecha2;
         this.codUnico = codigo;
         this.costoBase = costoBase;
         this.cantPersonas = cantPersonas;
         this.nombre=nom;
	}

	protected abstract String DatosAPagar();
	public abstract double totalApagar();

    @Override
    public String toString() {
        return "ServicioSimple [codUnico=" + codUnico + ", tipo=" + nombre + ", costoBase=" + costoBase + ", cantPersonas=" + cantPersonas + ", fechaIn=" + fechaIn +", fechaOut=" + fechaOut +"]";
    }

    public int obtenerCodUnico() {
        return codUnico;
    }

    public double obtenerCostoBase() {
        return costoBase;
    }

    public int obtenerCantPersonas() {
        return cantPersonas;
    }
    
    public String obtenerFechaIn() {
    	return fechaIn;
    }
    
    public String obtenerFechaOut() {
    	return fechaOut;
    }
    public String obtenerNombreServicio() {
    	return nombre;
    }
    
    protected int diferenciasFechas(String f1, String f2) {
		LocalDate date1 = LocalDate.parse(f1);
        LocalDate date2 = LocalDate.parse(f2);
        long diff = ChronoUnit.DAYS.between(date1, date2);
        if(diff<=0)
        	throw new RuntimeException("revisar fechas");
        return (int)diff;
	}

    
    
    
}
