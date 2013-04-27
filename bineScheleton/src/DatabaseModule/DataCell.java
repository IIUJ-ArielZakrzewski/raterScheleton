/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

/**
 *
 * @author Ariel
 */
public class DataCell extends DataComponent{
    String name;
    String value;

    public DataCell(String nazwa)
    {
        name = nazwa;
        value = null;
    }
    
    public DataCell(String nazwa, String wartosc)
    {
        name = nazwa;
        value = wartosc;
    }
    
    @Override
    public void set(DataComponent component) {
        if(component instanceof DataCell)
        {
            DataCell arg = (DataCell)component;
            name = arg.name;
            value = arg.value;
            tableName = arg.tableName;
        } else {
            //Wyświetlanie okna dialogowego o errorze błędnego typu
        }
    }
    
    public void set(String wartosc)
    {
        value = wartosc;
    }
    
}
