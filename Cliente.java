package Proyecto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Cliente {
    private String dni;
    private String nombre;
    private String direccion;
    private Map<Integer,ClienteContratacion> paquetes;

    public Cliente(String dni, String nombre, String direccion) {
        if (dni == null || nombre == null || direccion == null) {
            throw new RuntimeException("Datos inválidos");
        }
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.paquetes= new HashMap<>();
    }

    
    @Override
    public String toString() {
        return "Cliente [dni=" + dni + ", nombre=" + nombre + ", direccion=" + direccion + "]";
    }

    public String devolverNombre() {
        return this.nombre;
    }

    public String devolverDNI() {
        return this.dni;
    }
    
    public int largoContratacion() {
    	return this.devolverCodigos().size();
    }
    
    public ClienteContratacion devolverContratacion(int cod){
    	return this.paquetes.get(cod);
    }
    
    public void agregar(int cod, ClienteContratacion cc) {
    	if(this.paquetes.containsKey(cod)) {
    		throw new RuntimeException("Ya existe esa contratacion");
    	}
    	this.paquetes.put(cod,cc);
    }
    
    public void eliminar(int cod) {
    	this.paquetes.remove(cod);
    }

	public List<Integer> devolverCodigos() {
	        List<Integer> codigos = new ArrayList<>();
	        for (Entry<Integer, ClienteContratacion> entry : paquetes.entrySet()) {
	            Integer codigo = entry.getKey(); 
	            codigos.add(codigo);
	        }
	        return codigos;
	}

    
}
