/*
 * ReplaceStatement Class
 */
package interpreter.debugger;

public class ReplaceStatement extends DebugCommand {

    private final String command = "insert";
    private Integer lineNumber = null;
    
    public ReplaceStatement(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    /* When using the argument add 1 to make sure line starts at 1 */
    public void setArgs(String number){
        this.lineNumber= Integer.parseInt(number);
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {

        dvm.setLineToReplace(this.lineNumber);
        dvm.setReturnForInput(true);
        dvm.setPrintOutput(false);
        dvm.setPrintAll(false);
        dvm.setIsRunning(false);
        dvm.setSaved(false);
        dvm.setInputOperation("replace");
        return dvm;
    }

}

