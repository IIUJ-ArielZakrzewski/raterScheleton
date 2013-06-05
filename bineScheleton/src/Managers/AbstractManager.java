/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import DatabaseModule.DataRow;
import Dialogs.ErrorDialog;
import Models.Objects.AbstractObject;
import Models.Opinions.AbstractOpinion;
import binescheleton.DataVector;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ariel
 */
public abstract class AbstractManager {
    List<AbstractObject> objects;
    List<AbstractOpinion> opinions;
    
    public AbstractManager()
    {
        objects = new LinkedList<>();
        opinions = new LinkedList<>();
    }
    
    public abstract void addSubject();
    public abstract void addOpinion(String subjectName);
    public abstract void loadData();
    public abstract void refreshData();
    public abstract void editSubject(DataRow row);
    public abstract void editOpinion(DataRow row);
    
    public void removeSubject(DataRow row)
    {
        for(AbstractOpinion op : opinions)
        {
            if(op.getName().equals(row.getName()))
            {
                removeOpinion(op.row);
            }
        }
        boolean result = DataVector.getInstance().dbManager.remove(row);
        if(!result)
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd w usuwaniu obiektu: " + row.getName() + ".", "AbstractManager", "Method: removeSubject(DataRow row)", "row");
            errorDialog.setVisible(true);
            return;
        }
        for(AbstractObject a : objects)
        {
            if(a.getName().equals(row.getAttribute("name").getValue()))
            {
                objects.remove(a);
                refreshData();
                return;
            }
        }
    }
    
    public void removeOpinion(DataRow row)
    {
        boolean result = DataVector.getInstance().dbManager.remove(row);
        if(!result)
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd w usuwaniu opinii: " + row.getName() + ".", "AbstractManager", "Method: removeOpinion(DataRow row)", "row");
            errorDialog.setVisible(true);
            return;
        }
        for(AbstractOpinion a : opinions)
        {
            if(a.getName().equals(row.getName()))
            {
                opinions.remove(a);
                refreshData();
                return;
            }
        }
    }
    
    public List<AbstractObject> getObjects()
    {
        return objects;
    }
    
    public AbstractObject getObject(String name)
    {
        for(AbstractObject o : objects)
        {
            if(o.getName().equals(name))
            {
                return o;
            }
        }
        return null;
    }
    
    public AbstractOpinion getOpinion(String name)
    {
        for(AbstractOpinion o : opinions)
        {
            if(o.getName().equals(name))
                return o;
        }
        return null;
    }
    
    
}
