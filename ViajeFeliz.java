package Proyecto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ViajeFeliz implements IViajeFeliz {
	private static int codSS, codP;
    private String cuit;
    private Map<String, Cliente> clientes;
    private Map<String,ClienteContratacion> contrataciones;
    private Map<Integer, ServicioSimple> servicios;
    private Map<Integer, Paquete> paquetes;

    public ViajeFeliz(String cuit) {
        this.cuit = cuit;
        this.clientes = new HashMap<>();
        this.servicios = new HashMap<>();
        this.paquetes = new HashMap<>();
        this.contrataciones= new HashMap<>();
        this.codP=0;
        this.codSS=0;
    }

    @Override
    public void registrarCliente(String dni, String nombre, String direccion) {
    	if(this.clientes.containsKey(dni)) {
    		throw new RuntimeException("Ya existe ese cliente");
    	}
        Cliente cliente = new Cliente(dni, nombre, direccion);
        clientes.put(dni, cliente);
    }

    public int crearServicio(double costo, String fechaInicio, int cantPersonas, String destino, boolean esDiaCompleto) {
        //(double costoBase, int cantPersonas, String ciudad, String fechaIn, boolean diaCompleto)
    	ServicioSimple servicio = new ServicioExcursion(codSS++,costo, cantPersonas, destino, fechaInicio, esDiaCompleto);
        servicios.put(servicio.obtenerCodUnico(), servicio);
        return servicio.obtenerCodUnico();
    }

    public int crearServicio(double costo, String fechaInicio, int cantPersonas, String pais, String ciudad, double tasa, String fechaFin) {
        //(double costoBase, int cantPersonas, String pais, String ciudad, String fechaIn, String fechaOut, double tasaImpuesto)
    	ServicioSimple servicio = new ServicioAlquiler(codSS++,costo, cantPersonas, pais, ciudad, fechaInicio, fechaFin, tasa);
    	servicios.put(servicio.obtenerCodUnico(), servicio);
        return servicio.obtenerCodUnico();
    }

    public int crearServicio(double costo, String fechaInicio, int cantPersonas, String pais, String ciudad, String fechaFin, String hotel, double traslado) {
    	//(double costoBase, int cantPersonas, String pais, String ciudad, String fechaIn,String fechaOut, String nomHotel, double costoTraslado)
    	ServicioSimple servicio = new ServicioAlojamiento(codSS++,costo, cantPersonas, pais, ciudad, fechaInicio, fechaFin, hotel, traslado);
    	servicios.put(servicio.obtenerCodUnico(), servicio);
        return servicio.obtenerCodUnico();
    }

    public int crearServicio(double costo, String fechaInicio, int cantPersonas, String pais, String ciudad, String fechaFin, double garantia) {
    	//(double costoBase, int cantPersonas, String pais, String ciudad, String fechaIn,String fechaOut, double garantia)
    	ServicioSimple servicio = new ServicioVuelo(codSS++,costo, cantPersonas, pais, ciudad, fechaInicio, fechaFin, garantia);
    	servicios.put(servicio.obtenerCodUnico(), servicio);
        return servicio.obtenerCodUnico();
    }
    
    private List<ServicioSimple> serviciosSeleccionados( int[] codigosDeServicios){
    	List<ServicioSimple> seleccionados = new ArrayList<>();
        //crear un lista con los servicios
        //luego agregarla al paquete * cantPaquetes
        for(int x = 0; x < codigosDeServicios.length; x++) { 
        	ServicioSimple ss = this.servicios.get(codigosDeServicios[x]);
        	if (ss != null) { 
        		seleccionados.add(ss); 
        		servicios.remove(ss); 
        		}
        	}
        return seleccionados;
    }
    
    private int crearPaquete(List<ServicioSimple> ss) {
    		PaquetePredefinido paquete = new PaquetePredefinido(codP++);
            paquete.agregarServicios(ss);
            paquetes.put(paquete.obtenerCodigo(), paquete);
            return paquete.obtenerCodigo();
    }
   
    public int[] crearPaquetesPredefinidos(int cantPaquetes, int[] codigosDeServicios) {
    	int [] codigos= new int[cantPaquetes];
        List<ServicioSimple> serviciosSeleccionados = serviciosSeleccionados(codigosDeServicios);

        for (int i = 0; i < cantPaquetes; i++) {
            if(codigos.length<=cantPaquetes) {
            	int codigoPaquete = crearPaquete(serviciosSeleccionados);
            	codigos[i] = codigoPaquete;
            }
        }
        return codigos;
    }

    private ClienteContratacion devolverContratacionCliente(String dni) {
    	return this.contrataciones.get(dni);
    }
    
    public int iniciarContratacion(String dni, int codServicio) {
        Cliente cliente = clientes.get(dni);
        if (cliente == null) {
            throw new IllegalStateException("El cliente no existe");
        }

        // Verificar si el cliente tiene contrataciones activas
        ClienteContratacion contratacionExistente = devolverContratacionCliente(dni);
        if (contratacionExistente != null && contratacionExistente.estaActivo()) {
            throw new IllegalStateException("El cliente tiene contrataciones activas.");
        }

        // Si no hay contrataciones activas, proceder con la contratación
        int codContratacion = codP++;
        this.contrataciones.put(dni, new ClienteContratacion(dni, codContratacion));
        ClienteContratacion contratacion = devolverContratacionCliente(dni);

        ServicioSimple servicio = servicios.get(codServicio);
        Paquete paquete = paquetes.get(codServicio);

        if (servicio != null) {
            Paquete paq = new PaquetePredefinido(codP++);
            paq.establecerFechaInicio(servicio.obtenerFechaIn());
            paq.agregarServicio(servicio);
            contratacion.contratarPaquete(codContratacion, paq);
            servicios.remove(codServicio);
        } else if (paquete != null) {
            contratacion.contratarPaquete(codContratacion, paquete);
            paquetes.remove(codServicio);
        } else {
            throw new IllegalStateException("Paquete o servicio no existe");
        }
        cliente.agregar(codContratacion, contratacion);
        contratacion.activar();
        return codContratacion;
    }



    private void leerCatalog(String dni) {
    	Cliente cli=this.clientes.get(dni);
    	System.out.println(cli.devolverNombre());
    	ClienteContratacion cc=this.contrataciones.get(dni);
    	for(Paquete paq:cc.listaContratacionesCopia()) {
    		System.out.println(paq.obtenerDecuento());
    		for(ServicioSimple ss:paq.listaServiciosCopia()) {
    			System.out.println(ss.DatosAPagar());
    		}
    		System.out.println();
    	}
    	/*System.out.println("____________________");
    	for (Paquete p : paquetes.values()) {
            System.out.println("Cod: " + p.obtenerCodigo() + " " + p.toString());
        }
    	System.out.println("____________________");
        for (ServicioSimple servicio : servicios.values()) {
            System.out.println("Cod: " + servicio.obtenerCodUnico() + " " + servicio.toString());
        }*/
    }

    public void agregarServicioAContratacion(String dni, int codServicio) {
    	 ClienteContratacion contratacion=this.devolverContratacionCliente(dni);
         if (contratacion == null) {
             throw new IllegalArgumentException("Cliente no encontrado");
         }
        Paquete paquete = this.devolverContratacionCliente(dni).PaqueteVigente();
        if (paquete == null) {
            throw new IllegalArgumentException("No hay contrataciones");
        }
        
        ServicioSimple servicio = servicios.get(codServicio);
        if (servicio != null) {
        	paquete.agregarServicio(servicio);
            servicios.remove(codServicio);
        }else{
            throw new IllegalArgumentException("No existe ese servicio");
        }/**/
        
    }


    public void quitarServicioDeContratacion(String dni, int codServicio) {
    	Cliente cliente=this.clientes.get(dni);
    	if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        
        ClienteContratacion contratacion=this.devolverContratacionCliente(dni);
        int codContratacion=contratacion.devolverCodigo();
        
        Paquete paquete = contratacion.PaqueteVigente();
        
        if (paquete == null) {
            throw new IllegalArgumentException("No hay una contratación vigente para el cliente.");
        }
        ServicioSimple servicio =this.devolverContratacionCliente(dni).devolverServicioPaqueteVigente(codServicio);
        if (servicio == null) {
            throw new IllegalArgumentException("Servicio no encontrado en la contratación vigente del cliente.");
        }
        cliente.eliminar(codContratacion);
        contratacion.quitarServiciocontrataciones(servicio);
    }
    
    /*private double calCosto() {
		7200 double cVuelo = 2000 * 3 * 1.2; // Costo * Cant + % Tasa
		2475 double cAloj = 600 * 4 + 75; // Costo para 2 * cantDias + Traslado
		2600 double cAlq = 350 * 1 * 4 + 1200; // Costo * cant * cantDias + garantia
		475 double cExc1 = 250 * 2 * 0.95; // ValorMedioDia * Cant - Desc 5% (2 personas)
		1350 double cExc2 = 250 * 2 * 3 * 0.90; // MedioDia * 2 (completo) * Cant - Desc 10% (3 personas)
		14100 double sumaCosto = cVuelo + cAloj + cAlq + cExc1 + cExc2;
		13495 return sumaCosto * 0.95; // 5% Desc por 2 servicios (un paquetePredefinido y un servicio de excursion)
	}*/
    
    public double calcularCostoDePaquetePersonalizado(String dni, int codPaquetePersonalizado) {
    	Cliente cliente=this.clientes.get(dni);
    	if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    	ClienteContratacion contratacion=cliente.devolverContratacion(codPaquetePersonalizado);
        if (contratacion == null) {
            throw new IllegalArgumentException("Cliente no tiene contrataciones");
        }
        return contratacion.totalApagarContratacion();
    }

    public double pagarContratacion(String dni, String fechaPago) {
     	ClienteContratacion contratacion=this.devolverContratacionCliente(dni);
         if (contratacion == null) {
             throw new IllegalArgumentException("Cliente no encontrado, o, no tiene contrataciones");
         }
         contratacion.pagar();
        leerCatalog(dni);
     	return contratacion.pagar(fechaPago);
    }  

    public List<Integer> historialDeContrataciones(String dni) {
    	ClienteContratacion contratacion=this.devolverContratacionCliente(dni);
        if (contratacion == null) {
            throw new IllegalArgumentException("Cliente no encontrado, o, no tiene contrataciones");
        }
        return this.clientes.get(dni).devolverCodigos();
    }
    
    public String contratacionesSinIniciar(String fecha) {
    	/**
    	 * Devolver los paquetes que aún no se iniciaron dando nombre del cliente, 
    	 * fecha de inicio y los datos de los servicios contratados.
    	 * " {nombre_cliente} | {fecha_inicio} | [ {tipo_servicio_1}, {tipo_servicio_2}]
    	 * Ejemplo: Homero | 2024-02-14 | [Vuelo, Alojamiento, Excursion]
    	 * */
    	    String resultado="";
    	    for(Cliente clientela:this.clientes.values()) {
    	    	for(Integer cod:clientela.devolverCodigos()) {
    	    		ClienteContratacion contratacion=clientela.devolverContratacion(cod); 
    	    		List<Paquete> paquetesCliente= contratacion.listaContratacionesCopia();
    	    		for (Paquete paq : paquetesCliente) {
    	    			String fechaInicio=paq.devolverfechainicio();
    	    			List<String> tiposServicios = new ArrayList<>();
        	            for (ServicioSimple servicio : paq.listaServiciosCopia()) {
        	                tiposServicios.add(servicio.obtenerNombreServicio());
        	            }
    	    			if(this.paqueteNoIniciado(fechaInicio, fecha)) {
        	        	String dni=contratacion.devolverDNI();
        	        	String nombre=this.clientes.get(dni).devolverNombre();
        	        	resultado += String.format("%s | %s | [%s]\n", nombre, fechaInicio, String.join(", ",tiposServicios));
        	        	
        	            }
        	        }
    	    	}
    	    }
    	    return resultado;
    }
    
    private boolean paqueteNoIniciado(String fechaInicio,String fecha) {
    	//fechaInicio>fecha
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaLocalDate = LocalDate.parse(fecha, formatter);
        LocalDate fechaInicioLocalDate = LocalDate.parse(fechaInicio, formatter);
        return fechaInicioLocalDate.isAfter(fechaLocalDate);
    }

    public List<String> contratacionesQueInicianEnFecha(String fecha) {
    	/* Devuelve una lista con los datos de las contrataciones que se inician 
    	 * en la fecha pasada por parámetro. Indicando a que cliente pertenecen.
    	 * Formato de cada entrada: "{codigoUnicoContratacion} - ({dniCliente} {nombreCliente})"*/
    	    List<String> resultado = new LinkedList<>();
    	    for(Cliente clientela:this.clientes.values()) {
    	    	for(Integer cod:clientela.devolverCodigos()) {
    	    		ClienteContratacion contratacion=clientela.devolverContratacion(cod);
    	    		List<Paquete> paquetesCliente= contratacion.listaContratacionesCopia();
    	    		for (Paquete paq : paquetesCliente) {
    	    			if(paq.devolverfechainicio().equals(fecha)) {
        	        	String dni=contratacion.devolverDNI();
        	        	String nombre=this.clientes.get(dni).devolverNombre();
        	            String sb = String.format("%s - (%s %s)", cod, dni, nombre);
        	            resultado.add(sb);
        	            }
        	        }
    	    	}
    	    }
    	    return resultado;
    }
    
    public Set<Integer> obtenerCodigosCatalogo() {
        Set<Integer> codigos = new HashSet<>(servicios.keySet());
        codigos.addAll(paquetes.keySet());
        return codigos;
    }
}