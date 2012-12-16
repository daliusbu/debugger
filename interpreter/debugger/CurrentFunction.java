/**
 * CurrentFunction Class
 */

package interpreter.debugger;


public class CurrentFunction extends DebugCommand {
    
    private final String command = "funct";
    private Integer args = null;
    
    public CurrentFunction(){
        
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
        return "Current function: ";
    }

    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        Integer start   = dvm.peekFromEnvironmentStack().getStartLine();
        Integer end     = dvm.peekFromEnvironmentStack().getEndLine();
        Integer current = dvm.peekFromEnvironmentStack().getCurrentLine();
        dvm.setCurrentFunction(start, end, current);
        dvm.setPrintAll(true);
        dvm.setIsRunning(false);
        
        return dvm;
    }

}
