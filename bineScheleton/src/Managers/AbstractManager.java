/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import Models.Objects.AbstractObject;
import Models.Opinions.AbstractOpinion;
import java.util.List;

/**
 *
 * @author Ariel
 */
public abstract class AbstractManager {
    List<AbstractObject> objects;
    List<AbstractOpinion> opinions;
}
