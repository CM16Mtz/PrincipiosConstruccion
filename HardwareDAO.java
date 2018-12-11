package dao;

import entidades.Area;
import entidades.Hardware;
import entidades.Licencia;
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
 * Clase que utiliza las operaciones CRUD para manejar los hardwares de la base de datos.
 * @author Jatniel Martínez
 */
public class HardwareDAO extends Objeto<Hardware> {

  @Override
  public List<Hardware> consultarElementos() {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa la consulta
    try {
      consulta = conexion.prepareStatement(    //Se establece la consulta
          "SELECT * FROM Hardware h INNER JOIN Licencia l ON h.Licencia_idLicencia = l.idLicencia "
              + "INNER JOIN Area a ON h.Area_idArea = a.idArea");
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      List<Hardware> lista = new ArrayList<>();    //Lista que guarda las filas de la tabla Hardware
      while (resultado.next()) {    //Mientras exista una fila posterior...
        //Se establecen los valores de los atributos de Hardware
        Integer id = resultado.getInt("idHardware");
        String descripcion = resultado.getString("descripcion");
        String estado = resultado.getString("estado");
        String modelo = resultado.getString("modelo");
        Integer numeroInventario = resultado.getInt("numeroInventario");
        Integer serie = resultado.getInt("serie");
        String tipo = resultado.getString("tipo");
        Integer idLicencia = resultado.getInt("Licencia_idLicencia");
        Integer idArea = resultado.getInt("Area_idArea");
        Licencia licencia = new Licencia();
        licencia.setId(resultado.getInt("idLicencia"));
        licencia.setClave(resultado.getInt("clave"));
        licencia.setFechaFin(resultado.getDate("fechaFin"));
        licencia.setFechaInicio(resultado.getDate("fechaInicio"));
        licencia.setProveedor(resultado.getString("proveedor"));
        Area area = new Area();
        area.setId(resultado.getInt("idArea"));
        area.setDescripcion(resultado.getString("descripcion"));
        area.setNumero(resultado.getString("numero"));
        area.setUbicacion(resultado.getString("ubicacion"));
        //Se añade a la lista la fila encontrada por resultado
        lista.add(new Hardware(
            id, descripcion, estado, modelo, numeroInventario, serie, tipo, licencia, area));
      }
      return lista;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible recuperar la información",
          "Error de consulta", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();    //Se cierra la conexión
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al recuperar la información",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }

  @Override
  public int insertarElemento(Hardware elemento) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("INSERT INTO Hardware VALUES("
          + "default, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      consulta.setString(1, elemento.getDescripcion());
      consulta.setString(2, elemento.getEstado());
      consulta.setString(3, elemento.getModelo());
      consulta.setInt(4, elemento.getNumeroInventario());
      consulta.setInt(5, elemento.getSerie());
      consulta.setString(6, elemento.getTipo());
      consulta.setInt(7, elemento.getLicencia().getId());
      consulta.setInt(8, elemento.getArea().getId());
      consulta.executeUpdate();    //Se ejecuta la consulta
      ResultSet resultado = consulta.getGeneratedKeys();    //Recupera las llaves generadas
      resultado.next();
      int id = resultado.getInt(1);    //Número que indica si el registro fue exitoso o no
      return id;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible guardar el hardware",
          "Error de registro", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();    //Se cierra la conexión
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al guardar el registro",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return -1;
  }

  @Override
  public void eliminarElemento(Hardware elemento) {
    //Se evalúa si el elemento existe
    if (elemento.getSerie() == 0) {
      JOptionPane.showMessageDialog(null, "No se puede eliminar un hardware que no está registrado",
          "Hardware no existe", JOptionPane.WARNING_MESSAGE);
    }
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("DELETE FROM Hardware WHERE serie = ?");
      consulta.setInt(1, elemento.getSerie());
      consulta.executeUpdate();    //Se ejecuta la consulta
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible eliminar el hardware",
          "Error de eliminación", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al eliminar el hardware",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @Override
  public void actualizarElemento(Hardware elemento) {
    //Se evalúa si el elemento existe
    if (elemento.getId().equals(-1)) {
      JOptionPane.showMessageDialog(null,
          "No se puede modificar un hardware que no está registrado", "Hardware no existe",
          JOptionPane.WARNING_MESSAGE);
    }
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("UPDATE Hardware SET descripcion = ?, estado = ?, "
          + "modelo = ?, numeroInventario = ?, serie = ?, tipo = ?, "
          + "Licencia_idLicencia = ? , Area_idArea = ? WHERE idHardware = ?");
      consulta.setString(1, elemento.getDescripcion());
      consulta.setString(2, elemento.getEstado());
      consulta.setString(3, elemento.getModelo());
      consulta.setInt(4, elemento.getNumeroInventario());
      consulta.setInt(5, elemento.getSerie());
      consulta.setString(6, elemento.getTipo());
      consulta.setInt(7, elemento.getLicencia().getId());
      consulta.setInt(8, elemento.getArea().getId());
      consulta.executeUpdate();    //Se ejecuta la consulta
      JOptionPane.showMessageDialog(null, "Hardware actualizado con éxito",
          "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible modificar el hardware",
          "Error al actualizar", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();    //Se cierra la conexión
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al modificar el hardware",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  //@Override
  public Hardware buscarElemento(Object parametro) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("SELECT * FROM Hardware h INNER JOIN Licencia l ON "
          + "h.Licencia_idLicencia = l.idLicencia INNER JOIN Area a ON h.Area_idArea = a.idArea "
          + "WHERE h.serie = ?");
      Integer serie = Integer.parseInt(parametro.toString());
      consulta.setInt(1, serie);
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      resultado.next();
      //Se establecen los valores de los atributos de hardware
      Integer id = resultado.getInt("idHardware");
      String descripcion = resultado.getString("descripcion");
      String estado = resultado.getString("estado");
      String modelo = resultado.getString("modelo");
      Integer numeroInventario = resultado.getInt("numeroInventario");
      String tipo = resultado.getString("tipo");
      Licencia licencia = new Licencia();
      licencia.setId(resultado.getInt("Licencia_idLicencia"));
      Area area = new Area();
      area.setId(resultado.getInt("Area_idArea"));
      //Se devuelve el software con los valores establecidos
      return new Hardware(
          id, descripcion, estado, modelo, numeroInventario, serie, tipo, licencia, area);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible buscar el hardware",
          "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al buscar el hardware",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }
  
}
