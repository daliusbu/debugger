/**
 * HaltCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class HaltCode extends ByteCode{
    
    private final String codeName = "HALT";
    private int size = 0;
    private int position = 0;
    
    public HaltCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){

    }
    
    @Override
    public Object execute(VirtualMachine vm){
        vm.isRunningToFalse();
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
    public String getArg1() {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void setGoToPosition(Integer position){
        throw new UnsupportedOperationException("Not supported.");
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName()+"\n");
    }
}
