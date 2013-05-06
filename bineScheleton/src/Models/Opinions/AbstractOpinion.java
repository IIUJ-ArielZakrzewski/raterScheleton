/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Opinions;

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
    
    public AbstractOpinion(String nazwa)
    {
        totalRate = 0.0;
        name = nazwa;
        opinion = "";
        row = new DataRow(nazwa);
        attributes = new LinkedList<>();
    }
    
    public AbstractOpinion(DataRow obiekt)
    {
        attributes = new LinkedList<>();
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
                    DataRow parametry = new DataRow();
                    parametry.setTableName(cell.getName());
                    parametry.addAttribute("name", cell.getValue());
                    attributes.add(new AbstractAttribute(DataVector.getInstance().dbManager.select(parametry).get(0)));
            }
        }
        
        row = obiekt;
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
        //W klasie pochodnej powinna zostać przeładowana ta metoda
        //Następnie powinna zostać wywołana z klasy bazowej
        //A następnie poniższa linia powinna zostać dodana, z uwzględnieniem odpowiedniej nazwy
        //totalRate = DataVector.getInstance().getCalculation(<nazwa>).recalculateRate(this);
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
        //W klasie pochodnej powinna zostać przeładowana ta metoda
        //Następnie powinna zostać wywołana z klasy bazowej
        //A następnie poniższa linia powinna zostać dodana, z uwzględnieniem odpowiedniej nazwy
        //totalRate = DataVector.getInstance().getCalculation(<nazwa>).recalculateRate(this);
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
            for(AbstractAttribute a : attributes)
            {
                row.addAttribute(a.getType(), a.getName());
            }
            DataVector.getInstance().dbManager.insert(row);
        } else {
            row.update("rate", totalRate + "");
            row.update("opinion", opinion);
            DataRow param = new DataRow(row.getName());
            param.setTableName(row.getTableName());
            DataVector.getInstance().dbManager.update(param, row);
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
    }
    
    public double getTotalRate()
    {
        return totalRate;
    }
    
}
