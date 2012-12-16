/**
 * DebuggerVirtualMachine Class
 */

package interpreter.debugger;

import interpreter.VirtualMachine;
import interpreter.bytecode.ByteCode;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;


public class DebuggerVirtualMachine extends VirtualMachine {
    
    private DebugCommand debugCommand;
    private DebuggerProgram debuggerProgram;
    private Stack<FunctionEnvironmentRecord> environmentStack;
    private boolean canSetBreakpoint;
    private boolean printOutput;
    private String error;
    private boolean continueExecuting;
    private ArrayList<Integer> breakPointsList;
    private Integer currentCodeLine;
    private Integer savedLineNumber;
    private String currentFunction;
    private boolean printAll;
    private int envSize;
    private boolean readingCommand;
    private int previousPC;
    private boolean toReadLine;
    private boolean isStepOutPending;
    private boolean isStepInSet;
    private boolean isStepOverSet;
    private boolean watchingSet;
    private String watchOutput;
    private HashMap<String,Integer> watchStack;
    private boolean watchedValueChanged;
    private ArrayList<DebuggerEntry> entriesCopy;
    private boolean returnForInput;
    private Integer lineToReplace;
    private String inputOperation;
    private boolean saved;
    private int offset;
    
    /**
     * Constructor
     * @param debuggerProgram 
     */
    public DebuggerVirtualMachine(DebuggerProgram debuggerProgram){
        super(debuggerProgram);
        this.printAll = true;
        this.isRunning = true;
        this.canSetBreakpoint = false;
        this.printOutput = false;
        this.error = null;
        this.continueExecuting = true;
        this.debuggerProgram = debuggerProgram;
        this.environmentStack = new Stack(); 
        this.breakPointsList = new ArrayList<>();
        this.isStepOutPending = false;
        this.isStepInSet = false;
        this.isStepOverSet = false;
        this.watchingSet = false;
        this.watchOutput=null;
        this.readingCommand = false;
        this.toReadLine = false;
        this.previousPC = 0;
        this.currentCodeLine = -1;
        this.watchStack = new HashMap<>();
        this.watchedValueChanged = false;
        this.returnForInput = false;
        this.saved = true;
        this.offset = 0;
        this.init();
    }
    
    /**
     * Initializes DebuggerVirtualMachine
     */
    private void init(){
        this.setDebuggerEntries();
        this.pc = 0;
        this.startNewScope();
    }
    
    /**************************************************************************/
    /***************************  COMMAND METHODS  ****************************/
    /**************************************************************************/
    
    /**
     * Execute command method
     * @param command 
     */
    public void executeCommand(DebugCommand command){
        this.debugCommand = command;
        this.checkCommand();
    }
    
    /**
     * Checks if command exists and executes it
     */
    private void checkCommand(){
        if (this.debugCommand == null){

        }
        else {
            this.debugCommand.execute(this);
        }
    }
    
