/*
 * StepOver Class
 */
package interpreter.debugger;

public class StepOver extends DebugCommand {

    private final String command = "stepover";
    private Integer args = null;
    
    public StepOver(){
        
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
        return "Step over is set.";
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        dvm.setIsRunning(true);
        dvm.setIsStepOverSet(true);
        dvm.setSavedLineNumber(dvm.getcurrentCodeLine());

        return dvm;
    }

}
