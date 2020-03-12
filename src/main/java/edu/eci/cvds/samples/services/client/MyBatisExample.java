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
package edu.eci.cvds.samples.services.client;

import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ClienteMapper;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static javax.xml.bind.DatatypeConverter.parseDate;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();
        
        //Crear el mapper y usarlo: 
        ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);
        // Consulta de todos los clientes.
        System.out.println(cm.consultarClientes());
        
        System.out.println(" ----------------------------------------------------------------------------------- ");
        System.out.println(" ----------------------------------------------------------------------------------- ");
        
        // Consulta del cliente de documento #6 llamado Elkin.
        System.out.println(cm.consultarCliente(6));
        
        // Se agrega el itemRentado 15 "Pollo" al cliente #6 Elkin con las fechas respectivas. 
        cm.agregarItemRentadoACliente(6, 3, dateFormat("12/03/2020"), dateFormat("12/05/2020"));
        
        sqlss.commit();
        sqlss.close();
    }
    
    public static Date dateFormat(String date) {
        Date format;
        try {
            format = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            format = null;
        }
        return format;
    }
    

}