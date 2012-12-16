/**
 * ByteCode Abstract Class
 */
package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public abstract class ByteCode {
    
    /**
     * Initializes the Byte Code
     * @param nextToken 
     */
    public abstract void init(ArrayList<String> codeArgs);
    
    /**
     * Executes the code
     * @param obj
     * @return Object
     */
    public abstract Object execute(VirtualMachine vm);
    
    /**
     * Sets code's position in the program
     * @param position 
     */
    public abstract void setPosition(int position);
    
    /**
     * Gets code's position
     * @return int
     */
    public abstract int getPosition();
    
    /**
     * Returns number of arguments Byte Code has
     * @return int
     */
    public abstract int getNumberOfAgrs();
    
    /**
     * Returns the name of the code. Ex.: LABEL
     * @return String
     */
    public abstract String getCodeName();

    /**
     * Used in all ByteCode classes as well as Program class to get the code's
     * argument
     * @return String
     */
    public abstract String getArg1();

    /**
     * Sets the position byte code has to jump to
     * @param position
     */
    public abstract void setGoToPosition(Integer position);
    
    /**
     * Dumps byte code
     */
    public abstract void dumpCode(ArrayList<Integer> stack);

    
}
