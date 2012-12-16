/**
 * ReturnCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ReturnCode extends ByteCode {

    protected final String codeName = "RETURN";
    protected String arg1;
    protected int size = 0;
    protected int position = 0;
    protected String step;
    
    public ReturnCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        if(this.size==1){
            this.arg1 = codeArgs.get(0);
            this.parseArg();
        }
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        vm.popFrame();
        vm.setPC(vm.getSavedPC());
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
        if(this.size==1){
            if(Pattern.matches("[a-zA-Z]+<<[0-9]+>>", this.arg1)){
                String regex1 = ">>";
                String regex2 = "<<";
                // Getting a step
                this.step = this.arg1.split(regex1)[0].split(regex2)[1];
            }
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
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        int lastIndex = stack.size() - 1;
        int stackTop = stack.get(lastIndex);
        System.out.print(this.getCodeName()+" ");
        if(this.arg1!=null){
            System.out.print(this.getArg1()+" ");
            System.out.print("\t");
            System.out.print("exit "+this.getArg1()+": "+stackTop+"\n");
        }
        else{
            System.out.print("\n");
        }
    }
}
