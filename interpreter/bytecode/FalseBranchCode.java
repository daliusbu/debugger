/**
 * FalseBranchCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FalseBranchCode extends ByteCode {

    private final String codeName = "FALSEBRANCH";
    private String arg1;
    private int size = 0;
    private int position = 0;
    private String label;
    private String step;
    private Integer goToPosition;
    
    public FalseBranchCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
        this.parseArg();
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        int value = vm.popRunStack();
        if(value==0){
            vm.setPC(getGoToPosition()-1);
        }
        return vm;
    }
    
    @Override
    public void setPosition(int position){
        this.position = position;
    }
    
    @Override
    public int getPosition(){
        return this.position;
    }
    
    @Override
    public String getCodeName(){
        return this.codeName;
    }
    
    @Override
    public int getNumberOfAgrs(){
        return this.size;
    }
    
    @Override
    public String getArg1(){
        return this.arg1;
    }
    
    /**
     * parses argument
     */
    private void parseArg(){
        if(Pattern.matches("[a-zA-Z]+<<[0-9]+>>", this.arg1)){
            String regex1 = ">>";
            String regex2 = "<<";
            // Getting a step
            this.step = this.arg1.split(regex1)[0].split(regex2)[1];
            // Getting a label
            this.label = this.arg1.split(regex2)[0];
        }
    }

    public String getLabel(){
        return this.label;
    }
    
    /**
     * Gets the location the byte code has to go to
     * @return step
     */
    public String getStep(){
        return this.step;
    }
    
    @Override
    public void setGoToPosition(Integer position){
        this.goToPosition = position;
    }
    
    /**
     * Gets the position byte code has to jump to
     * @return position
     */
    public Integer getGoToPosition(){
        return this.goToPosition;
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName()+" ");
        System.out.print(this.getArg1()+"\n");
    }
}
