package dao;

import entidades.Licencia;
import entidades.Software;
import herencia.Objeto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Clase que utiliza las operaciones CRUD para manejar los softwares de la base de datos.
 * @author Jatniel Martínez
 */
public class SoftwareDAO extends Objeto<Software> {

  @Override
  public List<Software> consultarElementos() {
    Connection conexion = this.conectar();    //Inicializa la conexion con la base de datos
    PreparedStatement consulta = null;    //Inicializa la consulta
    try {
      consulta = conexion.prepareStatement(    //Se establece la consulta
          "SELECT * FROM Software s INNER JOIN Licencia l ON s.Licencia_idLicencia = l.idLicencia");
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      List<Software> lista = new ArrayList<>();    //Lista que guarda las filas de la tabla Software
      while (resultado.next()) {    //Mientras exista una fila posterior...
        //Se establecen los valores de los atributos de Software
        Integer id = resultado.getInt("idSoftware");
        String nombre = resultado.getString("nombre");
        String observaciones = resultado.getString("observaciones");
        Integer version = resultado.getInt("version");
        Integer idLicencia = resultado.getInt("Licencia_idLicencia");
        Licencia licencia = new Licencia();
        licencia.setId(resultado.getInt("idLicencia"));
        licencia.setClave(resultado.getInt("clave"));
        licencia.setFechaFin(resultado.getDate("fechaFin"));
        licencia.setFechaInicio(resultado.getDate("fechaInicio"));
        licencia.setProveedor(resultado.getString("proveedor"));
        //Se añade a la lista la fila encontrada por resultado
        lista.add(new Software(id, nombre, observaciones, version, licencia));
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
  public int insertarElemento(Software elemento) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement(
          "INSERT INTO Software VALUES(default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      consulta.setString(1, elemento.getNombre());
      consulta.setString(2, elemento.getObservaciones());
      consulta.setInt(3, elemento.getVersion());
      consulta.setInt(4, elemento.getLicencia().getId());
      consulta.executeUpdate();    //Se ejecuta la consulta
      ResultSet resultado = consulta.getGeneratedKeys();    //Recupera las llaves generadas
      resultado.next();
      int id = resultado.getInt(1);    //Número que indica si el registro fue exitoso o no
      return id;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible guardar el software",
          "Error de registro", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al guardar el registro",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return -1;
  }

  @Override
  public void eliminarElemento(Software elemento) {
    //Se evalúa si el elemento existe
    if (elemento.getNombre().isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se puede eliminar un software que no está registrado",
          "Software no existe", JOptionPane.WARNING_MESSAGE);
    }
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("DELETE FROM Software WHERE nombre = ?");
      consulta.setString(1, elemento.getNombre());
      consulta.executeUpdate();    //Se ejecuta la consulta
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible eliminar el software",
          "Error de eliminación", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al eliminar el software",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @Override
  public void actualizarElemento(Software elemento) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("UPDATE Software SET nombre = ?, observaciones = ?, "
          + "version = ?, Licencia_idLicencia = ? WHERE idSoftware = ?");
      consulta.setString(1, elemento.getNombre());
      consulta.setString(2, elemento.getObservaciones());
      consulta.setInt(3, elemento.getVersion());
      consulta.setInt(4, elemento.getLicencia().getId());
      consulta.setInt(5, elemento.getId());
      consulta.executeUpdate();    //Se ejecuta la consulta
      JOptionPane.showMessageDialog(null, "Software actualizado con éxito",
          "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible modificar el software",
          "Error al actualizar", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();    //Se cierra la conexión
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al modificar el software",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  //@Override
  public Software buscarElemento(Object parametro) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("SELECT * FROM Software s INNER JOIN Licencia l ON "
          + "s.Licencia_idLicencia = l.idLicencia WHERE s.nombre = ?");
      String nombre = parametro.toString();
      consulta.setString(1, nombre);
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      resultado.next();
      //Se establecen los valores de los atributos de software
      Integer id = resultado.getInt("idSoftware");
      String observaciones = resultado.getString("observaciones");
      Integer version = resultado.getInt("version");
      Licencia licencia = new Licencia();
      licencia.setId(resultado.getInt("Licencia_idLicencia"));
      //Se devuelve el software con los valores establecidos
      return new Software(id, nombre, observaciones, version, licencia);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible buscar el software",
          "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al buscar el software",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }
  
}
