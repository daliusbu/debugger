/*
 * WatchVariable Class
 */
package interpreter.debugger;

public class WatchVariable extends DebugCommand {

    private final String command = "watch";
    private Integer varLineNumber = null;
    private String varName = null;
    
    public WatchVariable(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String argument){
        if(this.varLineNumber !=null){
            // Reset variables first
            this.varName = null;
            this.varLineNumber = null;
            // Must be the name here
            this.varName = argument;
        }
        else{
            if(this.varName!=null){
                this.varLineNumber = Integer.parseInt(argument);
            }
            else{
                this.varName = argument;
            }
        }
    }
    
    public Integer getArgs(){
        return this.varLineNumber;
    }
    
    public String getVarName(){
        return this.varName;
    }
    
    @Override
    public String toString(){
        return "Watching: " + this.varName + " on line " + this.varLineNumber;
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        dvm.watchVariable(this.varName, this.varLineNumber);
        dvm.addToWatchStack(varName,varLineNumber);
        dvm.setWatchingSet(true);
        dvm.setIsRunning(false);
        dvm.setPrintOutput(true);
        dvm.setPrintAll(false);

        return dvm;
    }

}
