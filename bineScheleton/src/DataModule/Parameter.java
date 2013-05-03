/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModule;

/**
 *
 * @author Ariel
 */
public class Parameter {
    String name;
    String value;
    
    public Parameter(String nazwa)
    {
        name = nazwa;
        value = "";
    }
    
    public Parameter(String nazwa, String wartosc)
    {
        name = nazwa;
        value = wartosc;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setValue(String wartosc)
    {
        value = wartosc;
    }
    
    public String getValue()
    {
        return value;
    }
}
