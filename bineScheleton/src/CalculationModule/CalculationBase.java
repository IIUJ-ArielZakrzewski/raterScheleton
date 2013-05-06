/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculationModule;

import Models.Opinions.AbstractOpinion;

/**
 *
 * @author Ariel
 */
public abstract class CalculationBase {
    String name;
    public abstract double recalculateRate(AbstractOpinion opinion);
    
    public String getName()
    {
        return name;
    }
}
