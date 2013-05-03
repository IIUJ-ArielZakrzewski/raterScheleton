/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;

/**
 *
 * @author Ariel
 */
public class DataCell extends DataComponent{
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
            ErrorDialog errorDialog = new ErrorDialog(true, "Błędny typ danych: DataComponent, oczekiwany: DataCell.", "DataCell", "Method: set(DataComponent component)", "component");
            errorDialog.setVisible(true);
        }
    }
    
    public void set(String wartosc)
    {
        value = wartosc;
    }
    
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String newValue)
    {
        value = newValue;
    }
}
