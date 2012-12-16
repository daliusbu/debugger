/**
 * ArgsCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class ArgsCode extends ByteCode {
    
    private final String codeName = "ARGS";
    private Integer arg1;
    private int size = 0;
    private int position = 0;
    
    public ArgsCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = Integer.parseInt(codeArgs.get(0));
    }

    @Override
    public Object execute(VirtualMachine vm){
        int argument = getArg1AsInteger();
        vm.setFunctionHasArguments(argument);
        vm.newFrameAt(argument);
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
     * Gets argument one as integer
     * @return argument one
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