    /**
     * Main Execute Command Method - has the main functionality of the class
     */
    public void execute(){
        this.pc = this.previousPC;
        while(this.isRunning){
            ByteCode code = this.debuggerProgram.getCode(pc);
            code.execute(this);
            this.pc=this.getPC();
            
            if(this.getWatchingSet()){
                if(this.watchedValueChanged){
                    if(this.isBreakpointSetOnLine(this.currentCodeLine)){
                        this.setIsRunning(false);
                        this.setPrintOutput(true);
                        this.setPrintAll(true);
                    }
                    else{
                        this.setPrintOutput(true);
                        this.setIsRunning(true);
                        this.setPrintAll(false);
                    }
                }
            }
            
            if(this.getIsStepOutPending()){
                /* Let's check for breakpoints - if it's on the current 
                 * line - overwrite stepOut
                 */
               
                if(this.getEnvSize()!=this.getEnvironmentStackSize()){
                    /*
                     * Environment stack size was set in StepOut in case current 
                     * Environment Stack size drops below envSize - it means 
                     * we stepped out
                     */
                    if(this.getEnvironmentStackSize()<this.getEnvSize()){

                        this.setIsRunning(false);
                        this.setPrintOutput(true);
                        this.setCurrentFunction();
                        // Reset stepout
                        this.setIsStepOutPending(false);
                    }
                }   
                else{
                    this.setIsRunning(true);
                }
                
                if(this.isBreakpointSetOnLine(this.getCurrentLine())){
                    this.setIsRunning(false);
                    this.setPrintOutput(true);
                    this.setCurrentFunction();
                    // Reset stepout
                    this.setIsStepOutPending(false);
                }
                
            }
            else if(this.isStepInSet){
                if(this.getEnvSize()!=this.getEnvironmentStackSize()||
                   this.currentCodeLine!=this.savedLineNumber){
                    if(this.getEnvSize()<this.getEnvironmentStackSize()){
                        this.setIsRunning(false);
                        this.setPrintAll(true);
                        this.setCurrentFunction();
                        this.setIsStepInSet(false); 
                    }
                }
            }
            else if(this.isStepOverSet){
                if(this.savedLineNumber!=this.currentCodeLine){
                    this.setIsRunning(false);
                    this.setPrintOutput(true);
                    this.setPrintAll(true);
                    this.setCurrentFunction();
                    this.setIsStepOverSet(false);
                }
            }

            int p = ++pc;
            this.previousPC = p;
            if(this.debuggerProgram.getByteCodesSize()<=pc){
                this.setIsRunning(false);
                this.previousPC = 0;
                this.setPrintAll(true);
            }
        }
    }
    
    /**
     * Prints source code and command
     * @return 
     */
    public String printCommand(Integer currentLine){
        String str;
        str = "";
        if(this.readingCommand){
            
            if(this.printAll){
                if(this.currentFunction!=null){
                    str += "\n---------------------------";
                    str += this.getCurrentFunction();   
                    str += "---------------------------";
                }  
                else{    
                    str += "*** READ ***";
                }
            }
        }
        else{
            str += this.debugCommand.toString();
            if(this.printAll){
                str += "\n---------------------------";
                if(this.currentFunction!=null){
                    str += this.getCurrentFunction();    
                }
                else{
                    str += this.returnAllLines(currentLine);
                }
                str += "---------------------------";
            }
        }
        this.readingCommand = false;
        return str;
    }
    
    /**
     * CHecks if to read line
     * @return boolean
     */
    public boolean readLine(){
        return this.toReadLine;
    }
    
    /**
     * Read line setter
     * @param read 
     */
    public void setToReadLine(boolean read){
        this.toReadLine = read;
    }
    
    /**
     * readingCommand setter
     * @param read 
     */
    public void setReadingCommand(boolean read){
        this.readingCommand = read;
    }
    
    /**
     * printAll setter
     * @param print 
     */
    public void setPrintAll(boolean print){
        this.printAll = print;
    }
    
    /**
    * Sets debug command
    * @param debugCommand 
    */
    public void setDebugCommand(DebugCommand debugCommand){
        this.debugCommand = debugCommand;
    }

    /**
    * Returns debug command
    * @return debugCommand
    */
    public DebugCommand getDebugCommand(){
        return this.debugCommand;
    }
    
    /**
     * Set isStepOutPending
     * @param isPending 
     */
    public void setIsStepOutPending(boolean isPending){
        this.isStepOutPending = isPending;
    }
    
    /**
     * Get isStepOutPending
     * @return 
     */
    public boolean getIsStepOutPending(){
        return this.isStepOutPending;
    }
    
    /**
     * envSize setter
     * @param setTo 
     */
    public void setEnvSize(int size){
        this.envSize = size;
    }
    
    /**
     * envSize getter
     * @return 
     */
    public int getEnvSize(){
        return this.envSize;
    }
    
    /**************************************************************************/
    /*************************** HELPER  METHODS  *****************************/
    /**************************************************************************/
    
