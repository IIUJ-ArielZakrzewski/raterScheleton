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
public abstract class DatabaseConnector {
    
    String login;
    String password;
    String port;
    
    public abstract boolean insert(DataRow row);
    public abstract boolean update(DataRow parameters, DataRow row);
    public abstract boolean remove(DataRow row);
    public abstract List<DataRow> select(DataRow parameters);
}
