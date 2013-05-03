/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.util.ArrayList;

/**
 *
 * @author Ariel
 */
public class DataRow extends DataComponent{

    
    public DataRow()
    {
        row = new ArrayList<>();
    }
    
    public DataRow(String nazwa)
    {
        row = new ArrayList<>();
        name = nazwa;
        addAttribute("name", name);
    }
    
    @Override
    public void set(DataComponent component) {
        if(component instanceof DataRow)
        {
            row = new ArrayList<>();
            DataRow arg = (DataRow)component;
            tableName = arg.tableName;
            for(DataCell a : arg.row)
            {
                row.add(a);
            }
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błędny typ danych: DataComponent, oczekiwany: DataRow.", "DataRow", "Method: set(DataComponent component)", "component");
            errorDialog.setVisible(true);
        }
    }
    
    public boolean isEmpty()
    {
        return row.isEmpty();
    }
    
    
}
