/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticalgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

public class Roullete {
    private final Random RNG;
    private final Population population;
    private final int numSpins;
    
    private long totalFitness = 0;
    private long lesserNegativeFitness = 0;    
    
    public Roullete(Population population) {
        this.population = population;
        //K = n, ou o numero de spins é o numero de elementos
        numSpins = population.getPopulation().size();
        RNG = new Random(System.currentTimeMillis());
        calculateNormalizedTotalFitness();
    }
    
    public List<Individual> spin(){
        List<Individual> selected = new ArrayList<>();
        PrimitiveIterator.OfDouble rngStream = RNG.doubles().iterator();
        
        for(int i = 0; i < numSpins; i++){
            double soma = getProbability(i);
            double r = rngStream.next().doubleValue();
            int j = i;
            while(soma < r){
                j = (j+1)%numSpins;
                soma = soma + getProbability(j);
            }
            selected.add(population.getPopulation().get(i));
        }
                
        return selected;
    }
    
    private double getProbability(int i){
        List<Individual> parents = population.getPopulation();
        if(i >= parents.size())
            return 0;
        //Fitness normalizado: desloca-se o menor negativo até zero, e todos os 
        //outros fitness a mesma quantidade para se obter o módulo e a 
        //probabilidade correta.
        long normalizedFitness = parents.get(i).getFitness() - lesserNegativeFitness;
        return (double)normalizedFitness/(double)totalFitness;
    }

    private void calculateNormalizedTotalFitness() {
        List<Individual> parents = population.getPopulation();
        long fit;
        for(Individual ind: parents){
            fit = ind.getFitness();
            totalFitness += fit;
            if(fit < lesserNegativeFitness)
                lesserNegativeFitness = fit;
        }
        //Normalização para numeros negativos: o menor de todos (negativo) equivale à zero 
        //e todos elementos serão deslocados a mesma quantidade
        //[multiplica-se o menor valor negativo pelo numero de elementos para 
        //simular este deslocamento para todos eles, por fim somando o módulo]
        totalFitness -= lesserNegativeFitness*parents.size();  
        
        //Evitando divisão por zero caso todos os fitness sejam iguais 
        //e negativos
        if(totalFitness == 0){
            //todos terão mesma chance
            totalFitness = parents.size();
            lesserNegativeFitness -= 1;
        }
    }
}
