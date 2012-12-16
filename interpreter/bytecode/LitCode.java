/**
 * LitCode Class
 */

package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class LitCode extends ByteCode {

    protected final String codeName = "LIT";
    protected Integer arg1;
    protected String arg2;
    protected int size = 0;
    protected int position = 0;
    
    public LitCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        if(this.size==1){
            this.arg1 = Integer.parseInt(codeArgs.get(0));
        }
        else if(this.size==2){
            this.arg1 = Integer.parseInt(codeArgs.get(0));
            this.arg2 = codeArgs.get(1);
        }
    }
    
    @Override
    public Object execute(VirtualMachine vm){ 
        int temp = vm.pushRunStack(this.getArg1AsInteger());

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
     * Gets argument 2
     * @return argument 2
     */
    public String getArg2(){
        return this.arg2;
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
        System.out.print(this.getArg1()+" ");
        if(this.arg2!=null){
            System.out.print(this.getArg2()+" ");
        }
        System.out.print("\t");
        if(this.arg2!=null){
            System.out.print("int " + this.getArg2()+"\n");
        }
        else{
            System.out.print("\n");
        }
    }
}