    /**
     * Sets current code line
     * @param lineNumber 
     */
    public void setCurrentCodeLine(Integer lineNumber){
        this.currentCodeLine = lineNumber;
    }
    
    /**
     * Gets current code line
     * @return
     */
    public Integer getcurrentCodeLine(){
        return this.currentCodeLine;
    }
    
    /**
     * Sets Command Error
     */
    public void setError(String str){
    	   this.error = str;
    }
    
    /**
     * Print Error
     * @return error
     */
    public String printError(){
    	   return this.error;
    }
    
    /**
     * Clear Error
     */
    public void clearError(){
    	   this.error = null;
    }
    
    /**
     * Continue executing setter
     * @param setHalt 
     */
    public void setContinueExecuting(boolean setHalt){
    		this.continueExecuting = setHalt;		 
    }
    
    /**
     * Continue executing getter
     * @return 
     */
    public boolean getContinueExecuting(){
    		return this.continueExecuting;		 
    }
    
    /**
     * Get line numbers from x.cod
     * @return 
     */
    public ArrayList<Integer>getLineNumbers(){
        return this.debuggerProgram.getLineNumbers();
    }
    
    /**
     * isRunning setter
     * @param setTo 
     */
    public void setIsRunning(boolean setTo){
        this.isRunning = setTo;
    }
    
    /**
     * isRunning getter
     * @return 
     */
    public boolean getIsRunning(){
        return this.isRunning;
    }
    
    /**
     * Wrapper method
     * @return 
     */
    public String returnAllLines(Integer currentLine){
        String str = "";
        str += this.debuggerProgram.returnAllLines(currentLine);
        return str;
    }
    
    /**
     * FunctionEnvironmentRecord wrapper method
     * @return 
     */
    public int getCurrentLine(){
        return this.peekFromEnvironmentStack().getCurrentLine();
    }
       
    /**
     * Print output getter
     * @return 
     */
    public boolean getPrintOutput(){
    	 return this.printOutput;
    }
    
    /**
     * printOutput setter
     * @param printOutput 
     */
    public void setPrintOutput(boolean printOutput){
    	 this.printOutput = printOutput;
    }
    
    /**************************************************************************/
    /***************************  STACK METHODS  ******************************/
    /**************************************************************************/
    public void setOffset(){
        this.offset = this.getRunStackSize() - 1;
    }
    
    /**
     * Returns offset
     * @return integer
     */
    public int getOffset(){
        return this.offset;
    }
    
    /**
     * Resets offset when entering new frame
     */
    public void resetOffset(){
        this.offset = this.offset - this.getPointer();
    }
    
    /**
     * Enter formals into the symbol table - wrapper method
     * @param key
     * @param value 
     */
    public void enterToSymbolTable(String key, int value, boolean watch){
        this.peekFromEnvironmentStack().enter(key, value, watch);
    }
    
    /**
     * Deletes n values from the symbol table - wrapper method
     * @param n 
     */
    public void deleteFromSymbolTable(int n){
        this.peekFromEnvironmentStack().pop(n);
    }
    
    /**
     * Peeks and returns FunctionEnvironmentRecord object from the environment
     * stack
     * @return 
     */
    public FunctionEnvironmentRecord peekFromEnvironmentStack(){
        return (FunctionEnvironmentRecord)this.environmentStack.peek();
    }
    
    /**
     * On function start creates new FunctionEnvironmentRecord object and
     * adds it into environmetStack
     * @param functionName
     * @param startLine
     * @param endLine 
     */
    public void createNewFrame(String functionName, int startLine, int endLine){               
        FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
        fctEnvRecord.startFunction(functionName, startLine, endLine);
        this.environmentStack.push(fctEnvRecord);      
    }
    
    /**
     * Starts new scope - wrapper method for FunctionEnvironmetRecord beginScope
     */
    public void startNewScope(){               
        FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
        fctEnvRecord.beginScope();
        this.environmentStack.push(fctEnvRecord);      
    }
    
