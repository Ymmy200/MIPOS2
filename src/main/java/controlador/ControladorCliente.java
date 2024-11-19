package controlador;

import configuracion.CConexion;
import modelos.ModeloCliente;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.CallableStatement;

public class ControladorCliente {

    public void MostrarClientes(JTable tablaTotalClientes) {
        // Objeto para la conexión a la base de datos
        CConexion objetoConexion = new CConexion();

        // Modelo para manejar los datos de los clientes
        ModeloCliente objetoCliente = new ModeloCliente();

        // Modelo para la tabla
        DefaultTableModel modelo = new DefaultTableModel();

        // Definición de columnas en la tabla
        modelo.addColumn("ID");
        modelo.addColumn("Nombres");
        modelo.addColumn("Primer Apellido");
        modelo.addColumn("Segundo Apellido");

        // Asignar modelo a la tabla
        tablaTotalClientes.setModel(modelo);

        // Consulta SQL
        String sql = "SELECT cliente.idcliente, cliente.nombres, cliente.appaterno, cliente.apmaterno FROM cliente";

        try {
            // Crear el statement y ejecutar la consulta
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Recorrer el resultado de la consulta
            while (rs.next()) {
                // Asignar valores a los atributos del cliente
                objetoCliente.setIdCliente(rs.getInt("idcliente"));
                objetoCliente.setNombres(rs.getString("nombres"));
                objetoCliente.setApPaterno(rs.getString("appaterno"));
                objetoCliente.setApMaterno(rs.getString("apmaterno"));

                // Agregar una nueva fila al modelo de la tabla
                modelo.addRow(new Object[]{
                    objetoCliente.getIdCliente(),
                    objetoCliente.getNombres(),
                    objetoCliente.getApPaterno(),
                    objetoCliente.getApMaterno()
                });
            }
            tablaTotalClientes.setModel(modelo);
        } catch (Exception e) {
            System.err.println("Error al mostrar clientes: " + e.getMessage());
        }
    }

    public void agregarCliente(JTextField nombres, JTextField appaterno, JTextField apmaterno) {
        // Objeto de conexión
        CConexion objetoConexion = new CConexion();
        ModeloCliente objetoCliente = new ModeloCliente();

        // Consulta SQL para insertar cliente
        String sql = "INSERT INTO cliente (nombres, appaterno, apmaterno) VALUES (?, ?, ?)";

        try {
            // Preparar los datos del cliente
            objetoCliente.setNombres(nombres.getText());
            objetoCliente.setApPaterno(appaterno.getText());
            objetoCliente.setApMaterno(apmaterno.getText());

            // Preparar la consulta
            CallableStatement cs = (CallableStatement) objetoConexion.estableceConexion().prepareCall(sql);
            cs.setString(1, objetoCliente.getNombres());
            cs.setString(2, objetoCliente.getApPaterno());
            cs.setString(3, objetoCliente.getApMaterno());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Cliente agregado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.toString());
        }
    }

    public void Seleccionar(JTable totalCliente, JTextField id, JTextField nombres, JTextField appaterno, JTextField apmaterno) {
        int fila = totalCliente.getSelectedRow();
        try {
            if (fila >= 0) {
                id.setText(totalCliente.getValueAt(fila, 0).toString());
                nombres.setText(totalCliente.getValueAt(fila, 1).toString());
                appaterno.setText(totalCliente.getValueAt(fila, 2).toString());
                apmaterno.setText(totalCliente.getValueAt(fila, 3).toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar el cliente: " + e.getMessage());
        }
    }
    
   public void ModificarCliente(JTextField id, JTextField nombres, JTextField appaterno, JTextField apmaterno) {
    // Objeto de conexión
    CConexion objetoConexion = new CConexion();
    ModeloCliente objetoCliente = new ModeloCliente();

    // Consulta SQL para actualizar los datos del cliente
    String sql = "UPDATE cliente SET nombres = ?, appaterno = ?, apmaterno = ? WHERE idcliente = ?";

    try {
        // Preparar los datos del cliente
        objetoCliente.setNombres(nombres.getText());
        objetoCliente.setApPaterno(appaterno.getText());
        objetoCliente.setApMaterno(apmaterno.getText());
        objetoCliente.setIdCliente(Integer.parseInt(id.getText()));

        // Preparar la consulta
        CallableStatement cs = (CallableStatement) objetoConexion.estableceConexion().prepareCall(sql);
        cs.setString(1, objetoCliente.getNombres());
        cs.setString(2, objetoCliente.getApPaterno());
        cs.setString(3, objetoCliente.getApMaterno());
        cs.setInt(4, objetoCliente.getIdCliente());
        
        // Ejecutar la actualización
        int filasAfectadas = cs.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Cliente modificado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el cliente con el ID proporcionado.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al modificar: " + e.toString());
    }
}
   public void Limpiar(JTextField id, JTextField nombres, JTextField appaterno, JTextField apmaterno) {
    // Limpiar los campos de texto
    id.setText("");
    nombres.setText("");
    appaterno.setText("");
    apmaterno.setText("");
}
  public void eliminarClientes(JTextField id, JTable tablaTotalClientes) {
    // Objeto de conexión
    CConexion objetoConexion = new CConexion();

    // Consulta SQL para eliminar el cliente
    String sql = "DELETE FROM cliente WHERE cliente.idcliente = ?";

    try {
        // Verificar que el campo id no esté vacío
        if (id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de cliente.");
            return;
        }

        // Preparar la consulta
        CallableStatement cs = (CallableStatement) objetoConexion.estableceConexion().prepareCall(sql);
        cs.setInt(1, Integer.parseInt(id.getText()));  // Establecer el ID del cliente a eliminar

        // Ejecutar la eliminación
        int filasAfectadas = cs.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.");
            // Actualizar la lista de clientes en la tabla
            MostrarClientes(tablaTotalClientes);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el cliente con el ID proporcionado.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.toString());
    }
}




}
