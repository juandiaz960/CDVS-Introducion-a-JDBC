/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import edu.eci.cvds.samples.entities.TipoItem;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 2120046
 */
public interface ItemRentadoMapper {
    public List<TipoItem> getItemsRentados();
    
    public TipoItem getItemRentado(int id);
    
    public void addItemRentado(int cliid,int itemid,Date fechaini,Date fechafin);
}