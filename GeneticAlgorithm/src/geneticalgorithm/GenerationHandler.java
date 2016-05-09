/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.util.List;

/**
 *
 * @author Jacichen
 */
public class GenerationHandler {
    final private Discipline disciplines[];
    final private Population population;
    final private FitnessFunction ff;
    
    public final long MAX_ITER = 1000000;
    public static final int STAGNATION_LIMIT = 1000;
    
    private long lastBestFitness;
    private int gensStagnated;
    
    private Individual bestFitted;
    
    GenerationHandler(Discipline[] disciplines) throws Exception {
        this.disciplines = disciplines;
        ff = new FitnessFunction(disciplines);
        population = new Population(disciplines, ff);
    }
    
    public Individual execute() throws Exception{
        bestFitted = population.getPopulation().get(0);
        int i;
        for(i = 0; i < MAX_ITER && bestFitted.getFitness() < ff.getBestFitnessPossible() && 
                gensStagnated < STAGNATION_LIMIT; i++){
            if(lastBestFitness == bestFitted.getFitness())
                gensStagnated++;
            else
                gensStagnated = 0;
            lastBestFitness = bestFitted.getFitness();
            
            List<Individual> selected = population.selectMates();
            List<Individual> children = population.makeChildren(selected);
            
            population.addToPopulation(children);
            
            bestFitted = population.selectionOfTheFittest();
            lastBestFitness = bestFitted.getFitness();
        }
        return bestFitted;
    }
}
