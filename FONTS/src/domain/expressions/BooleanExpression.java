package src.domain.expressions;
public class BooleanExpression{
    private String expression;
    private ExpressionTreeNode root;

    private BooleanExpression(String expr,ExpressionTreeNode rot){ //creadora privada per només crear expressions vàlides
        expression=expr.trim();
        root=rot;
    }

    static public BooleanExpression createBooleanExpression(String expr) throws Exception{ //creadora que només crea expressions válides
        ExpressionTreeNode rot=new ExpressionTreeNode();
        buildTree(rot,expr);
        return new BooleanExpression(expr,rot);
    }

    public String getExpression(){
        return expression;
    }

    public ExpressionTreeNode getRoot(){
        return root;
    }

    static private void buildTree(ExpressionTreeNode rot,String s)throws Exception{ //funció recursiva encarregada de crear l'arbre mentre comprova que la expressió és correcte
        while(parentesis(s)){//treu els paréntesis que engloben tota la expressió
            s=s.substring(1,s.length()-1);
        }
        if(s.length()==0) throw new Exception("Incorrect expression: missing operand");
        int index =findLastOperation(s);//busca la última operació que caldria realitzar de la expressió, també fa control d'errors
        if(index==-2){//indica que no queda cap operador
            if(missingOperator(s)) throw new Exception("Incorrect expression: missing operator"); //si la expressió no té operadors i no és un operand válid és incorrecte
            rot.setValue(s);
            return;
        }
        else{
            //subdividim la expressió en dues (o una si la operació es un "!") a partir de la ultima operació a realitzar que hem trobat abans
            String val=String.valueOf(s.charAt(index));
            rot.setValue(val);
            String s1= s.substring(0,index).trim();
            String s2= s.substring(index+1,s.length()).trim();
            if(!val.equals("!")){
                 ExpressionTreeNode l = new ExpressionTreeNode();
                buildTree(l, s1);
                 rot.setLeft(l);
                 ExpressionTreeNode r = new ExpressionTreeNode();
                buildTree(r, s2);
                 rot.setRight(r);
                 return;
            }
            else{
                 ExpressionTreeNode r = new ExpressionTreeNode();
                 buildTree(r, s2);
                 rot.setRight(r);
                 if(s1.length()>0) throw new Exception("Incorrect expression: missing operator");
                return;
            }
            
            
            
        }
    }

    static private boolean missingOperator(String s){ //comprova que s sigui un operand correcte
        char[] x = s.trim().toCharArray();
        if(x[0]=='{'){
            int count=0;
            boolean estaBuit=true;
            for(char c: x){
                if (count>=2) return true;
                if(c=='}' |c == '{' ) {
                    count++;
                }
                else if( c!=' '){
                    estaBuit=false;
                }
                
            }
            if(estaBuit) return true;
        }
        else if(x[0]=='"'){
            int count = 0;
            boolean estaBuit=true;
            for(char c: x){
                if(count>=2) return true;
                if(c=='"') count++;
                else if( c!=' '){
                    estaBuit=false;
                }
            }
            if(estaBuit) return true;
        }
        else{
            for(char c: x){
                if(c==' ') return true;
            }
        }
        return false;
    }

    static private int findLastOperation(String s) throws Exception{ //troba la última operació a realitzar en s
        char[] x = s.toCharArray();
        int[] ranking = new int[s.length()];
        int rank = 0;
        int index = 0;
        boolean cntrCorxetes=false;
        boolean cntrCometes=false;
        int ultimParentesi=0;
        for(char c : x) {
            if(!cntrCorxetes && !cntrCometes && (c == '&' || c == '|' || c == '!')) ranking[index] = rank;
            else if(!cntrCorxetes && !cntrCometes && c =='('){
                    ranking[index] = -1;
                    rank++;
            }
            else if(!cntrCorxetes && !cntrCometes && c==')'){
                ranking[index] = -1;
                rank--;
            }
            else if(!cntrCorxetes && !cntrCometes && c=='{'){
                ranking[index] = -1;
                cntrCorxetes=true;
            }
            else if(c=='}' && !cntrCometes){
                ranking[index] = -1;
                if(cntrCorxetes) cntrCorxetes=false;
                else throw new Exception("Incorrect expression: there is a problem with brakets");//la expressio no es correcte
            }
            else if(c=='"' && !cntrCorxetes){
                ranking[index] = -1;
                cntrCometes=!cntrCometes;
            }
            else ranking[index] = -1;
            
            if(ultimParentesi==index-1 && (c==')'))throw new Exception("Incorrect expression: there is a problem with parentheses"); //la expressio es incorrecte
            if(ultimParentesi==index-1 && (c==' ')) ultimParentesi++;
            
            if(rank<0) throw new Exception("Incorrect expression: there is a problem with parentheses");//la expressió no és correcte
            index++;
        }
        if(rank!=0) throw new Exception("Incorrect expression: there is a problem with parentheses");//la expressió no és correcte
        if(cntrCometes) throw new Exception("Incorrect expression: there is a problem with quotation marks");
        if(cntrCorxetes)throw new Exception("Incorrect expression: there is a problem with brakets"); //la expressio no es correcte
        int min = s.length() + 1;
        int indexMin = -2;
        for (int i = 0; i < ranking.length; ++i) {
            if (ranking[i] != -1)
                if (ranking[i] < min) {
                    min = ranking[i];
                    indexMin = i;
                }
                else if(ranking[i] == min && hasLowerPreference(x[i], x[indexMin])) {//quina es la preferencia?
                    min = ranking[i];
                    indexMin = i;
                }
            }
        return indexMin;//si torna -2 vol dir que no queden operacions 
    }

    static private Boolean hasLowerPreference(char iact, char imin) { //compara la preferencia entre dos operadors
        if (imin == '|') return false;
        if (iact == '|') return true;
        if (imin == '&') return false;
        if (iact == '&') return true;
        else return false;
    }

    static private boolean parentesis(String s){ //comprova si hi ha un parentesis que conté la expressió sencera
        int count=0;
        if(s.length()>1 && s.charAt(0)=='(' && s.charAt(s.length()-1)==')'){
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)=='(') count++;
                else if(s.charAt(i)==')') count--;

                if(count==0 && i!=s.length()-1) return false;

            }
            return true;
        }
        else return false;
    }
}
