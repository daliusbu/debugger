/**
 * ClearBreakpoint Class
 */

package interpreter.debugger;


public class StepOut extends DebugCommand {
    
    private final String command = "stepout";
    private Integer args = null;
    
    public StepOut(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String number){
        this.args = Integer.parseInt(number);
    }
    
    @Override
    public String toString(){
        return "Step out is set.";
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        dvm.setIsStepOutPending(true);
        // Set Environment Stack size here
        dvm.setEnvSize(dvm.getEnvironmentStackSize());

        dvm.setIsRunning(true);
        // We don't want to print anyting at the moment
        dvm.setPrintAll(false);

        return dvm;
    }

}