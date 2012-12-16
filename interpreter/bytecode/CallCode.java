/**
 * CallCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CallCode extends ByteCode{

    protected final String codeName = "CALL";
    protected String arg1;
    protected int size = 0;
    protected int position = 0;
    protected String functionName;
    protected String step;
    protected Integer goToPosition;
    protected int callArguments;
    
    public CallCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
        this.parseArg();
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        this.callArguments=vm.getFunctionHasArguments();
        if(getGoToPosition()!=null){
            vm.setSavedPC(vm.getPC());
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
     * Parses the argument
     */
    protected void parseArg(){
        if(Pattern.matches("[a-zA-Z]+<<[0-9]+>>", this.arg1)){
            String regex1 = ">>";
            String regex2 = "<<";
            // Getting a step
            this.step = this.arg1.split(regex1)[0].split(regex2)[1];
            // Getting a label
            this.functionName = this.arg1.split(regex2)[0];
        }
        else if(Pattern.matches("[a-zA-Z]+<<[[0-9]+,[0-9]]+>>", this.arg1)){
            String regex1 = ">>";
            String regex2 = "<<";
            // Getting a step
            this.step = this.arg1.split(regex1)[0].split(regex2)[1];
            // Getting a label
            this.functionName = this.arg1.split(regex2)[0];
        }
        else{
            this.functionName = this.arg1;
        }
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
     * Gets the position byte code has to jump
     * @return position
     */
    public Integer getGoToPosition(){
        return this.goToPosition;
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        int lastIndex = stack.size() - 1;
        int args = this.callArguments;
        System.out.print(this.getCodeName()+" ");
        System.out.print(this.getArg1()+" ");
        System.out.print("\t");
        System.out.print(this.functionName+"(");
            if(args!=0){
                for(int i=lastIndex-args+1; i<=lastIndex; i++){
                    System.out.print(stack.get(i));
                    if(i<lastIndex){
                        System.out.print(",");
                    }
                }
            }
        System.out.print(")\n");
    }
    
}
