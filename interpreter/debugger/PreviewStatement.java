/*
 * PreviewStatement Class
 */
package interpreter.debugger;

public class PreviewStatement extends DebugCommand {

    private final String command = "preview";
    private Integer args = null;
    private String sourceCode;
    
    public PreviewStatement(){
        
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
        return this.sourceCode;
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        this.sourceCode = dvm.generateSourceCode();
        dvm.setPrintOutput(true);
        dvm.setPrintAll(false);
        dvm.setIsRunning(false);
        return dvm;
    }

}
