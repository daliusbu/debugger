/*
 * DebuggerFunctionCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.ByteCode;
import interpreter.debugger.DebuggerVirtualMachine;
import java.util.ArrayList;

public class DebuggerFunctionCode extends ByteCode {
    
    private final String codeName = "FUNCTION";
    private String arg1;
    private Integer arg2;
    private Integer arg3;
    private int size = 0;
    private int position = 0;

    public DebuggerFunctionCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
        this.arg2 = Integer.parseInt(codeArgs.get(1));
        this.arg3 = Integer.parseInt(codeArgs.get(2));
    }

    @Override
    public Object execute(VirtualMachine vm) {
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        dvm.resetOffset();

        if(dvm.getIsStepInSet()){
            dvm.setIsRunning(false);
            dvm.setPrintAll(true);
            dvm.setCurrentFunction();
        }
        dvm.peekFromEnvironmentStack().startFunction(this.getArg1(), this.getArg2(), this.getArg3());
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
    
    /**
     * Gets argument 3
     * @return argument 3
     */
    public Integer getArg3(){
        return this.arg3;
    }

    @Override
    public void setGoToPosition(Integer position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dumpCode(ArrayList<Integer> stack){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
