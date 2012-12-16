/**
 * PopCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class PopCode extends ByteCode {
    
    protected final String codeName = "POP";
    protected Integer arg1;
    protected int size = 0;
    protected int position = 0;
    
    public PopCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = Integer.parseInt(codeArgs.get(0));
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        int popLevels = this.getArg1AsInteger();
        for(int i=0; i<popLevels; i++){
            vm.popRunStack();
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
        return this.arg1+"";
    }
    
    /**
     * Gets argument 1 as integer
     * @return argument 1
     */
    public Integer getArg1AsInteger(){
        return this.arg1;
    }
    
    @Override
    public void setGoToPosition(Integer position){
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName()+" ");
        System.out.print(this.getArg1()+"\n");
    }
}
