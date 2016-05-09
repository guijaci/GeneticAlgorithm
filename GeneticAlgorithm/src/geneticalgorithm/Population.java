/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

/**
 *
 * @author Jacichen
 */
public class Population {
    public static final int POP_MAX = 50;
    
    public static final float CROSSOVER_RATE = .8F;
    public static final float MUTATION_RATE = .05F;
    
    private final Discipline disciplines[];
    private final FitnessFunction ff;
    
    private final Random RNG;

    private List<Individual> population;
    
    Population(Discipline[] disciplines, FitnessFunction ff) throws Exception {
        this.disciplines = disciplines;
        population = new ArrayList<>();
        this.ff = ff;
        RNG = new Random(System.currentTimeMillis());
        initializePopulation();
    }

    private void initializePopulation() throws Exception {
        for(int i = 0; i < POP_MAX; i++){
            boolean cromossome[] = new boolean[disciplines.length];
            for(int j = 0; j < cromossome.length; j++)
                cromossome[j] = RNG.nextBoolean();
            population.add(new Individual(ff, cromossome, disciplines));
        }
    }
    
    public List<Individual> getPopulation(){
        return population;
    }
    
    public List<Individual> selectMates() throws Exception{
        Roullete roullete = new Roullete(this);
        return roullete.spin();
    }

    public List<Individual> makeChildren(List<Individual> selected) throws Exception {
        List<Individual> children = reproduct(selected);
        PrimitiveIterator.OfDouble rngStream = RNG.doubles().iterator();
        for(Individual ind: children)
            for(int i = 0; i < disciplines.length; i++)
                if(rngStream.next().doubleValue() < MUTATION_RATE)
                    ind.mutate(i);
        return children;
    }
    
    public List<Individual> reproduct(List<Individual> selected) throws Exception {
        List<Individual> children = new ArrayList<>();
        //Equalizando o numero de pares para crossover ;)
        if(selected.size()%2 > 0)
            selected.add(selected.get(0));
        PrimitiveIterator.OfDouble rngStream = RNG.doubles().iterator();
        
        Individual mate1,mate2;
        for(int i = 0; i < selected.size()/2; i++){
            mate1 = selected.get(2*i);
            mate2 = selected.get(2*i+1);
            //Rolou Crossover
            if(rngStream.next() < CROSSOVER_RATE){
                //ponto de seção 
                int cutPosition = 0;
                while(cutPosition == 0 || cutPosition == disciplines.length){
                    cutPosition = (int) (rngStream.next().doubleValue()*(double)disciplines.length);
                }
                children.addAll(Individual.doCrossover(mate1, mate2, cutPosition));
            //Não Rolou Crossover
            }else{
                children.add(mate1);
                children.add(mate2);
            }
        }
        
        return children;
    }
    
    public void addToPopulation(List<Individual> newMembers){
        population.addAll(newMembers);
    }

    public Individual selectionOfTheFittest() {
        population.sort(new IndividualComparator());
        while(population.size() > POP_MAX){
            population.remove(0);
        }
        return population.get(population.size()-1);
    }
    
    private class IndividualComparator implements Comparator<Individual>{
        @Override
        public int compare(Individual o1, Individual o2) {
            return Long.signum(o1.getFitness() - o2.getFitness());
        }
    }
}
