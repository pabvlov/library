package cl.inacap.clasedatabase.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.Cliente;
import Model.Direccion;
import Model.Trabajador;
import Servicios.DireccionesServiceLocal;
import Servicios.TrabajadoresServiceLocal;

/**
 * Servlet implementation class TrabajadorDireccion
 */
@WebServlet("/TrabajadorDireccion.do")
public class TrabajadorDireccion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrabajadorDireccion() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Inject
    private TrabajadoresServiceLocal trabajadoresService;
    @Inject
    private DireccionesServiceLocal direccionService;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		trabajadores = trabajadoresService.getAll();
		List<Direccion> direcciones = new ArrayList<Direccion>();
		direcciones = direccionService.getAll();
		List<Direccion> direccionesCliente = new ArrayList<Direccion>();
		
		int rut = Integer.parseInt(request.getParameter("rut"));
		String direccion = request.getParameter("direccion");
		
		Direccion addDireccion = new Direccion();
		
		boolean existeDireccion = false;
		for(Direccion d : direcciones) {
			if(d.getDireccion().equals(direccion)) {
				existeDireccion = true;
				addDireccion = d;
				break;
			}
		}
		if(!direccion.equals("")) {
			if(!existeDireccion) {
				addDireccion.setDireccion(direccion);
				direccionService.add(addDireccion);
			}
			
			
			Trabajador trabajadorFinal = new Trabajador();
			
			for(Trabajador trabajador : trabajadores) {
				boolean existeEnUsuario = false;
				if (trabajador.getRut() == rut) {
					trabajadorFinal = trabajador;
					direccionesCliente = trabajador.getDirecciones();
					for(int i = 0; i < direccionesCliente.size(); i++) {
						if(direccionesCliente.get(i).getDireccion().equals(addDireccion.getDireccion())) {
							existeEnUsuario = true;
							break;
						}
					}
					if(!existeEnUsuario) {
						
						direccionesCliente.add(addDireccion);
						trabajadorFinal.setDirecciones(direccionesCliente);
						trabajadoresService.update(trabajadorFinal);
					}
				}
			}
		}
		
		
		
		
		
		response.sendRedirect("DetalleTrabajadores.do");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
