/**
 * HaltExecution Class
 */

package interpreter.debugger;


public class HaltExecution extends DebugCommand {
    
    private final String command = "halt";
    
    public HaltExecution(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public String toString(){
        return "*******  Execution Halted  *******";
    }

    @Override
    public void setArgs(String number) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        if(dvm.getSaved()){
            /* Let's clear all errors first */
            dvm.clearError();      

            dvm.setPrintOutput(false);
            /* Make sure to let VM know we want to halt */
            dvm.setContinueExecuting(false);

            /* Will output message as an error -no need extra coding here */
            dvm.setError(this.toString());  

            dvm.setIsRunning(false);
        }
        else{
            // Ask to save to file
            dvm.setReturnForInput(true);
            dvm.setInputOperation("save");
        }
        return dvm;
    }

}