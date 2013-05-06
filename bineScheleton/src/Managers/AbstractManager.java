/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import DatabaseModule.DataRow;
import Dialogs.ErrorDialog;
import Models.Objects.AbstractObject;
import Models.Opinions.AbstractOpinion;
import Panels.AbstractPanel;
import binescheleton.DataVector;
import java.util.List;

/**
 *
 * @author Ariel
 */
public abstract class AbstractManager {
    List<AbstractObject> objects;
    List<AbstractOpinion> opinions;
    
    public abstract void addSubject();
    public abstract void addOpinion();
    
    public void editSubject(DataRow row)
    {
        AbstractObject subject = null;
        for(AbstractObject o : objects)
        {
            if(o.getName().equals(row.getAttribute("name").getValue()))
            {
                subject = o;
            }
        }
        
        if(subject != null)
        {
            //wyświetlanie okna dialogowego do edycji obiektu
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nie odnaleziono obiektu.", "AbstractManager", "Method: editSubject(DataRow row)", "subject, row");
            errorDialog.show();
        }
    }
    
    public void removeSubject(DataRow row)
    {
        boolean result = DataVector.getInstance().dbManager.remove(row);
        if(!result)
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd w usuwaniu obiektu: " + row.getName() + ".", "AbstractManager", "Method: removeSubject(DataRow row)", "row");
            errorDialog.show();
        }
    }
    
    public void editOpinion(DataRow row)
    {
        AbstractOpinion subject = null;
        for(AbstractOpinion o : opinions)
        {
            if(o.getName().equals(row.getAttribute("name").getValue()))
            {
                subject = o;
            }
        }
        
        if(subject != null)
        {
            //wyświetlanie okna dialogowego do edycji opinii
        } else {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nie odnaleziono opinii.", "AbstractManager", "Method: editOpinion(DataRow row)", "subject, row");
            errorDialog.show();
        }
    }
    
    public void removeOpinion(DataRow row)
    {
        boolean result = DataVector.getInstance().dbManager.remove(row);
        if(!result)
        {
            ErrorDialog errorDialog = new ErrorDialog(true, "Nastąpił błąd w usuwaniu opinii: " + row.getName() + ".", "AbstractManager", "Method: removeOpinion(DataRow row)", "row");
            errorDialog.show();
        }
    }
}
