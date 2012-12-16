/**
 * ListBreakpoints Class
 */

package interpreter.debugger;

import java.util.ArrayList;

public class ListBreakpoints extends DebugCommand {
    
    private final String command = "list";
    private Integer args = null;
    private String breakpointList;
   
    public ListBreakpoints(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String number){
        this.args = Integer.parseInt(number);
    }
    
    public void setBreakpointList(ArrayList breakpoints){
        String str = "";
        for(int i=0; i<breakpoints.size(); i++){
            str += breakpoints.get(i) + " ";
        }
        this.breakpointList = str;
    }
    
    @Override
    public String toString(){
        return "Breakpoints: " + this.breakpointList;
    }

    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        ArrayList<Integer> list = dvm.getBreakpoinList();
        this.setBreakpointList(list);
        dvm.setPrintOutput(true);
        dvm.setPrintAll(false);
        dvm.setIsRunning(false);
        return dvm;
    }

}


