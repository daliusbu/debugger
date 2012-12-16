/*
 * DebuggerReadCode Class
 */
package interpreter.bytecode.debuggerByteCodes;

import interpreter.VirtualMachine;
import interpreter.bytecode.ReadCode;
import interpreter.debugger.DebuggerVirtualMachine;
import java.util.Scanner;

public class DebuggerReadCode extends ReadCode {
    
    
    public DebuggerReadCode(){
        
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        DebuggerVirtualMachine dvm = (DebuggerVirtualMachine)vm;
        dvm.setToReadLine(true);

        Scanner stdin = new Scanner(System.in);
        int input;
        System.out.print("Enter an integer: ");
        input = stdin.nextInt();
        dvm.pushRunStack(input);
        
        if(dvm.isBreakpointSet()){
            dvm.setIsRunning(false);
            dvm.setPrintOutput(true);
            dvm.setPrintAll(true);
            dvm.setReadingCommand(true);
        }
        else{
            dvm.setIsRunning(true);
            dvm.setPrintAll(false);
        }

        return dvm;
    }
    
}
