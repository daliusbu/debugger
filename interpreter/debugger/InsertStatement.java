/*
 * InsertStatement Class
 */
package interpreter.debugger;

public class InsertStatement extends DebugCommand {

    private final String command = "insert";
    private Integer lineNumber = null;
    
    public InsertStatement(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String number){
        this.lineNumber = Integer.parseInt(number);
    }
    
    /*
    @Override
    public String toString(){
        return "Statement inserted after: ";
    }
    */
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        dvm.setLineToReplace(this.lineNumber);
        dvm.setReturnForInput(true);
        dvm.setPrintOutput(false);
        dvm.setPrintAll(false);
        dvm.setIsRunning(false);
        dvm.setSaved(false);
        dvm.setInputOperation("insert");
        return dvm;
    }

}
