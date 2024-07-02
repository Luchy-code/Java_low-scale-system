
package Proyecto;

import java.util.*;

public abstract class Paquete{
	private int codigo;
	private String fechaIn;
    private LinkedList<ServicioSimple> servicios;
    private int cantPaquetes;
    private double descuento;

    public Paquete(int cod) {
    	this.codigo=cod;
        this.servicios = new LinkedList<>();
    }
    
    public void establecerFechaInicio(String fecha) {
    	this.fechaIn=fecha;
    }
    
    public double obtenerDecuento() {
    	return this.descuento;
    }

	public void establecerDescuento(int cant) {
        //int cantidadServicios = servicios.size();
		if (cant >= 3) {
            descuento = 0.10;
        } else if (cant == 2) {
            descuento = 0.05;
        } else {
            descuento = 0.0;
        }
    }
    
    public String leerServicios() {
    	StringBuilder sb=new StringBuilder();
    	sb.append("{");
    	for(ServicioSimple ss:servicios) {
    		sb.append(ss.toString()).append("\n");
    	}
    	sb.append("}");
    	return sb.toString();
    }
    
    public boolean existeServicio(ServicioSimple servicio) {
    	return this.servicios.contains(servicio);
    }
    
    public void agregarServicio(ServicioSimple servicio) {
    	if(existeServicio(servicio)) {
    		throw new RuntimeException("Ya existe el paquete");
    	}
    	this.servicios.add(servicio);
    	this.cantPaquetes=servicios.size();
    }
    
    public void agregarServicios(List<ServicioSimple> ss) {
    	this.establecerFechaInicio(ss.get(0).obtenerFechaIn());
    	
    	for(int i=0;i<ss.size();i++) {
    		ServicioSimple servicio=ss.get(i);
    		this.agregarServicio(servicio);
    		}
    }

    public int obtenerCodigo() {
        return codigo;
    }
    
    public int obtenerLargo() {
        return cantPaquetes;
    }
    
    public String devolverfechainicio() {
    	return this.fechaIn;
    }
    
    public ServicioSimple devolverServicio(int codigoS) {
        for(ServicioSimple ss:this.servicios) {
        	if(ss.obtenerCodUnico()==codigoS) {
        		return ss;
        	}
        }
        return null;
    }
    
    public List<ServicioSimple> listaServiciosCopia() {
    	ArrayList<ServicioSimple> lista= new ArrayList<>();
    	for(ServicioSimple servicio: this.servicios) {
    		lista.add(servicio);
    	}
        return lista;
    }
   

    public double totalApagar() {
        double total = 0;
    	List<ServicioSimple> lista= listaServiciosCopia();
        for (ServicioSimple servicio : lista) {
            total += servicio.totalApagar();
        }
        return total - (total * (this.descuento));
    }
    
    public void eliminarServicio(ServicioSimple servicio) {
        if (existeServicio(servicio)) {
            servicios.remove(servicio);
            this.cantPaquetes = servicios.size();
        }
    }

	@Override
	public String toString() {
		return "Paquete [codigo=" + codigo + ", fechaIn=" + fechaIn + ", servicios=" + this.leerServicios() + ", cantPaquetes="
				+ cantPaquetes + ", descuento=" + descuento + "]";
	}

  
    
    
}