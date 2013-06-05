/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Opinions;

import DataModule.Parameter;
import DatabaseModule.DataCell;
import DatabaseModule.DataRow;
import Models.Attributes.AbstractAttribute;
import binescheleton.DataVector;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author Ariel
 */
public abstract class AbstractOpinion {
    double totalRate;
    String name;
    String opinion;
    public DataRow row;
    List<AbstractAttribute> attributes;
    List<Parameter> parameters;
    
    public AbstractOpinion(String nazwa)
    {
        totalRate = 0.0;
        name = nazwa;
        opinion = "";
        row = new DataRow(nazwa);
        attributes = new LinkedList<>();
        parameters = new LinkedList<>();
    }
    
    public AbstractOpinion(DataRow obiekt)
    {
        attributes = new LinkedList<>();
        parameters = new LinkedList<>();
        for(DataCell cell : obiekt.row)
        {
            switch (cell.getName()) {
                case "name":
                    name = cell.getValue();
                    break;
                case "opinion":
                    opinion = cell.getValue();
                    break;
                case "rate":
                    totalRate = Double.parseDouble(cell.getValue());
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
            parameters.add(p);
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
            row.addAttribute("rate", totalRate + "");
            row.addAttribute("opinion", opinion);
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
            if(row.containsAttribute("rate"))
            {
                row.update("rate", totalRate + "");
            } else {
                row.addAttribute("rate", totalRate + "");
            }
            
            if(row.containsAttribute("opinion"))
            {
                row.update("opinion", opinion);
            } else {
                row.addAttribute("opinion", opinion);
            }
            
            
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
    
    public String getOpinion()
    {
        return opinion;
    }
    
    public void setOpinion(String newOpinion)
    {
        opinion = newOpinion;
        if(row.containsAttribute("opinion"))
        {
            row.update("opinion", opinion);
        } else {
            row.addAttribute("opinion", opinion);
        }
    }
    
    public void setTotalRate(double newRate)
    {
        totalRate = newRate;
        if(row.containsAttribute("rate"))
        {
            row.update("rate", totalRate + "");
        } else {
            row.addAttribute("rate", totalRate + "");
        }
    }
    
    public double getTotalRate()
    {
        return totalRate;
    }
    
    public DataRow getRow()
    {
        return row;
    }
    
}
