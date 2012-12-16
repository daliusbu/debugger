/*
 * DebuggerFormalCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.ByteCode;
import interpreter.debugger.DebuggerVirtualMachine;
import java.util.ArrayList;


public class DebuggerFormalCode extends ByteCode {
    
    private final String codeName = "FORMAL";
    private String arg1;
    private Integer arg2;
    private int size = 0;
    private int position = 0;

    public DebuggerFormalCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs) {
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
        this.arg2 = Integer.parseInt(codeArgs.get(1));
    }

    @Override
    public Object execute(VirtualMachine vm) {
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        dvm.enterToSymbolTable(this.getArg1(), this.getArg2(), false);
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
        return this.arg1;
    }
    
    /**
     * Gets argument 2
     * @return argument 2
     */
    public Integer getArg2(){
        return this.arg2;
    }

    @Override
    public void setGoToPosition(Integer position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dumpCode(ArrayList<Integer> stack) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
