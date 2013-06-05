/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binescheleton;

import CalculationModule.CalculationBase;
import DatabaseModule.DatabaseManager;
import DatabaseModule.LocalDatabaseConnectorSqlServer;
import DatabaseModule.ServerDatabaseConnectorEmpty;
import EnumModule.EnumBase;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ariel
 */
public class DataVector {
    static volatile DataVector instance;
    MainWindow parent;
    public DatabaseManager dbManager;
    List<CalculationBase> calculations;
    List<EnumBase> enums;
    
    private DataVector()
    {
        calculations = new LinkedList<>();
        enums = new LinkedList<>();
    }
    
    public static DataVector getInstance()
    {
        if(instance == null)
                {
                    synchronized(DataVector.class)
                    {
                        if(instance == null)
                        {
                            instance = new DataVector();
                        }
                    }
                }
        return instance;
    }
    
    public MainWindow getMainWindow()
    {
        return parent;
    }
    
    public void setMainWindow(MainWindow window)
    {
        parent = window;
    }
    
    public void addCalculation(CalculationBase calc)
    {
        calculations.add(calc);
    }
    
    public CalculationBase getCalculation(String nazwa)
    {
        for(CalculationBase c : calculations)
        {
            if(c.getName().equals(nazwa))
            {
                return c;
            }
        }
        
        return null;
    }
}
