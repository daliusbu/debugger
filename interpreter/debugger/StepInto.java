/*
 * StepInto Class
 */
package interpreter.debugger;

public class StepInto extends DebugCommand {

    private final String command = "stepin";
    private Integer args = null;
    
    public StepInto(){
        
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
        return "Step into is set.";
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        dvm.setIsRunning(true);
        dvm.setIsStepInSet(true);
        dvm.setEnvSize(dvm.getEnvironmentStackSize());
        dvm.setSavedLineNumber(dvm.getcurrentCodeLine());
        return dvm;
    }

}
