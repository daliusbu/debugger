/*
 * ContinueExecution Class
 */
package interpreter.debugger;

public class ContinueExecution extends DebugCommand {

    private final String command = "cont";
    private Integer args = null;
    
    public ContinueExecution(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public String toString(){
        return "Continuing Execution...";
    }

    @Override
    public void setArgs(String number) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
    	/* Setting true to continue running VM */
        dvm.setIsRunning(true);
        dvm.setPrintOutput(false);
        return dvm;
    }
    
}
