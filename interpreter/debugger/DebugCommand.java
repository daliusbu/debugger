/**
 * DebugCommand Class
 */

package interpreter.debugger;

public abstract class DebugCommand {
    
    /**
     * Returns command name
     * @return command name
     */
    public abstract String getCommandName();
    
    /**
     * Sets arguments
     * @param number 
     */
    public abstract void setArgs(String argument);

    /**
     * Executes the command
     * @param dvm
     * @return DebuggerVirtualMachine
     */
    public abstract Object execute(DebuggerVirtualMachine dvm);
    
}
