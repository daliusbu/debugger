/*
 * ChangeLocalVariable Class
 */
package interpreter.debugger;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Set;

public class ChangeLocalVariable extends DebugCommand {

    private final String command = "changevar";
    private Integer variable = null;
    private String varName = null;
    private Integer changeFrom;
    
    public ChangeLocalVariable(){
        
    }
    
    @Override
    public String getCommandName() {
        return this.command;
    }
    
    @Override
    public void setArgs(String argument){
        if(this.variable !=null){
            // Reset variables first
            this.varName = null;
            this.variable = null;
            // Must be the name here
            this.varName = argument;
        }
        else{
            if(this.varName!=null){
                this.variable = Integer.parseInt(argument);
            }
            else{
                this.varName = argument;
            }
        }
    }
    
    public Integer getArgs(){
        return this.variable;
    }
    
    public String getVarName(){
        return this.varName;
    }
    
    @Override
    public String toString(){
        return "Variable " + this.varName + " changed from " + this.changeFrom + " to " + this.variable;
    }
    
    @Override
    public Object execute(DebuggerVirtualMachine dvm) {
        
        Set localNames = dvm.peekFromEnvironmentStack().getKeys();
        ArrayDeque<Integer> vars = dvm.getTopFrameValues();
        HashMap<String,Integer> locals = dvm.getLocalVariableMap(localNames, vars);
        if(locals.containsKey(this.varName)){
            this.changeFrom = locals.get(this.varName);
            dvm.enterToSymbolTable(this.varName , this.variable, false);
            int offset = dvm.getOffset();
            dvm.replace(this.changeFrom, this.variable, offset);
        }

        return dvm;
    }

}
