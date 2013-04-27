/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Opinions;

import DatabaseModule.DataComponent;
import Models.Attributes.AbstractAttribute;
import java.util.List;
/**
 *
 * @author Ariel
 */
public abstract class AbstractOpinion {
    double totalRate;
    String opinion;
    DataComponent row;
    List<AbstractAttribute> attributes;
}
