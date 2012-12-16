/*
 * SaveStatement Class
 */
package interpreter.debugger;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveStatement extends DebugCommand {

    private final String command = "save";
    private String filename = null;
    private String sourceCode;
    
    public SaveStatement(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String filename){
        this.filename = filename;
    }
    
    public String getSourceCode(){
        return this.sourceCode;
    }

    @Override
    public String toString(){
        return "Saved to " + this.filename;
    }
    
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        this.sourceCode = dvm.generateSourceCode();
        try {
            dvm.writeToFile(this.sourceCode, filename);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        dvm.setPrintOutput(true);
        dvm.setPrintAll(false);
        dvm.setIsRunning(false);
        dvm.setSaved(true);
        return dvm;
    }

}
