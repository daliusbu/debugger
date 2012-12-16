/**
 * ReadCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadCode extends ByteCode {
    
    protected final String codeName = "READ";
    protected int size = 0;
    protected int position = 0;
    
    public ReadCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){

    }
    
    @Override
    public Object execute(VirtualMachine vm){
        Scanner stdin = new Scanner(System.in);
        int input;
        System.out.print("Enter an integer: ");
        input = stdin.nextInt();
        vm.pushRunStack(input);
        
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
