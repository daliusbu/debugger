/*
 * DebuggerCallCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.CallCode;
import interpreter.debugger.DebuggerVirtualMachine;


public class DebuggerCallCode extends CallCode {
    
    public DebuggerCallCode(){
        
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        this.callArguments=dvm.getFunctionHasArguments();
        
        dvm.startNewScope();
        if(this.getGoToPosition()!=null){
            dvm.setSavedPC(dvm.getPC());
            dvm.setPC(getGoToPosition()-1);
        }
        
        return dvm;
    }
    
}
