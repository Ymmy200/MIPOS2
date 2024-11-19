/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CConexion {
    Connection conectar = null;
    
    
    String usuario = "root";
    String contrasenia = "";
    String bd = "dbpos";
    String ip = "localhost";
    String puerto = "3306"; 

   
    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection estableceConexion() {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
            JOptionPane.showMessageDialog(null, "Conexión correcta");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.toString());
        }
        return conectar; 
    }
    
   public void cerrarconexion() {
    try {
        if (conectar != null && !conectar.isClosed()) {
            conectar.close();
            JOptionPane.showMessageDialog(null, "Conexión cerrada correctamente");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.toString());
    }
}

}
