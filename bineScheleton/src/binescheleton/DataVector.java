/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binescheleton;

import DatabaseModule.DatabaseManager;

/**
 *
 * @author Ariel
 */
public class DataVector {
    public static volatile DataVector instance;
    MainWindow parent;
    public DatabaseManager dbManager;
    
    private DataVector()
    {
        
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
}
