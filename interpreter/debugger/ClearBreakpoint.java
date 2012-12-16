/**
 * ClearBreakpoint Class
 */

package interpreter.debugger;


public class ClearBreakpoint extends DebugCommand {
    
    private final String command = "clear";
    private Integer args = null;
    
    public ClearBreakpoint(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String number){
        this.args = Integer.parseInt(number);
    }
    
    public Integer getArgs(){
        return this.args;
    }
    
    @Override
    public String toString(){
        return "Breakpoint cleared: " + this.args;
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
    	/* Let's clear all errors first */
    	dvm.clearError();
        boolean rightLine = dvm.getLineNumbers().contains(this.args);
        /* Let VM know that we can set the breakpoint if it's true */
        dvm.setCanSetBreakpoint(rightLine);
        /* Allow to print output to the screen */        
        if(rightLine){
            dvm.setPrintAll(true);
            dvm.setPrintOutput(true);
            dvm.updateEntryBreakpoint(this.args, false);
            dvm.removeBreakPointFromTheList(this.getArgs());
        }
        else{
            dvm.setPrintOutput(false);
            dvm.setError("Breakpoint was not set on this line.");  
        }
        dvm.setIsRunning(false);
        return dvm;
    }

}