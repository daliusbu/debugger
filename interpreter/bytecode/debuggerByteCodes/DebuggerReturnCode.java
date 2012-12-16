/*
 * DebuggerReturnCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.ReturnCode;
import interpreter.debugger.DebuggerVirtualMachine;


public class DebuggerReturnCode extends ReturnCode {
    
    public DebuggerReturnCode(){
        
    }
    
    @Override
    public Object execute(VirtualMachine vm){       
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        dvm.popFrame();
        dvm.resetOffset();

        dvm.leaveScope();


        dvm.setPC(dvm.getSavedPC());
        /*
         * Set isRunning to false only if stepout is pending and envSize drops below
         * current envSize
         */
        if(dvm.getIsStepOutPending()&&(dvm.getEnvironmentStackSize()<dvm.getEnvSize())){
            dvm.setIsRunning(false);
        }
        else{
            dvm.setIsRunning(true);
        }
        return dvm;
    }
    
}
