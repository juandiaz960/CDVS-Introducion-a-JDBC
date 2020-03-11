/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cvds.sampleprj.jdbc.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class JDBCExample {
    
    public static void main(String args[]){
        try {
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/bdprueba";
            String driver="com.mysql.jdbc.Driver";
            String user="bdprueba";
            String pwd="prueba2019";
                        
            Class.forName(driver);
            Connection con=DriverManager.getConnection(url,user,pwd);
            con.setAutoCommit(false);
                 
            
            System.out.println("Valor total pedido 1:"+valorTotalPedido(con, 1));
            
            List<String> prodsPedido=nombresProductosPedido(con, 1);
            
            
            System.out.println("Productos del pedido 1:");
            System.out.println("-----------------------");
            for (String nomprod:prodsPedido){
                System.out.println(nomprod);
            }
            System.out.println("-----------------------");
            
            
            int suCodigoECI=2104587;
            registrarNuevoProducto(con, suCodigoECI, "SU NOMBRE", 99999999);            
            con.commit();
                        
            
            con.close();
                                   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(JDBCExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Agregar un nuevo producto con los parámetros dados
     * @param con la conexión JDBC
     * @param codigo
     * @param nombre
     * @param precio
     * @throws SQLException 
     */
    public static void registrarNuevoProducto(Connection con, int codigo, String nombre,int precio) throws SQLException{
        String consulta = "INSERT INTO ORD_PRODUCTOS (codigo, nombre, precio) values (?,?,?)";          
        //Crear preparedStatement
        PreparedStatement agregarProducto = con.prepareStatement(consulta);        
        //Asignar parámetros
        agregarProducto.setInt(1, codigo);
        agregarProducto.setString(2, nombre);
        agregarProducto.setInt(3, precio);
        //usar 'execute'
        agregarProducto.execute();
	con.commit();
        
    }
    
    /**
     * Consultar los nombres de los productos asociados a un pedido
     * @param con la conexión JDBC
     * @param codigoPedido el código del pedido
     * @return np
     * @throws SQLException
     */
    public static List<String> nombresProductosPedido(Connection con, int codigoPedido) throws SQLException{
        List<String> np=new LinkedList<>();
        String query = "SELECT nombre FROM ORD_DETALLE_PEDIDO dp , ORD_PRODUCTOS p WHERE dp.producto_fk = p.codigo AND dp.pedido_fk = ?";
        PreparedStatement npPedido = null;
	//Crear prepared statement
        npPedido = con.prepareStatement(query);
        //asignar parámetros
        npPedido.setInt(1, codigoPedido);
        //usar executeQuery 
        ResultSet rS = npPedido.executeQuery();
        //Sacar resultados del ResultSet
        while (rS.next()) {
            String nombre= rS.getString("nombre");
	    np.add(nombre);
	}
        //Llenar la lista y retornarla
        return np;
    }

    
    /**
     * Calcular el costo total de un pedido
     * @param con
     * @param codigoPedido código del pedido cuyo total se calculará
     * @return el costo total del pedido (suma de: cantidades*precios)
     * @throws SQLExeption
     */
    public static int valorTotalPedido(Connection con, int codigoPedido) throws SQLException{
        
        String query = "SELECT SUM(cantidad*ORD_PRODUCTOS.precio) FROM ORD_DETALLE_PEDIDO,ORD_PRODUCTOS WHERE producto_fk = ORD_PRODUCTOS.codigo && pedido_fk = ?;";
        PreparedStatement totalPedido = null;
        //Crear prepared statement
        totalPedido = con.prepareStatement(query);
        //asignar parámetros
        totalPedido.setInt(1, codigoPedido);
        //usar executeQuery
        ResultSet rS = totalPedido.executeQuery();
        //Sacar resultado del ResultSet
        int costoPedido = 0;
        while(rS.next()){
            costoPedido = rS.getInt(1);                
        }
        return costoPedido;
    }    
    
}