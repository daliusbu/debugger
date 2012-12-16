/**
 * DebuggerEntry Class
 */
package interpreter.debugger;


public class DebuggerEntry {
    
    private String sourceLine;
    private boolean isBreakPointSet;
    
    /**
     * Constructor
     */
    public DebuggerEntry(){
       
    }
    
    /**
     * Sets entry
     * @param sourceLine
     * @param isBreakPointSet 
     */
    public void setEntry(String sourceLine, boolean isBreakPointSet){
        this.sourceLine = sourceLine;
        this.isBreakPointSet = isBreakPointSet;
    }
    
    /**
     * Sets breakpoint to the line
     * @param breakpoint 
     */
    public void setBreakpointTo(boolean breakpoint){
        if(breakpoint==true){
            this.setBreakPoint();
        }
        else{
            this.removeBreakPoint();
        }
    }
    
    /**
     * Sets breakpoint to the line
     */
    private void setBreakPoint(){
        this.isBreakPointSet = true;
    }
    
    /**
     * Removes breakpoint
     */
    private void removeBreakPoint(){
        this.isBreakPointSet = false;
    }   
    
    /**
     * Returns source line
     * @return source line
     */
    public String getSourceLine(){
        return this.sourceLine;
    }
    
    /**
     * Checks if a breakpoint is set
     * @return boolean
     */
    public boolean isBreakPointSet(){
        return isBreakPointSet;
    }
    
    @Override
    public String toString(){
        return this.sourceLine;
    }
    
}
