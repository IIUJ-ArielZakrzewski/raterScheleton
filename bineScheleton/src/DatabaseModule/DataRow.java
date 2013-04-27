/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseModule;

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
            //Wyświetlanie okna dialogowego o errorze błędnego typu dla argumentu wejściowego
        }
    }
    
    public void addAttribute(String name)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                //Wyświetlanie okna dialogowego o errorze istniejęcego atrybutu a danej nazwie
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
                //Wyświetlanie okna dialogowego o errorze istniejęcego atrybutu a danej nazwie
                return;
            }
        }
        DataCell newCell = new DataCell(name,value);
        row.add(newCell);
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
        //Wyświetlanie okna dialogowego o errorze nieistniejęcego atrybutu a danej nazwie
    }
    
    public void insert(String name, String value)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                if(a.value == null)
                {
                    a.value = value;
                } else {
                    //Wyświetlanie okna dialogowego o errorze ustalonej wartości podczas insert
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
        //Wyświetlanie okna dialogowego o errorze nieistniejęcego atrybutu a danej nazwie
    }
    
    public void remove(String name)
    {
        for(DataCell a : row)
        {
            if(a.name.equals(name))
            {
                a.value = null;
                return;
            }
        }
        //Wyświetlanie okna dialogowego o errorze nieistniejęcego atrybutu a danej nazwie
    }
    
}
