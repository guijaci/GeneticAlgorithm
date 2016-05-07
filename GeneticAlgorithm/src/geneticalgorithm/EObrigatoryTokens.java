package geneticalgorithm;

public enum EObrigatoryTokens {
    A("A"), B("B"), C("C"), D("D"), E("E"), F("F");
    
    private String disciplineName;
    
    EObrigatoryTokens(String disciplineName){
        this.disciplineName = disciplineName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }
}
