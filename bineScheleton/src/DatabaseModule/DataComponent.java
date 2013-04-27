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
public abstract class DataComponent {
    String tableName;
    List<DataCell> row;
    
    public abstract void set(DataComponent component);

    
    public void setTableName(String name)
    {
        tableName = name;
    }
}
