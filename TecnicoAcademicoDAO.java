package dao;

import entidades.TecnicoAcademico;
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
 * Clase que utiliza las operaciones CRUD para manejar los técnicos académicos de la base de datos.
 * @author Jatniel Martínez
 */
public class TecnicoAcademicoDAO extends Objeto<TecnicoAcademico> {

  @Override
  public List<TecnicoAcademico> consultarElementos() {
    Connection conexion = this.conectar();    //Inicializa la conexion con la base de datos
    PreparedStatement consulta = null;    //Inicializa la consulta
    try {
      consulta = conexion.prepareStatement(    //Se establece la consulta
          "SELECT* FROM Tecnico_Academico ta");
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      List<TecnicoAcademico> lista = new ArrayList<>();    //Lista que guarda los tecnicos
      while (resultado.next()) {    //Mientras exista una fila posterior...
        //Se establecen los valores de los atributos de Tecnico_Academico
        Integer id = resultado.getInt("idTecnico_Academico");
        String correoElectronico = resultado.getString("correoElectronico");
        String entidadAcademica = resultado.getString("entidadAcademica");
        String nombre = resultado.getString("nombre");
        String noPersonal = resultado.getString("noPersonal");
        String telefono = resultado.getString("telefono");
        //Se añade a la lista la fila encontrada por resultado
        lista.add(new TecnicoAcademico(
            id, correoElectronico, entidadAcademica, nombre, noPersonal, telefono));
      }
      return lista;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible recuperar la información",
          "Error de consulta", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al recuperar la información",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }

  @Override
  public int insertarElemento(TecnicoAcademico elemento) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("INSERT INTO Tecnico_Academico VALUES("
          + "default, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      consulta.setString(1, elemento.getCorreoElectronico());
      consulta.setString(2, elemento.getEntidadAcademica());
      consulta.setString(3, elemento.getNombre());
      consulta.setString(4, elemento.getNoPersonal());
      consulta.setString(5, elemento.getTelefono());
      consulta.executeUpdate();    //Se ejecuta la consulta
      ResultSet resultado = consulta.getGeneratedKeys();    //Recupera las llaves generadas
      resultado.next();
      int id = resultado.getInt(1);    //Número que indica si el registro fue exitoso o no
      return id;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible guardar el técnico académico",
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
  public void eliminarElemento(TecnicoAcademico elemento) {
    //Se evalúa si el elemento existe
    if (elemento.getNoPersonal().isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se puede eliminar un técnico que no está registrado",
          "Técnico académico no existe", JOptionPane.WARNING_MESSAGE);
    }
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement(
          "DELETE FROM Tecnico_Academico WHERE noPersonal = ?");
      consulta.setString(1, elemento.getNoPersonal());
      consulta.executeUpdate();    //Se ejecuta la consulta
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible eliminar el técnico académico",
          "Error de eliminación", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al eliminar el técnico académico",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @Override
  public void actualizarElemento(TecnicoAcademico elemento) {
    //Se evalúa si el elemento existe
    if (elemento.getId().equals(-1)) {
      JOptionPane.showMessageDialog(null,
          "No se puede modificar un técnico académico que no está registrado",
          "Técnico académico no existe", JOptionPane.WARNING_MESSAGE);
    }
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement("UPDATE Tecnico_Academico SET correoElectronico = ?, "
          + "entidadAcademica = ?, nombre = ?, noPersonal = ?, telefono = ? "
          + "WHERE idTecnico_Academico = ?");
      consulta.setString(1, elemento.getCorreoElectronico());
      consulta.setString(2, elemento.getEntidadAcademica());
      consulta.setString(3, elemento.getNombre());
      consulta.setString(4, elemento.getNoPersonal());
      consulta.setString(5, elemento.getTelefono());
      consulta.executeUpdate();    //Se ejecuta la consulta
      JOptionPane.showMessageDialog(null, "Técnico académico actualizado con éxito",
          "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible modificar el técnico académico",
          "Error al actualizar", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();    //Se cierra la conexión
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al modificar el técnico académico",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  //@Override
  public TecnicoAcademico buscarElemento(Object parametro) {
    Connection conexion = this.conectar();    //Inicializa la conexión con la base de datos
    PreparedStatement consulta = null;    //Inicializa  la consulta
    try {
      //Se establece la consulta
      consulta = conexion.prepareStatement(
          "SELECT * FROM Tecnico_Academico WHERE noPersonal = ?");
      String noPersonal = parametro.toString();
      consulta.setString(1, noPersonal);
      ResultSet resultado = consulta.executeQuery();    //Se obtiene el resultado de la consulta
      resultado.next();
      //Se establecen los valores de los atributos de software
      Integer id = resultado.getInt("idTecnico_Academico");
      String correoElectronico = resultado.getString("correoElectronico");
      String entidadAcademica = resultado.getString("entidadAcademica");
      String nombre = resultado.getString("nombre");
      String telefono = resultado.getString("telefono");
      return new TecnicoAcademico(    //Se devuelve el técnico con los valores establecidos
          id, correoElectronico, entidadAcademica, nombre, noPersonal, telefono);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "No es posible buscar el técnico académico",
          "Error de búsqueda", JOptionPane.ERROR_MESSAGE);
    } finally {
      try {
        conexion.close();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Se produjo un error al buscar el técnico académico",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return null;
  }
  
}
