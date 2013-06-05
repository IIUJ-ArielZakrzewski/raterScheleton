/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Objects;

import DataModule.Parameter;
import DatabaseModule.DataCell;
import DatabaseModule.DataRow;
import Dialogs.ErrorDialog;
import Models.Attributes.AbstractAttribute;
import Models.Opinions.AbstractOpinion;
import binescheleton.DataVector;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author Ariel
 */
public abstract class AbstractObject {
    String name;
    String description;
    DataRow row;
    AbstractOpinion opinion;
    List<AbstractAttribute> attributes;
    List<Parameter> parameters;
    
    public abstract void createOpinion();
    public abstract void createOpinion(DataRow row);
    public abstract void changeOpinion();
    
    public AbstractObject()
    {
        name = "";
        description = "";
        row = new DataRow();
        opinion = null;
        attributes = new LinkedList<>();
        parameters = new LinkedList<>();
    }
    
    public AbstractObject(DataRow obiekt)
    {
        opinion = null;
        attributes = new LinkedList<>();
        parameters = new LinkedList<>();
        for(DataCell cell : obiekt.row)
        {
            switch (cell.getName()) {
                case "name":
                    name = cell.getValue();
                    break;
                case "description":
                    description = cell.getValue();
                    break;
                default:
                    if(DataVector.getInstance().dbManager.isAtrribute(cell.getName()))
                    {
                        DataRow parametry = new DataRow();
                        parametry.setTableName(cell.getName());
                        parametry.addAttribute("name", cell.getValue());
                        attributes.add(new AbstractAttribute(DataVector.getInstance().dbManager.select(parametry).get(0)));
                    } else {
                        parameters.add(new Parameter(cell.getName(), cell.getValue()));
                    }
                    
            }
        }
        
        row = obiekt;
    }
    
    public AbstractObject(DataRow obiekt, DataRow opinia)
    {
        //Należy w klasie pochodnej wywołać metodę
        //createOpinie(opinia);
        attributes = new LinkedList<>();
        parameters = new LinkedList<>();
        for(DataCell cell : obiekt.row)
        {
            switch (cell.getName()) {
                case "name":
                    name = cell.getValue();
                    break;
                case "description":
                    description = cell.getValue();
                    break;
                default:
                    if(DataVector.getInstance().dbManager.isAtrribute(cell.getName()))
                    {
                        DataRow parametry = new DataRow();
                        parametry.setTableName(cell.getName());
                        parametry.addAttribute("name", cell.getValue());
                        attributes.add(new AbstractAttribute(DataVector.getInstance().dbManager.select(parametry).get(0)));
                    } else {
                        parameters.add(new Parameter(cell.getName(), cell.getValue()));
                    }
                    
            }
        }
        
        row = obiekt;
    }
    
    public void removeOpinion()
    {
        DataVector.getInstance().dbManager.remove(opinion.row);
        opinion = null;
    }
    
    public Parameter getParameter(String nazwa)
    {
        for(Parameter p : parameters)
        {
            if(p.getName().equals(nazwa))
                return p;
        }
        return null;
    }
    
    public void setParameter(String nazwa, String wartosc)
    {
        Parameter p = getParameter(nazwa);
        if(p == null)
        {
            p = new Parameter(nazwa, wartosc);
        } else {
            p.setValue(wartosc);
        }
        if(row.containsAttribute(p.getName()))
        {
            row.update(p.getName(), p.getValue());
        } else {
            row.insert(p.getName(), p.getValue());
        }
        
    }
    
    public void removeParameter(String nazwa)
    {
        getParameter(nazwa).setValue("");
        row.update(getParameter(nazwa).getName(), getParameter(nazwa).getValue());
    }
    
    public AbstractAttribute getAttribute(String name)
    {
        for(AbstractAttribute atrybut : attributes)
        {
            if(atrybut.getType().equals(name))
            {
                return atrybut;
            }
        }
        
        return null;
    }
    
    public void setAttribute(String nazwa, String wartosc)
    {
        DataRow parametry = new DataRow();
        parametry.setTableName(nazwa);
        parametry.addAttribute("name", wartosc);
        for(AbstractAttribute atrybut : attributes)
        {
            if(atrybut.getType().equals(nazwa))
            {
                atrybut.load(DataVector.getInstance().dbManager.select(parametry).get(0));
                if(row.containsAttribute(atrybut.getName()))
                {
                    row.update(nazwa, wartosc);
                } else {
                    row.insert(nazwa, wartosc);
                }
                
            }
        }
    }
    
    public void removeAttribute(String nazwa)
    {
        DataRow parametry = new DataRow();
        parametry.setTableName(nazwa);
        for(AbstractAttribute atrybut : attributes)
        {
            if(atrybut.getType().equals(nazwa))
            {
                atrybut = new AbstractAttribute(nazwa);
                row.update(nazwa, "");
            }
        }
    }
    
    public void save()
    {
        //W klasie docelowej należy sprawdzić, czy row.isEmpty()
        //jeśli tak, to należy nadać nazwę tabeli, do ktrej zostanie wstawiony wiersz
        //należy to zrobić za pomocą row.setTableName(<nazwa>);
        //Dopiero potem należy wywołać save z klasy bazowej
        if(row.isEmpty())
        {
            row.addAttribute("name", name);
            row.addAttribute("description", description);
            for(Parameter p : parameters)
            {
                row.addAttribute(p.getName(), p.getValue());
            }
            for(AbstractAttribute a : attributes)
            {
                row.addAttribute(a.getType(), a.getName());
            }
            DataVector.getInstance().dbManager.insert(row);
        } else {
            row.update("description", description);
            DataRow param = new DataRow(row.getName());
            param.setTableName(row.getTableName());
            param.addAttribute("userName", DataVector.getInstance().dbManager.getUserName());
            List<DataRow> res = DataVector.getInstance().dbManager.select(param);
            if(!res.isEmpty())
            {
                DataVector.getInstance().dbManager.update(param, row);
            } else {
                DataVector.getInstance().dbManager.insert(row);
            }
        }
    }
            
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        if(name.equals(""))
        {
            name = newName;
            row.addAttribute("name", name);
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nie można zmienić istniejącej nazwy.", "AbstractObject", "Method: setName", "newName");
            errorDialog.setVisible(true);
        }
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String newDescription)
    {
        description = newDescription;
        if(row.containsAttribute("description"))
        {
            row.update("description", description);
        } else {
            row.addAttribute("description", description);
        }
        
    }
    
    public AbstractOpinion getOpinion()
    {
        return opinion;
    }
    
    public void setOpinion(AbstractOpinion newOpinion)
    {
        opinion = newOpinion;
    }
    
    public DataRow getRow()
    {
        return row;
    }
    
    
}
