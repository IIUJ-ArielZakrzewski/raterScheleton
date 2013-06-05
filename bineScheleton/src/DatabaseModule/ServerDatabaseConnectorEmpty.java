/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import java.util.List;

/**
 *
 * @author Ariel
 */
public class ServerDatabaseConnectorEmpty extends ServerDatabaseConnector{

    @Override
    public boolean insert(DataRow row) {
        return false;
    }

    @Override
    public boolean update(DataRow parameters, DataRow row) {
        return false;
    }

    @Override
    public boolean remove(DataRow row) {
        return false;
    }

    @Override
    public List<DataRow> select(DataRow parameters) {
        return null;
    }
    
    @Override
    public boolean checkConnection()
    {
        return false;
    }
    
}
