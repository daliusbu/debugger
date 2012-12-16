/**
 * DebuggerProgram Class
 */
package interpreter.debugger;

import interpreter.Program;
import interpreter.bytecode.ByteCode;
import interpreter.bytecode.debuggerByteCodes.DebuggerLineCode;
import java.util.ArrayList;


public class DebuggerProgram extends Program{

    private ArrayList<DebuggerEntry> entries;
    private ArrayList<Integer> lineCodes;
    
    /**
     * Constructor
     */
    public DebuggerProgram(){
        super();
        this.lineCodes = new ArrayList<>();
        this.entries = new ArrayList<>();
    }
    
    /**
     * Adds byte code into ArrayList
     * Makes LABEL addresses HashMap with the same iteration to be used for 
     * resolving addresses
     * @param bytecode 
     */    
    @Override
    public void addByteCode(ByteCode bytecode){
        this.bytecodes.add(bytecode);
        
        // Will make LABEL addresses HashMap with the same iteration
        if("LABEL".equals(bytecode.getCodeName())){
            String arg = bytecode.getArg1();
            // arg->key | possition->value
            this.labelCodes.put(arg,bytecode.getPosition());
        }

        if("LINE".equals(bytecode.getCodeName())){
            Integer arg = ((DebuggerLineCode)bytecode).getArg1AsInteger();
            // Enter linu number into the lineCodes array
            this.lineCodes.add(arg);
        }
        
    }
    
    /**
     * Returns label codes HashMap used to resolve addresses
     * @return labelCodes HashMap
     */
    public ArrayList<Integer> getLineNumbers(){
        return this.lineCodes;
    }
    
    /**
     * Adds DebuggerEntry to the DebuggerProgram
     * @param entry 
     */
    public void addEntry(DebuggerEntry entry){
        this.entries.add(entry);
    }
    
    /**
     * Returns entry line and sets breakpoint in front if it exists
     * @param lineNumber
     * @return entry line
     */
    private String returnEntryLine(int lineNumber, boolean showArrow){
        String output = "";
        DebuggerEntry thisEntry = this.getEntry(lineNumber);
        // Start line number from 1
        int line = lineNumber + 1;

        if(thisEntry.isBreakPointSet()){
            output += "*" + line + ". ";
        }
        else {
            output += " " + line + ". ";
        }
        if(showArrow){
            output += thisEntry.getSourceLine() + " <------";
        }
        else{
            output += thisEntry.getSourceLine();
        }
        
        return output;
    }
    
    /**
     * Returns all code lines
     * @return string
     */
    public String returnAllLines(Integer currentLine){
        String str = "\n";
        for(int i=0; i<this.getLineCount(); i++){
            if(i+1==currentLine){
                str += returnEntryLine(i,true);
            }
            else{
                str += returnEntryLine(i,false);
            }
            str += "\n";
        } 
        str += "\n";
        return str;
    }
    
    /**
     * Returns code lines from line # to line #
     * @return string
     */
    public String returnLinesFromTo(Integer start, Integer end, Integer currentLine){
        String str = "\n";
        for(int i=start; i<end; i++){
            if(i==currentLine){
                str += returnEntryLine(i,true);
            }
            else{
                str += returnEntryLine(i,false);
            }
            str += "\n";
        } 
        str += "\n";
        return str;
    }
    
    /**
     * Gets DebuggerEntry line - wrapper method
     * @param lineNumber
     * @return entry line
     */
    public DebuggerEntry getEntry(int lineNumber){
        return this.entries.get(lineNumber);
    }
    
    /**
     * Get code line count
     * @return line count
     */
    public int getLineCount(){
        return this.entries.size();
    }
    
    /**
     * Set breakpoint to the DebuggerEntry line.
     * @param lineNumber
     * @param breakpoint - true to set breakpoint, false to remove it
     */
    public void updateEntryBreakpoint(int lineNumber, boolean breakpoint){
        // Make sure breakpoints set to corrent lines
        this.getEntry(lineNumber-1).setBreakpointTo(breakpoint);
    }
    
    /**
     * Gets Byte Code from position
     * @param pc
     * @return ByteCode
     */
    @Override
    public ByteCode getCode(int pc){       
        return this.bytecodes.get(pc);
    }
    
    /**
     * Returns the clone of DebuggerEntries
     * @return ArrayList<DebuggerEntry>
     */
    public ArrayList<DebuggerEntry> getEntries(){
        return (ArrayList<DebuggerEntry>)this.entries.clone();
    }
    

}
