package proyecto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ClienteContratacion{
	
	//un paquete por contratcion, se armara de sistemas
	//en caso de aquirir un paquete, pues ese sera la contratacion
	//si se contrata servicios se arma un paquete
	private String dni;
	private int codigo;
	private LinkedList<Paquete> contrataciones;
	private int contPaquete,contServicio;
	//contador para saber cuantas se agrega y aplicar descuento
	private String fechaPago;
	private boolean pagado,contratacionesActivas;
	
	public ClienteContratacion(String dni, int cod) {
		this.codigo=cod;
		this.dni = dni;
		this.contrataciones=new LinkedList<>();
		this.fechaPago=null;
		this.contratacionesActivas=false;
		this.pagado=false;
		this.contPaquete=0;
		this.contServicio=1;
	}
 

    public int obtenerCantidadTotalDeContrataciones() {
    	return this.contPaquete+this.contServicio;
    }
    
	public void contratarPaquete(int cod,Paquete paq) {
        if (paq == null) {
            throw new IllegalArgumentException("Paquete no puede ser null");
        }
        if(this.estaActivo()) {
        	throw new IllegalStateException("Tiene contrataciones activas");
        }
        if (existePaquete(paq)) {
            throw new IllegalStateException("Ya existe ese paquete");
        }
        this.activar();
        contrataciones.add(paq);
        this.contPaquete++;
    }
	


    public void agregarServiciocontrataciones(ServicioSimple servicio) {
        if (listaVacia()) {
            throw new RuntimeException("No hay paquetes en contratación");
        }
        if(!this.estaActivo()) {
        	throw new IllegalStateException("NO tiene contrataciones activas");
        }
        if (existeServicio(servicio)) {
            throw new IllegalStateException("Ya existe ese servicio");
        }
        Paquete contratacion = PaqueteVigente();
        contratacion.agregarServicio(servicio);
        this.contServicio++;
    }
  

	public boolean estaPagado() {
		return pagado;
	}


	public boolean estaActivo() {
		return contratacionesActivas;
	}


	public void pagar() {
		this.contratacionesActivas=false;
		this.pagado = true;
	}


	public void activar() {
		this.contratacionesActivas = true;
	}


	private boolean existePaquete(Paquete paq) {
        return this.contrataciones.contains(paq);
    }
    
    private boolean existeServicio(ServicioSimple servicio) {
    	return PaqueteVigente().existeServicio(servicio);
    } 

    public int devolverCodigo() {
    	return this.codigo;
    }


    public void quitarServiciocontrataciones(ServicioSimple ss) {
    	if (listaVacia()) {
            throw new RuntimeException("No hay paquetes en contratación");
        }
    	Paquete contratacion = PaqueteVigente();
        contratacion.eliminarServicio(ss);
        this.contServicio--;
    }

    public Paquete PaqueteVigente() {
        if (listaVacia()) {
            return null;
        }
        return contrataciones.getLast();
    }
    
  

    public ServicioSimple devolverServicioPaqueteVigente(int codServicio) {
        Paquete contratacion = PaqueteVigente();
        if (contratacion == null) {
            return null;
        }
        ServicioSimple ss=contratacion.devolverServicio(codServicio);
        if (ss == null) {
            return null;
        }
        return ss;
    }

	public Paquete obtenerPaqueteNoPagado() {
        return (!pagado || fechaPago==null) ? PaqueteVigente() : null;
    }

    public Paquete obtenerPaqueteNoIniciado() {
        return (this.contratacionesActivas) ? PaqueteVigente() : null;
    }

    private boolean listaVacia() {
        return this.contrataciones.isEmpty();
    }
    
    public double totalApagarContratacion() {
    	Paquete paquete = this.PaqueteVigente();
    	paquete.establecerDescuento(this.obtenerCantidadTotalDeContrataciones());
    	return paquete.totalApagar();
    }

    public double pagar(String fechaPago) {
        if (listaVacia()) {
            throw new RuntimeException("No hay paquetes contratados.");
        }
        Paquete paquete = this.PaqueteVigente();
        String fechaInicio = paquete.devolverfechainicio();
        if (fechaInicio == null) {
            throw new RuntimeException("No se pudo determinar la fecha de inicio del paquete.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaPagoLocalDate = LocalDate.parse(fechaPago, formatter);
        LocalDate fechaInicioLocalDate = LocalDate.parse(fechaInicio, formatter);

        if (fechaPagoLocalDate.isAfter(fechaInicioLocalDate)) {
            throw new RuntimeException("La fecha de pago debe ser anterior a la fecha de inicio del paquete.");
        }
        
        this.pagar();
        this.fechaPago=fechaPago;
        //this.contPaquetes=0;
        return totalApagarContratacion();
    }


    public boolean devolverEstadoPago() {
        return this.pagado;
    }
    
    public List<Paquete> listaContratacionesCopia() {
    	ArrayList lista= new ArrayList<>();
    	for(Paquete paq: this.contrataciones) {
    		lista.add(paq);
    	}
        return lista;
    }

	public String devolverDNI() {
		return this.dni;
	}
	
	public String leerCC() {
		StringBuilder sb=new StringBuilder();
		for(Paquete cc:this.contrataciones) {
			sb.append(cc.devolverfechainicio()).append("////");
			for(ServicioSimple ss:cc.listaServiciosCopia()) {
				sb.append(ss.obtenerNombreServicio()+" ").append(ss.obtenerFechaIn()).append(" : ").append(ss.obtenerFechaOut()).append(";;");
			}
		}
		return sb.toString();
	}


	@Override
	public String toString() {
		return "CC [dni=" + dni + ", codigo=" + codigo + ", contrataciones=" + leerCC()+ "]";
	}

	

}


