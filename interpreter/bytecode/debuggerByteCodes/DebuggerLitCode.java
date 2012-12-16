/*
 * DebuggerLitCode
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.LitCode;
import interpreter.debugger.DebuggerVirtualMachine;


public class DebuggerLitCode extends LitCode {
    
    public DebuggerLitCode(){
        
    }
    
    
    @Override
    public Object execute(VirtualMachine vm){
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        dvm.pushRunStack(this.getArg1AsInteger());
        
        /* If there are 2 arguments */
        if(this.size==2){
            dvm.setOffset();
            Integer offset = dvm.getOffset();
            if(dvm.getWatchingSet()){
                String var = this.getArg2();
                Integer line = dvm.getcurrentCodeLine();
                if(dvm.isInWatchStack(var)){
                    Integer value = dvm.getFromWatchStack(var);
                    // Check if we are watching this value on this line
                    if(line==value){
                        dvm.peekFromEnvironmentStack().enter(this.getArg2(), offset, true);
                    }
                    // We are not watching on this line
                    else{
                        dvm.peekFromEnvironmentStack().enter(this.getArg2(), offset, false);
                    }
                }
                else{
                    // We are not watching this variable
                    dvm.peekFromEnvironmentStack().enter(this.getArg2(), offset, false);
                }
            }
            else{
                dvm.peekFromEnvironmentStack().enter(this.getArg2(), offset,false);
            }
        }

        return dvm;
    }
}
