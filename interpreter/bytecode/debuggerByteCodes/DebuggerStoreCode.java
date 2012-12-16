/*
 * DebuggerStoreCode Class
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.StoreCode;
import interpreter.debugger.DebuggerVirtualMachine;


public class DebuggerStoreCode extends StoreCode {
    
    public DebuggerStoreCode(){
        
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        
        if(dvm.getWatchingSet()){
            String key = this.getArg2();
            if(dvm.isInWatchStack(key)){
                //Integer lineNumber = dvm.getFromWatchStack(key);
                Integer offset = dvm.getValue(key);
                Integer beforeStore = dvm.getValueFromStack(offset);
                this.stored = dvm.store(this.getArg1AsInteger());
                if(this.stored!=beforeStore){
                    // It means value changed
                    dvm.setWatchedValueChanged(true);
                    String output = "Watched variable changed "+key+":"+this.stored+"\n";
                    if(dvm.isWatchOutputEmpty()){
                        // If empty - create string
                        dvm.setWatchForOutput(output);
                    }
                    else{
                        // If something is already there - append
                        dvm.appentWatchForOutput(output);
                    }
                }
                else{
                    dvm.setWatchedValueChanged(false);
                }
            }
        }
        else{
            this.stored = dvm.store(this.getArg1AsInteger());
        }

        return dvm;
    }
    

}