    /**
     * Returns from the function - leaves the scope
     */
    public void leaveScope(){ 
        if(!this.environmentStack.isEmpty()){
            this.environmentStack.pop(); 
        }
    }

    /**
     * Returns Environment Stack size
     * @return Environment Stack size
     */
    public int getEnvironmentStackSize(){
        return this.environmentStack.size();
    }
    
    /**************************************************************************/
    /************************  BREAKPOINT METHODS  ****************************/
    /**************************************************************************/
    
    public boolean isBreakpointSetOnLine(int lineNumber){
        return breakPointsList.contains(lineNumber);
    }
    
    /** 
     * DebuggerProgram wrapper method for updating breakpoint
     */ 
    public void updateEntryBreakpoint(int lineNumber, boolean breakpoint){
        this.debuggerProgram.updateEntryBreakpoint(lineNumber, breakpoint);
    }
    
    /**
     * canSetBreakpoint getter
     * @return 
     */
    public boolean getCanSetBreakpoint(){
        return this.canSetBreakpoint;
    }
    
    /**
     * canSetBreakpoint setter
     * @param canSet 
     */
    public void setCanSetBreakpoint(boolean canSet){
        this.canSetBreakpoint = canSet;
    }
    
    /**
     * Adds breakpoint to the list
     * @param line 
     */
    public void addBreakpoint(int line){
        this.breakPointsList.add(line);
    }
    
    /**
     * Returns breakpoint list array
     * @return 
     */
    public ArrayList<Integer> getBreakpoinList(){
        return (ArrayList<Integer>)this.breakPointsList.clone();
    }
    
    /**
     * Checking if breakpoint is set on line
     * @param lineNumber
     * @return 
     */
    public boolean isBreakpointSetOnLine(Integer lineNumber){
        return this.breakPointsList.contains(lineNumber);
    }
    
    /**
     * Checking if breakpoint is set on line
     * @param lineNumber
     * @return 
     */
    public void removeBreakPointFromTheList(Integer lineNumber){
        this.breakPointsList.remove(lineNumber);
    }
    
