
package geneticalgorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FitnessFunction {
    final Discipline discips[];
    final int maximumCredits;
    Discipline obrigatories[];
    
    public FitnessFunction(Discipline[] discips) {
        this.discips = discips;
        int sumCredits = 0;
        for(Discipline d: discips)
            sumCredits += d.getCredits();
        maximumCredits = sumCredits;
    }
    
    public long getBestFitnessPossible(){
        return maximumCredits;
    }
    
    public long getFitness(boolean state[]) throws Exception{
        if(state.length != discips.length)
            throw new Exception("Vetor de seleção de disciplinas possui tamanho diferente do vetor de diciplinas");
        
        long fitnessSum = 0;
        List<Integer> totalIntervals = new ArrayList<>();
        
        for(int i = 0; i < state.length; i++){
            if(state[i]){
                fitnessSum += discips[i].getCredits();
                totalIntervals.addAll(discips[i].getIntervals());
            }
        }
        
        totalIntervals.sort(new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        
        fitnessSum -= maximumCredits*countErrors(state, totalIntervals);
            
        return fitnessSum;
    }
    
    private int countErrors(boolean state[], List<Integer> intervals){
        int errors = 0;
        //Os algoritmos devem considerar que intervals esta ordenado para funcionar
        errors += countNotIncludedObrigatoryDisciplines(state);
        errors += countRepeatedDisciplines(state);
        errors += countRepeatedIntervals(intervals);
        errors += countMoreThanFiveIntervalsSequence(intervals);
        errors += countLessThanTwelveIntervalBetweenDays(intervals);
        return errors;
    }
    
    private int countNotIncludedObrigatoryDisciplines(boolean state[]){
        Discipline[] obrigatories = getObrigatories();
        List<String> tokensNotTaken = new ArrayList<>();
        //Para cada disciplina obrigatória existente crie um marcacador de que ele deve ser retirado
        for(Discipline d: obrigatories)
            if(!tokensNotTaken.contains(d))
                tokensNotTaken.add(d.getName().toUpperCase());
        //Para cada disciplina selecionada em state, remove um marcador se ela for obrigatoria
        for(int i = 0; i < state.length; i++)
            if(state[i])
                if(tokensNotTaken.contains(discips[i].getName().toUpperCase()))
                    tokensNotTaken.remove(discips[i].getName().toUpperCase());
        //Retorna o número de marcadores que sobraram
        return tokensNotTaken.size();
    }

    private int countRepeatedDisciplines(boolean state[]){
        int countRep = 0;
        List<String> repeatedTokens =  new ArrayList<>();
        //Se não existir uma disciplina selecionada entre os marcadores, insira um...
        //Caso exista, então foi encontrado uma disciplina repetida e deve-se aumentar o contador 
        for(int i = 0; i < state.length; i++){
            if(state[i]){
                if(repeatedTokens.contains(discips[i].getDisciplineName().toUpperCase()))
                    countRep++;
                else
                    repeatedTokens.add(discips[i].getDisciplineName().toUpperCase());
            }
        }
        
        return countRep;
    }
    
    private int countRepeatedIntervals(List<Integer> intervals){
        int countRep = 0;
        List<Integer> repeatedTokens = new ArrayList<>();
        //Se não existir o marcador do intervalo em questão, então insira um...
        //Caso exista, então foi encontrado um disciplina com horário sobreposto e deve-se aumentar o contador 
        for(Integer i: intervals)
            if(repeatedTokens.contains(i))
                countRep++;
            else
                repeatedTokens.add(i);
        return countRep++;
    }
    
    private int countMoreThanFiveIntervalsSequence(List<Integer> intervals){
        int ini = 0,prox = 1, overflow = 0;
        
        //Verifica sequência de horarios para identificar se existem mais de 5 disciplinas em sequencia
        for(;prox < intervals.size(); prox++){
            //Se a diferença entre um elemento e o próximo é maior que um, então não estão mais em sequencia
            if(intervals.get(prox)-intervals.get(prox-1) > 1){
                //Se a sequencia for maior que 5, então aumente a contagem
                if(prox - ini > 5)
                    //Quanto mais disciplinas seguidas após 5 pior
                    overflow += prox - ini - 5;                 
                //Reseta inicio da sequencia
                ini = prox;
            }
        }
        
        //Verificação de borda- último caso não se encaixa nas condições anteriores
        prox--;
        if(prox - ini > 5)
            overflow += prox - ini - 4;
               
        return overflow;
    }
    
    private int countLessThanTwelveIntervalBetweenDays(List<Integer> intervals){
        int contSmallDelay = 0;
        for(int i = 1; i < intervals.size(); i++)
            //Caso o intervalo entre a ultima aula de um dia e a primeira do dia seguinte seja menor que 
            //12 horas deve incrementar o contador
            if(intervals.get(i)-intervals.get(i-1) < 12 && intervals.get(i)/24 != intervals.get(i-1)/24)
                contSmallDelay++;
        return contSmallDelay;
    }
    
    private Discipline[] getObrigatories(){
        if(obrigatories == null){
            List<Discipline> ob = new ArrayList<>();
            for(Discipline d: discips)
                if(d.isObrigatory())
                    ob.add(d);

            obrigatories = ob.toArray(new Discipline[ob.size()]);
        }
        return obrigatories;
    }
}
