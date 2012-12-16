/*
 * DebuggerPopCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.PopCode;
import interpreter.debugger.DebuggerVirtualMachine;


public class DebuggerPopCode extends PopCode {
    
    public DebuggerPopCode(){
        
    }
    
    @Override
    public Object execute(VirtualMachine vm){   
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        int popLevels = this.getArg1AsInteger();
        for(int i=0; i<popLevels; i++){
            dvm.popRunStack();
        }       
        return dvm;
    }
    
}
