/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Objects;

import DatabaseModule.DataComponent;
import Models.Attributes.AbstractAttribute;
import Models.Opinions.AbstractOpinion;
import java.util.List;
/**
 *
 * @author Ariel
 */
public abstract class AbstractObject {
    String name;
    String description;
    DataComponent row;
    AbstractOpinion opinion;
    List<AbstractAttribute> attributes;
}
