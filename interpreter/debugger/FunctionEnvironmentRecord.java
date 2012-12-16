/**
 * FunctionEnvironmentRecord Class
 */

package interpreter.debugger;

import java.util.ArrayDeque;
import java.util.Set;


public class FunctionEnvironmentRecord {
    
    SymbolTable table;
    String functionName;
    int startLine;
    int endLine;
    int currentLine;
    
    /**
     * Constructor
     */
    public FunctionEnvironmentRecord(){
        this.table = new SymbolTable();
        this.functionName = null;
        this.startLine = -1;
        this.endLine = -1;
        this.currentLine = -1;
    }
    
    /**
     * Wrapper method for table's watchVariable method
     * @param name
     * @param line 
     */
    public void watchVariable(String name, Integer line){
        this.table.watchVariable(name, line);
    }
    
    /**
     * Gets the name of the variable we are watching - wrapper method
     * @return string
     */
    public String getWatchingName(){
        return this.table.getWatchingName();
    }
    
    /**
     * Gets the line of the variable we are watching - wrapper method
     * @return line number
     */
    public Integer getWatchingLine(){
        return this.table.getWatchingLine();
    }
    
    /**
     * Set Function Name
     * @param name 
     */
    public void setName(String name){
        this.functionName = name;
    }
    
    /**
     * Get Function Name
     * @return Function Name
     */
    public String getName(){
        return this.functionName;
    }
    
    /**
     * Set Function Start Line number
     * @param lineNumber 
     */
    public void setStartLine(int lineNumber){
        this.startLine = lineNumber;
    }

    /**
     * Get Function Start Line number
     * @return Start Line number
     */
    public int getStartLine(){
        return this.startLine;
    }

    /**
     * Set Function End Line Number
     * @param lineNumber 
     */
    public void setEndLine(int lineNumber){
        this.endLine = lineNumber;
    }

    /**
     * Get Function End Line Number
     * @return End Line number
     */
    public int getEndLine(){
        return this.endLine;
    }
    
    /**
     * Set current line being processed by the VM
     * @param lineNumber 
     */
    public void setCurrentLine(int lineNumber){
        this.currentLine = lineNumber;
    }

    /**
     * Get current line being processed by the VM
     * @return Current Line
     */
    public int getCurrentLine(){
        return this.currentLine;
    }
    
    /**
     * Wrapper method for Table beginScope() method 
     */
    public void beginScope(){
        this.table.beginScope();
    }
    
    /**
     * Function is entering an environment
     * @param functionName
     * @param startLine
     * @param endLine 
     */
    public void startFunction(String functionName, int startLine, int endLine){
        this.functionName = functionName;
        this.startLine = startLine;
        this.endLine = endLine;
    }
    
    /**
     * Line number
     * @param lineNumber 
     */
    public void setLineNumber(int lineNumber){
        this.currentLine = lineNumber;
    }
    
    /**
     * Enter a <key,value> into the Table
     * @param key
     * @param value 
     */
    public void enter(String key, int value, boolean watch){
        this.table.put(key,value, watch);
    }
    
    /**
     * Checks if watched value changed - wrapper method
     * @param str
     * @param var
     * @return boolean
     */
    public boolean watchedValueChanged(String str, Integer var){
        return this.table.watchedValueChanged(str, var);
    }
    
    /**
     * Wrapper method for Table remove() method
     * @param numberOfVariables 
     */
    public void pop(int numberOfVariables){
        this.table.remove(numberOfVariables);
    }
    
    /**
     * Gets keys fromt he table
     * @return Set
     */
    public Set getKeys(){
        return this.table.keys();
    }
    
    /**
     * peekTop
     * @return 
     */
    public String peekTop(){
        return this.table.peekTop();
    }
    
    /**
     * Gets top value
     * @return 
     */
    public String getTop(){
        return this.table.getTop();
    }
    
    /**
     * Wrapper method for Table get() method
     * Gets the object associated with the specified symbol in the SymbolTable
     * @param str
     * @return integer value
     */
    public int get(String str){
        return this.table.get(str);
    }
    
    /**
     * Get value from the table - wrapper method
     * @return 
     */
    public String getVariables(){
        String str;
        str =  table.getVariablesAsString();        
        return str;
    }
    
    /**
     * Dumps Environment Record
     */
    public void dump(){
        System.out.print("(");
        System.out.print(this.table);
        System.out.print(",");
        if(this.functionName!=null){
            System.out.print(this.functionName);
        }
        else{
            System.out.print("-");
        }
        System.out.print(",");
        if(this.startLine!=-1){
            System.out.print(this.startLine);
        }
        else{
            System.out.print("-");
        }
        System.out.print(",");
        if(this.endLine!=-1){
            System.out.print(this.endLine);
        }
        else{
            System.out.print("-");
        }
        System.out.print(",");
        if(this.currentLine!=-1){
            System.out.print(this.currentLine);
        }
        else{
            System.out.print("-");
        }
        System.out.println(")");
    }
    
    public static void main(String args[]) {
        FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
        fctEnvRecord.beginScope();
        fctEnvRecord.dump();
        fctEnvRecord.startFunction("g", 1, 20);
        fctEnvRecord.dump();
        fctEnvRecord.setLineNumber(5);
        fctEnvRecord.dump();
        fctEnvRecord.enter("a",4, false);
        fctEnvRecord.dump();
        fctEnvRecord.enter("b",2, false);
        fctEnvRecord.dump();
        fctEnvRecord.enter("c",7, false);
        fctEnvRecord.dump();
        fctEnvRecord.enter("a",1, false);
        fctEnvRecord.dump();
        fctEnvRecord.pop(2);
        fctEnvRecord.dump();
        fctEnvRecord.pop(1);
        fctEnvRecord.dump();
        fctEnvRecord.pop(1);
        fctEnvRecord.dump();
        fctEnvRecord.startFunction("Read", 1, 6);
        fctEnvRecord.enter("n",5, false);
        fctEnvRecord.dump();
        
        
    }  
}
