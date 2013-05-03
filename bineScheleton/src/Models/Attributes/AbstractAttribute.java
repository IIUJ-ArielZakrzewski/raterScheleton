/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Attributes;

import DatabaseModule.DataCell;
import DatabaseModule.DataComponent;
import DatabaseModule.DataRow;
import Dialogs.ErrorDialog;
import binescheleton.DataVector;
/**
 *
 * @author Ariel
 */
public class AbstractAttribute {
    String type;
    String name;
    String description;
    DataRow row;
    
    public AbstractAttribute(String tabela)
    {
        name = "";
        description = "";
        type = tabela;
        row = new DataRow();
        row.setTableName(type);
    }
    
    public AbstractAttribute(DataComponent data)
    {
        if(data instanceof DataRow)
        {
            type = data.getTableName();
            DataCell info = data.getAttribute("name");
            if(info != null)
            {
                name = info.getValue();
            }
            info = data.getAttribute("description");
            if(info != null)
            {
                name = info.getValue();
            }
            row = (DataRow)data;
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błędny typ danych: DataComponent, oczekiwany: DataRow.", "AbstractAttribute", "Constructor: AbstractAttribute(DataComponent data)", "data");
            errorDialog.setVisible(true);
        }
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        name = newName;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String newDescription)
    {
        description = newDescription;
    }
    
    public void load(DataComponent data)
    {
        if(data instanceof DataRow)
        {
            description = data.getAttribute("description").getValue();
            name = data.getAttribute("name").getValue();
            row = (DataRow) data;
            type = data.getTableName();
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Błędny typ danych: DataComponent, oczekiwany: DataRow.", "AbstractAttribute", "Method: load(DataComponent data)", "data");
            errorDialog.setVisible(true);
        }
    }
    
    public void save()
    {
        if(row.isEmpty())
        {
            row.addAttribute("name", name);
            row.addAttribute("description", description);
            DataVector.getInstance().dbManager.insert(row);
        } else {
            row.update("name", name);
            row.update("description", description);
            DataVector.getInstance().dbManager.update(row, row);
        }
        
    }
}
