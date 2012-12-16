/**
 * SetBreakpoint Class
 */

package interpreter.debugger;

public class SetBreakpoint extends DebugCommand {
    
    private final String command = "set";
    private Integer args = null;
    
    public SetBreakpoint(){
        
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
        return "Breakpoint set: " + this.args;
    }

    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        /* Let's clear all errors first */
        dvm.clearError();
        /* Check if lineNumbers array contains this argument which is the line
         * number on which we want to set a breakpoint
         * If line number is in the array it means we can set the breakpoint
         */
        boolean rightLine = dvm.getLineNumbers().contains(this.args);
        /* Let VM know that we can set the breakpoint if it's true */
        dvm.setCanSetBreakpoint(rightLine);
        /* Allow to print output to the screen */     
        if(rightLine){
            dvm.setPrintOutput(true);
            dvm.setPrintAll(true); 
            dvm.updateEntryBreakpoint(this.args, true);
            dvm.addBreakpoint(this.args);
        }
        else{
            dvm.setPrintOutput(false);
            dvm.setCanSetBreakpoint(false);
            dvm.setError("Breakpoint can not be set on this line");  
        }
        dvm.setIsRunning(false);
        return dvm;
    }

}
