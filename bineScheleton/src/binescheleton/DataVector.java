/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binescheleton;

/**
 *
 * @author Ariel
 */
public class DataVector {
    public static volatile DataVector instance;
    
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
}
