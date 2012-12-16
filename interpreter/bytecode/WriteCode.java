/**
 * WriteCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class WriteCode extends ByteCode {
    
    private int size = 0;
    private final String codeName = "WRITE";
    private int position = 0;
    private String writeValue;

    public WriteCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){

    }
    
    @Override
    public Object execute(VirtualMachine vm){
        int value = vm.peekRunStack();
        this.writeValue = value + "";
        System.out.println(this.toString());
        return vm;
    }
    
    @Override
    public String toString(){
        return this.writeValue;
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
        return "";
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
