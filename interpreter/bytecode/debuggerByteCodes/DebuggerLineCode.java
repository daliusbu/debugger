/*
 * DebuggerLineCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.ByteCode;
import interpreter.debugger.DebuggerVirtualMachine;
import java.util.ArrayList;


public class DebuggerLineCode extends ByteCode {
    
    private final String codeName = "LINE";
    private Integer arg1;
    private int size = 0;
    private int position = 0;
    
    public DebuggerLineCode(){
        
    }

    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = Integer.parseInt(codeArgs.get(0));
    }

    @Override
    public Object execute(VirtualMachine vm) {
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
     
        dvm.peekFromEnvironmentStack().setLineNumber(this.getArg1AsInteger());
        dvm.setCurrentCodeLine(this.getArg1AsInteger());
        
        if(dvm.isBreakpointSetOnLine(this.getArg1AsInteger())){

            dvm.setIsRunning(false);
            dvm.setPrintOutput(true);
            dvm.setPrintAll(true);
        }
        else{
            dvm.setIsRunning(true);
        }
        return dvm;
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
    public int getNumberOfAgrs(){
        return this.size;
    }

    @Override
    public String getCodeName(){
        return this.codeName;
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
    public void setGoToPosition(Integer position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName()+" ");
        System.out.print(this.getArg1()+"\n");
    }
    
}
