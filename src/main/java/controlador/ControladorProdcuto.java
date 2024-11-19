package controlador;

import configuracion.CConexion;
import modelos.ModeloProducto;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.Set;

public class ControladorProdcuto {

    public void MostrarProductos(JTable tablaTotalProductos) {
        // Objeto para la conexión a la base de datos
        CConexion objetoConexion = new CConexion();

        // Modelo para manejar los datos de los productos
        ModeloProducto objetoProducto = new ModeloProducto();

        // Modelo para la tabla
        DefaultTableModel modelo = new DefaultTableModel();

        // Definición de columnas en la tabla
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        // Asignar modelo a la tabla
        tablaTotalProductos.setModel(modelo);

        // Consulta SQL
        String sql = "SELECT productos.idproducto, productos.nombre, productos.precioProducto, productos.stock FROM productos";

        try {
            // Crear el statement y ejecutar la consulta
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Recorrer el resultado de la consulta
            while (rs.next()) {
                // Asignar valores a los atributos del producto
                objetoProducto.setIdproducto(rs.getInt("idproducto"));
                objetoProducto.setNombreProducto(rs.getString("nombre"));
                objetoProducto.setPrecioProducto(rs.getDouble("precioProducto"));
                objetoProducto.setStockProducto(rs.getInt("stock"));

                // Agregar una nueva fila al modelo de la tabla
                modelo.addRow(new Object[]{
                    objetoProducto.getIdproducto(),
                    objetoProducto.getNombreProducto(),
                    objetoProducto.getPrecioProducto(),
                    objetoProducto.getStockProducto()
                });
            }
            tablaTotalProductos.setModel(modelo);
        } catch (Exception e) {
            System.err.println("Error al mostrar productos: " + e.getMessage());
        }
    }

    public void agregarProducto(JTextField idproducto , JTextField nombre, JTextField precio, JTextField stock) {
        // Objeto de conexión
        CConexion objetoConexion = new CConexion();
        ModeloProducto objetoProducto = new ModeloProducto();

        // Consulta SQL para insertar producto
        String sql = "INSERT INTO productos (nombre, precioProducto, stock) VALUES (?, ?, ?)";

        try {
            // Preparar los datos del producto
            objetoProducto.setNombreProducto(nombre.getText());
            objetoProducto.setPrecioProducto(Double.parseDouble(precio.getText()));
            objetoProducto.setStockProducto(Integer.parseInt(stock.getText()));

            // Preparar la consulta
            CallableStatement cs = (CallableStatement) objetoConexion.estableceConexion().prepareCall(sql);
            cs.setString(1, objetoProducto.getNombreProducto());
            cs.setDouble(2, objetoProducto.getPrecioProducto());
            cs.setInt(3, objetoProducto.getStockProducto());
            cs.execute();

            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.toString());
        }
    }
     public void Seleccionar(JTable tablaProductos, JTextField idproducto, JTextField nombre, JTextField precio, JTextField stock) {
        int fila = tablaProductos.getSelectedRow();
        try {
            if (fila >= 0) {
                idproducto.setText(tablaProductos.getValueAt(fila, 0).toString());
                nombre.setText(tablaProductos.getValueAt(fila, 1).toString());
                precio.setText(tablaProductos.getValueAt(fila, 2).toString());
                stock.setText(tablaProductos.getValueAt(fila, 3).toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar el producto: " + e.getMessage());
        }
    }
      public void ModificarProducto(JTextField id, JTextField nombre, JTextField precio, JTextField stock) {
        // Objeto de conexión
        CConexion objetoConexion = new CConexion();
        ModeloProducto objetoProducto = new ModeloProducto();

        // Consulta SQL para actualizar los datos del producto
        String sql = "UPDATE productos SET productos.nombre = ?, productos.precioProducto = ?, productos.stock = ? WHERE productos.idproducto = ?";

        try {
            // Preparar los datos del producto
            
            objetoProducto.setIdproducto(Integer.parseInt(id.getText()));
            
            objetoProducto.setNombreProducto(nombre.getText());
            objetoProducto.setPrecioProducto(Double.parseDouble(precio.getText()));
            objetoProducto.setStockProducto(Integer.parseInt(stock.getText()));

            // Preparar la consulta
            CallableStatement cs = (CallableStatement) objetoConexion.estableceConexion().prepareCall(sql);
            cs.setString(1, objetoProducto.getNombreProducto());
            cs.setDouble(2, objetoProducto.getPrecioProducto());
            cs.setInt(3, objetoProducto.getStockProducto());
            cs.setInt(4, objetoProducto.getIdproducto());
            
            // Ejecutar la actualización
            int filasAfectadas = cs.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto modificado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con el ID proporcionado.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar: " + e.toString());
        }
    }
         public void Limpiar(JTextField id, JTextField nombre, JTextField precio, JTextField stock) {
        // Limpiar los campos de texto
        id.setText("");
        nombre.setText("");
        precio.setText("");
        stock.setText("");
}
   public void eliminarProducto(JTextField id, JTable tablaTotalProductos) {
    // Objeto de conexión
    CConexion objetoConexion = new CConexion();

    // Consulta SQL para eliminar el producto
    String sql = "DELETE FROM productos WHERE productos.idproducto = ?";

    try {
        // Verificar que el campo id no esté vacío y sea un número válido
        if (id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de producto.");
            return;
        }

        // Intentar convertir el ID ingresado a un entero
        int idProducto;
        try {
            idProducto = Integer.parseInt(id.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID ingresado no es válido.");
            return;
        }

        // Preparar la consulta SQL
        PreparedStatement ps = objetoConexion.estableceConexion().prepareStatement(sql);
        ps.setInt(1, idProducto);  // Establecer el ID del producto a eliminar

        // Ejecutar la eliminación
        int filasAfectadas = ps.executeUpdate();

        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
            // Actualizar la lista de productos en la tabla
            MostrarProductos(tablaTotalProductos);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el producto con el ID proporcionado.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.toString());
    } finally {
        // Asegúrate de cerrar la conexión después de usarla
        try {
            if (objetoConexion.estableceConexion() != null) {
                objetoConexion.estableceConexion().close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.toString());
        }
    }
}

}



   
