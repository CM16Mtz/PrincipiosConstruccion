package dao;

import entidades.Area;
import entidades.Hardware;
import entidades.Licencia;
import entidades.ReporteFalla;
import entidades.TecnicoAcademico;
import herencia.Objeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Clase que utiliza la operación Consultar para recuperar los reportes de falla en la base de
 * datos. A pesar de que la clase hereda de Objeto.java, por esta ocasión, no se utilzarán todos los
 * métodos que debe implementar ReporteFallaDAO.
 * @author Jatniel Martínez
 */
public class ReporteFallaDAO extends Objeto<ReporteFalla> {

  @Override
  public List<ReporteFalla> consultarElementos() {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa la consulta
    try {    //Se establece la consulta
      consulta = conexion.prepareStatement("SELECT * FROM Reporte_Falla rf INNER JOIN Hardware h ON"
          + " rf.Hardware_idHardware = h.idHardware INNER JOIN Tecnico_Academico ta ON "
          + "rf.Tecnico_Academico_idTecnico_Academico = ta.idTecnico_Academico");
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      List<ReporteFalla> lista = new ArrayList<>();    //Lista que guarda los reportes de falla
      while (resultado.next()) {    //Mientras exista una fila posterior
        //Se establecen los valores de los atributos de ReporteFalla
        Integer id = resultado.getInt("idReporte_Falla");
        String descripcion = resultado.getString("descripcion");
        Integer numReporte = resultado.getInt("numReporte");
        Integer idHardware = resultado.getInt("Hardware_idHardware");
        Integer idTecnicoAcademico = resultado.getInt("Tecnico_Academico_idTecnico_Academico");
        Hardware hardware = new Hardware();
        hardware.setId(resultado.getInt("idHardware"));
        hardware.setDescripcion(resultado.getString("descripcion"));
        hardware.setEstado(resultado.getString("estado"));
        hardware.setModelo(resultado.getString("modelo"));
        hardware.setNumeroInventario(resultado.getInt("numeroInventario"));
        hardware.setSerie(resultado.getInt("serie"));
        hardware.setTipo(resultado.getString("tipo"));
        Licencia licencia = new Licencia();
        licencia.setId(resultado.getInt("Licencia_idLicencia"));
        hardware.setLicencia(licencia);
        Area area = new Area();
        area.setId(resultado.getInt("Area_idArea"));
        hardware.setArea(area);
        TecnicoAcademico tecnico = new TecnicoAcademico();
        tecnico.setId(resultado.getInt("idTecnico_Academico"));
        tecnico.setCorreoElectronico(resultado.getString("correoElectronico"));
        tecnico.setEntidadAcademica(resultado.getString("entidadAcademica"));
        tecnico.setNombre(resultado.getString("nombre"));
        tecnico.setNoPersonal(resultado.getString("noPersonal"));
        tecnico.setTelefono("telefono");
        //Se añade a la lista la fila encontrada por resultado
        lista.add(new ReporteFalla(id, descripcion, numReporte, hardware, tecnico));
      }
      return lista;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible recuperar la información",
          "Error de consulta", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al recuperar la informacion",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }

  @Override
  public int insertarElemento(ReporteFalla elemento) {
    throw new UnsupportedOperationException("Método no utilizado.");
  }

  @Override
  public void eliminarElemento(ReporteFalla elemento) {
    throw new UnsupportedOperationException("Método no utilizado.");
  }

  @Override
  public void actualizarElemento(ReporteFalla elemento) {
    throw new UnsupportedOperationException("NMétodo no utilizado.");
  }
  
}