    /**
     * Checks if breakpoint is set
     * @return 
     */
    public boolean isBreakpointSet(){
        if(this.breakPointsList.size()>0){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * isStepInSet setter
     * @param set 
     */
    public void setIsStepInSet(boolean set){
        this.isStepInSet = set;
    }
    
    /**
     * isStepInset getter
     * @return boolean
     */
    public boolean getIsStepInSet(){
        return this.isStepInSet;
    }
    
    /**
     * isStepOverSet setter
     * @param set 
     */
    public void setIsStepOverSet(boolean set){
        this.isStepOverSet = set;
    }
    
    /**
     * isStepOverSet getter
     * @return boolean
     */
    public boolean getIsStepOverSet(){
        return this.isStepOverSet;
    }
    
    /**
     * savedLineNumber setter
     * @param saveLine 
     */
    public void setSavedLineNumber(int saveLine){
        this.savedLineNumber = saveLine;
    }
    
    /**
     * savedLineNumber getter
     * @return Integer
     */
    public Integer getSavedLineNumber(){
        return this.savedLineNumber;
    }

    /**************************************************************************/
    /***************  FUNCTION AND LOCAL VARIABLES METHODS  *******************/
    /**************************************************************************/
    /**
     * Set current function
     * @param start
     * @param end
     * @param currentLine 
     */
    public void setCurrentFunction(Integer start, Integer end, Integer currentLine){
        String str = "";
        if(start==-1){
            str += "---Intrinsic function---";
        }
        else{
            str += this.debuggerProgram.returnLinesFromTo(start-1, end, currentLine);
        }
        this.currentFunction = str;
    }
    
    /**
     * Set current function
     */
    public void setCurrentFunction(){
        Integer start   = this.peekFromEnvironmentStack().getStartLine();
        Integer end     = this.peekFromEnvironmentStack().getEndLine();
        Integer current = this.peekFromEnvironmentStack().getCurrentLine();
        this.setCurrentFunction(start, end, current);
        this.setPrintAll(true);
    }
    
    /**
     * Returns current function
     * @return string
     */
    public String getCurrentFunction(){
        return this.currentFunction;
    }
    
    /**
     * Set current function to null
     */
    public void setCurrentFunctionToNull(){
        this.currentFunction = null;
    }
    
    /**
     * Get values form the top frame - will use for getting local variables
     * @return 
     */
    public ArrayDeque<Integer> getTopFrameValues(){
        return this.runStack.getTopFrameValues();
    }
    
    /**
     * Gets local variables
     * @param keys
     * @param vars
     * @return 
     */
    public String getLocalVariables(Set keys, ArrayDeque<Integer> vars){
        String str="";
        Iterator it = keys.iterator();
        while(it.hasNext()){
            if(it.hasNext()){
                String s = (String)it.next();
                Integer offs = this.getValue(s);
                Integer var = this.getValueFromStack(offs);
                str += s + ": " + var + "\n";
            }
        }      
        return str;
    }
    
    /**
     * Get a HashMap of local variables
     * @param keys
     * @param vars
     * @return 
     */
    public HashMap<String,Integer> getLocalVariableMap(Set keys, ArrayDeque<Integer> vars){
        HashMap<String,Integer> locals = new HashMap<>();

        Iterator it = keys.iterator();
        while(it.hasNext()){
            if(it.hasNext()){
                locals.put((String)it.next(), vars.pollFirst());
            }
        }      
        return locals;
    }
    
    /**************************************************************************/
    /***************************  WATCH VARIABLES  ****************************/
    /**************************************************************************/
    /**
     * Wrapper method for FunctionEnvironmentRecord watchVariable
     * @param name
     * @param line 
     */
    public void watchVariable(String name, Integer line){
        this.peekFromEnvironmentStack().watchVariable(name, line);
    }
    
    /**
     * Sets watchingSet
     * @param set 
     */
    public void setWatchingSet(boolean set){
        this.watchingSet = set;
    }
    
    /**
     * Gets watchingSet
     * @return boolean
     */
    public boolean getWatchingSet(){
        return this.watchingSet;
    }
    
    /**
     * Sets watchedValueChanged
     * @param changed 
     */
    public void setWatchedValueChanged(boolean changed){
        this.watchedValueChanged = changed;
    }
    
    /**
     * Gets watchedValueChanged
     * @return boolean
     */
    public boolean getWatchedValueChanged(){
        return this.watchedValueChanged;
    }
    
    /**
     * Sets watchOutput
     * @param output 
     */
    public void setWatchForOutput(String output){
        this.watchOutput = output;
    }
    
    /**
     * Append to watchOutput
     * @param output 
     */
    public void appentWatchForOutput(String output){
        this.watchOutput += output;
    }
    
    /**
     * Checks if watchOutput string is empty
     * @return boolean
     */
    public boolean isWatchOutputEmpty(){
        if(this.watchOutput==null){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Wrapper method for FunctionEnvironmentRecord watchedValueChanged method
     * @param key
     * @param var
     * @return boolean
     */
    public boolean watchedValueChanged(String key, Integer var){
        return this.peekFromEnvironmentStack().watchedValueChanged(key, var);
    }
    
    /**
     * Outputs variable name and value we are watching
     * @return string
     */
    public String outputWatch(){        
        return this.watchOutput;
    }
    
    /**
     * Adds watch variable and line number to the HashMap
     * @param var
     * @param line 
     */
    public void addToWatchStack(String var,Integer line){
        this.watchStack.put(var,line);
    }
    
    /**
     * Checks is the variable name is in a HashMap
     * @param key
     * @return 
     */
    public boolean isInWatchStack(String key){
        return this.watchStack.containsKey(key);
    }
    
    /**
     * Gets Integer from watchStack
     * @param key
     * @return Integer
     */
    public Integer getFromWatchStack(String key){
        return this.watchStack.get(key);
    }
    
    /**
     * FunctionEnvironmentRecord wrapper method for getTop() method
     * @return String
     */
    public String getTopVariable(){
        return this.peekFromEnvironmentStack().getTop();
    }
    
    /**
     * FunctionEnvironmentRecord wrapper method for get() method
     * @param value
     * @return 
     */
    public Integer getValue(String value){
        return this.peekFromEnvironmentStack().get(value);
    }
    
    /**************************************************************************/
    /***************************  CHANGE VARIABLE  ****************************/
    /**************************************************************************/
    /**
     * Replace variable in runStack - wrapper method
     * @param from
     * @param to
     * @param offset 
     */
    public void replace(Integer from, Integer to, int offset){
        this.runStack.replace(from, to, offset);
    }
    
    /**************************************************************************/
    /**************************  MODIFY PROGRAM  ******************************/
    /**************************************************************************/
    /**
     * debuggerEntries setter
     */
    public void setDebuggerEntries(){
        this.entriesCopy = this.debuggerProgram.getEntries();
    }
    
    /**
     * debuggerEntries getter
     * @return ArrayList<DebuggerEntry>
     */
    public ArrayList<DebuggerEntry> getDebuggerEntries(){
        return this.entriesCopy;
    }
    
    /**
     * returnForInput setter
     * @param set 
     */
    public void setReturnForInput(boolean set){
        this.returnForInput = set;
    }
    
    /**
     * returnForInput getter
     * @return boolean
     */
    public boolean getReturnForInput(){
        return this.returnForInput;
    }
    
    /**
     * Replace code line
     * @param line
     * @param entry 
     */
    public void replace(Integer line, String entry){
        DebuggerEntry newEntry = new DebuggerEntry();
        newEntry.setEntry(entry, false);
        Integer index = line-1;
        if(line<this.entriesCopy.size()){
            this.entriesCopy.set(index, newEntry);
        }
    }
    
    /**
     * Insert code line
     * @param line
     * @param entry 
     */
    public void insert(Integer line, String entry){
        DebuggerEntry newEntry = new DebuggerEntry();
        newEntry.setEntry(entry, false);
        Integer index = line;
        if(line<this.entriesCopy.size()){
            this.entriesCopy.add(index, newEntry);
        }
    }
    
    /**
     * lineToReplace setter
     * @param line 
     */
    public void setLineToReplace(Integer line){
        this.lineToReplace = line;
    }
    
    /**
     * lineToReplace getter
     * @return Integer
     */
    public Integer getLineToReplace(){
        return this.lineToReplace;
    }
    
    /**
     * inputOperation setter
     * @param operation 
     */
    public void setInputOperation(String operation){
        this.inputOperation = operation;
    }
    
    /**
     * inputOperation getter
     * @return String
     */
    public String getInputOperation(){
        return this.inputOperation;
    }
    
    /**
     * Generates source code 
     * @return source code string
     */
    public String generateSourceCode(){
        ArrayList<DebuggerEntry> entries = this.getDebuggerEntries();
        String sourceCode = "";
        for(int i=0; i<entries.size(); i++){
            int line = i+1;
            sourceCode += " "+ line + ". "+entries.get(i).toString() + "\n";
        }
        return sourceCode;
    }
    
    /**
     * saved setter
     * @param saved 
     */
    public void setSaved(boolean saved){
        this.saved = saved;
    }
    
    /**
     * saved getter
     * @return boolean
     */
    public boolean getSaved(){
        return this.saved;
    }
    
    /**
     * Writes source code to the file
     * @param source
     * @param file 
     */
    public void writeToFile(String source, String file) throws FileNotFoundException{
        try (PrintWriter out = new PrintWriter(file)) {
            out.print(source);
        }
    }  
}
