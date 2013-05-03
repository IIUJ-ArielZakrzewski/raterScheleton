/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

import Dialogs.ErrorDialog;
import java.util.List;

/**
 *
 * @author Ariel
 */
public abstract class DataComponent {
    String tableName;
    String name;
    public List<DataCell> row;
    
    public abstract void set(DataComponent component);

    
    public void setTableName(String name)
    {
        tableName = name;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public DataCell getAttribute(String nazwa)
    {
        DataCell result = null;
        for(DataCell cell : row)
        {
            if(cell.name.equals(nazwa))
            {
                result = cell;
            }
        }
        return result;
    }
    
    public void addAttribute(String name)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                ErrorDialog errorDialog = new ErrorDialog(true, "Atrybut o podanej nazwie: " + a.name + " już istnieje.", "DataComponent", "Method: addAttribute(String name)", "name");
                errorDialog.setVisible(true);
                return;
            }
        }
        DataCell newCell = new DataCell(name);
        row.add(newCell);
    }
    
    public void addAttribute(String name, String value)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                ErrorDialog errorDialog = new ErrorDialog(true, "Atrybut o podanej nazwie: " + a.name + " już istnieje.", "DataComponent", "Method: addAttribute(String name, String value)", "name, value");
                errorDialog.setVisible(true);
                return;
            }
        }
        DataCell newCell = new DataCell(name,value);
        row.add(newCell);
    }
    
    public boolean containsAttribute(String name)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                return true;
            }
        }
        return false;
    }
    
    public void removeAttribute(String name)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                row.remove(a);
                return;
            }
        }
        ErrorDialog errorDialog = new ErrorDialog(true, "Atrybut o podanej nazwie: " + name + " nie istnieje. Nie można usunąć atrybutu.", "DataComponent", "Method: removeAttribute(String name)", "name");
        errorDialog.setVisible(true);
    }
    
    public void insert(String name, String value)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                if(a.value.equals(""))
                {
                    a.value = value;
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(true, "Wartość atrybutu o podanej nazwie: " + a.name + " już istnieje. Nie można wykonać operacji insert. Sugerowana operacja: update.", "DataComponent", "Method: insert(String name, String value)", "name, value");
                    errorDialog.setVisible(true);
                }
                return;
            }
        }
        addAttribute(name, value);
    }
    
    public void update(String name, String value)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                a.value = value;
                return;
            }
        }
        ErrorDialog errorDialog = new ErrorDialog(true, "Atrybut o podanej nazwie: " + name + " nie istnieje. Nie można wykonać operacji update.", "DataComponent", "Method: update(String name, String value)", "name, value");
        errorDialog.setVisible(true);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
}
