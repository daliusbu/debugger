/**
 * VirtualMachine Class
 */
package interpreter;

import interpreter.bytecode.*;
import java.util.ArrayList;
import java.util.Stack;

public class VirtualMachine {

    protected int pc;
    protected RunTimeStack runStack;
    protected Stack<Integer> returnAddrs;
    protected boolean isRunning;
    protected Program program;
    protected Stack<Integer> savedPC = new Stack<>();
    protected int functionHasArguments;
    protected boolean dumpOn = false;
    protected int savedValue;
    
    /**
     * Constructor
     * @param program 
     */
    public VirtualMachine(Program program){
        this.program = program;
        this.runStack = new RunTimeStack();
        this.returnAddrs = new Stack();
    }
    
    /**
     * Executes a program
     */
    public void executeProgram(){
        this.pc = 0;
        
        //this.returnAddrs = new Stack();
        this.isRunning = true;
        
        while(isRunning){
            ByteCode code = this.program.getCode(pc);
            code.execute(this);
            pc=this.getPC();
            //this.runStack.dump();
            if(dumpOn){
                /* We don't want DUMP codes */
                if(!"DUMP".equals(code.getCodeName())){
                    this.dumpByteCode(code);
                    this.dump();
                }
            }
            pc++;
            if(this.program.getByteCodesSize()<=pc){
                this.isRunningToFalse();
            }
        }
        
    }
    
    /**
     * Wraps runStack getStack method
     * @return stack
     */
    public ArrayList<Integer> getStack(){
        return this.runStack.getStack();
    }
    
    /**
     * Wraps runStack load method
     * @param offset
     * @return loaded value
     */
    public int load(int offset){
        int loadedValue = this.runStack.load(offset);
        return loadedValue;
    }
    
    /**
     * Wraps runStack store method
     * @param offset
     * @return savedValue
     */
    /*
    public int store(int offset){
        int savedValue = this.runStack.store(offset);
        return savedValue;
    }
    */
    public int store(int offset){
        this.savedValue = this.runStack.store(offset);
        return this.savedValue;
    }
    
    
    /**
     * Wraps runStack newFrameAt method
     * @param offset 
     */
    public void newFrameAt(int offset){
        this.runStack.newFrameAt(offset);
    }
    
    /**
     * Wraps runStack popFrame method
     */
    public void popFrame(){
        this.runStack.popFrame();
    }
    
    /**
     * Wraps runStack push method
     * @param arg1
     * @return pushed argument
     */
    public int pushRunStack(Integer arg1){
        int arg = this.runStack.push(arg1);
        return arg;
    }
    
    /**
     * Wraps runStack pop method
     * @return popped value
     */
    public int popRunStack(){
        return this.runStack.pop();
    }
    
    /**
     * Wraps runStack peek method
     * @return peeked value
     */
    public int peekRunStack(){
        return this.runStack.peek();
    }
    
    /**
     * Check if runStack is empty
     * @return 
     */
    public boolean isRuntimeStackEmpty(){
        return this.runStack.isEmpty();
    }
    
    /**
     * Get runStack size - wrapper method
     * @return size
     */
    public Integer getRunStackSize(){
        return this.runStack.getRunStackSize();
    }
    
    /**
     * Returns the pointer from the RunTimeStack - wrapper method
     * @return Integer
     */
    public Integer getPointer(){
        return this.runStack.getPointer();
    }
    
    /**
     * Returns value from the offset
     * @param offset
     * @return Integer
     */
    public Integer getValueFromStack(Integer offset){
        return this.runStack.getValue(offset);
    }
    
    /**
     * Sets program counter
     * @param index 
     */
    public void setPC(Integer index){
        this.returnAddrs.push(index);
    }
    
    /**
     * Gets program counter
     * @return program counter
     */
    public Integer getPC(){
        if(!this.returnAddrs.empty()){
            return this.returnAddrs.pop();
        }
        else {
            return this.pc;
        }
    }
    
    /**
     * Sets isRunning to false - used to stop the program
     */
    public void isRunningToFalse(){
        this.isRunning=false;
    }
    
    /**
     * Sets saved program counter
     * @param address 
     */
    public void setSavedPC(int address){
        this.savedPC.push(address);
    }
    
    /**
     * Gets saved program counter
     * @return program counter
     */
    public int getSavedPC(){
        if(!this.savedPC.empty()){
            return this.savedPC.pop();
        }
        else{
            return 0;
        }
    }
    
    /**
     * Sets number of arguments function call will have
     * @param numberOfArguments 
     */
    public void setFunctionHasArguments(int numberOfArguments){
        this.functionHasArguments=numberOfArguments;
    }
    
    /**
     * Get number of arguments function call will have
     * @return number of arguments
     */
    public int getFunctionHasArguments(){
        return this.functionHasArguments;
    }
    
    /**
     * Sets dumping state to ON or OFF
     * @param state 
     */
    public void setDumpState(boolean state){
        this.dumpOn = state;
    }
    
    /**
     * Gets dumping state
     * @return true or false
     */
    public boolean getDumpState(){
        return this.dumpOn;
    }
    
    /**
     * Wraps runStack dump method
     */
    public void dump(){
        this.runStack.dump();
    }
    
    /**
     * Calls byte code's dumpCode method to dump it's code
     * @param code 
     */
    public void dumpByteCode(ByteCode code){
        String name = code.getCodeName();
        if(!"DUMP".equals(name)){
            code.dumpCode(this.getStack());
        }
    }
    
}
