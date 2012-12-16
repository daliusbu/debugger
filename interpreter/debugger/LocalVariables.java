/**
 * LocalVariables Class
 */

package interpreter.debugger;

import java.util.ArrayDeque;
import java.util.Set;


public class LocalVariables extends DebugCommand {
    
    private final String command = "lvars";
    private String localVariables;
    
    public LocalVariables(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String number) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Sets local variables
     * @param str 
     */
    public void setLocalVariables(String str){
        this.localVariables = str;
    }
    
    /**
     * Returns local variables
     * @return 
     */
    public String getLocalVariables(){
        return this.localVariables;
    }
    
    @Override
    public String toString(){
        String str = "Local variables: \n";
        str += this.getLocalVariables();
        return str;
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        Set localNames = dvm.peekFromEnvironmentStack().getKeys();
        ArrayDeque<Integer> vars = dvm.getTopFrameValues();
        String str = dvm.getLocalVariables(localNames, vars);
        this.setLocalVariables(str);
        dvm.setPrintOutput(true);
        dvm.setIsRunning(false);
        dvm.setPrintAll(false);
        return dvm;
    }

}

