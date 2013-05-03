/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Opinions;

import DatabaseModule.DataRow;
import Models.Attributes.AbstractAttribute;
import java.util.List;
/**
 *
 * @author Ariel
 */
public abstract class AbstractOpinion {
    double totalRate;
    String opinion;
    public DataRow row;
    List<AbstractAttribute> attributes;
}
