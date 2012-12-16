/**
 * DumpCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class DumpCode extends ByteCode{
    
    private final String codeName = "DUMP";
    private int size = 0;
    private String arg1;
    private int position = 0;
    
    public DumpCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        vm.setDumpState(this.getDumpState());
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
    
    @Override
    public void setGoToPosition(Integer position){
        throw new UnsupportedOperationException("Not supported.");
    }
    
    /**
     * Gets dump state (ON or OFF)
     * @return dump state
     */
    public boolean getDumpState(){
        boolean isDumpOn = false;
        if("ON".equals(this.getArg1())){
            isDumpOn = true;
        }
        return isDumpOn;
    }
    
    /**
     * This method will not be printed if VirtualMachine.java
     * has if else statement stating so
     */
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName());
    }
}
