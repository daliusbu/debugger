/**
 * RunTimeStack Class
 */
package interpreter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class RunTimeStack {
    
    private ArrayList<Integer> runStack;
    private Stack<Integer> framePointers;
    private Integer poppedValue;
    private ArrayList<String> dumpOutput;

    /**
     * Constructor
     */
    public RunTimeStack(){
        this.runStack = new ArrayList<>();
        this.framePointers = new Stack<>();
        this.dumpOutput = new ArrayList<>();
        this.framePointers.push(0);
    }
    
    /**
     * Dump the RunTimeStack information for debugging
     */
    public void dump() {
        int len = this.runStack.size();
        Stack<Integer> clonedFramePointers;
        /*
         * Will clone framePointers stack so we don't remove items from the 
         * original stack
         */
        clonedFramePointers = (Stack<Integer>)this.framePointers.clone();

        while(!clonedFramePointers.empty()){
                  
            int pointer = clonedFramePointers.pop();
            int frameSize = len - pointer;
            int frameTop = pointer + frameSize;
            len = pointer;
            
            if(!this.runStack.isEmpty()){
                String frame = "[";
                for(int i=pointer; i<frameTop; i++){
                    if(this.runStack.get(i)!=null){
                        frame += this.runStack.get(i)+"";
                        if(i+1<frameTop){
                            frame += ","; 
                        }
                    }
                }
                frame+="] ";
                // Will add frames to ArrayList for later output
                this.dumpOutput.add(frame);
            }
        }
        int outputFrames = this.dumpOutput.size();
        for(int i=outputFrames-1; i>=0; i--){
            /* Print frames */
            System.out.print(this.dumpOutput.get(i));
        }
        System.out.println();
        /* clear the output ArrayList so we can reuse it */
        this.dumpOutput.clear();
    }
    
    /**
     * Peek top item on the runtime stack
     * @return top item on the runtime stack
     */
    public int peek(){
        int size = this.runStack.size();
        int lastIndex = size - 1;
        if(lastIndex>=0){
            return this.runStack.get(lastIndex);
        }
        else{
            return -1;
        }
    }
    
    /**
     * Pop top item on the runtime stack
     * @return top item on the runtime stack
     */
    public int pop(){
        int lastIndex = this.runStack.size() - 1;
        if(lastIndex>=0){
            return this.runStack.remove(lastIndex);
        }
        else{
            return -1;
        }
    }
    
    /**
     * Push item on to the stack
     * @param i - push item on the runtime stack
     * @return item just pushed
     */
    public int push(int i){
        this.runStack.add(i);
        return i;
    }
    
    /**
     * Start a new frame at offset
     * @param offset - indicates the number of slots down from the top of
     *                 RunTimeStack for starting a new frame
     */
    public void newFrameAt(int offset){
        int size = this.runStack.size();
        this.framePointers.push(size-offset);
    }
    
    /**
     * We pop the top frame when we return from a function, before popping, the
     * function's return value is at the top of the stack so we'll save the 
     * value, pop the top frame and then push the return value
     */
    public void popFrame(){
        int offset = 0;
        int lastIndex = this.runStack.size() - 1;
        if(!this.runStack.isEmpty()){        
            this.poppedValue=this.runStack.get(lastIndex);
        }
        if(!this.framePointers.empty()){
            offset = this.framePointers.pop();
        }
        for(int i = lastIndex; i>=offset; i--){
            if(!this.runStack.isEmpty()){
                this.runStack.remove(i);
            }
        }
        this.runStack.add(this.poppedValue);
    }
    
    /**
     * Get top frame values
     * @return topValues ArrayDeque
     */
    public ArrayDeque<Integer> getTopFrameValues(){
        ArrayDeque<Integer> topValues = new ArrayDeque<>();
        ArrayList runStackClone = (ArrayList<Integer>)this.runStack.clone(); 
        Stack<Integer> framePointersClone = (Stack<Integer>)this.framePointers.clone();
        
        int offset = 0;
        int lastIndex = this.runStack.size() - 1;
        if(!framePointersClone.empty()){
            offset = framePointersClone.pop();
        }
        for(int i = lastIndex; i>=offset; i--){
            if(!runStackClone.isEmpty()){
                topValues.addFirst((Integer)runStackClone.remove(i));
            }
        }
        
        return topValues;
    }
    
    /**
     * Get value from the runStack based on the offset
     * @param offset
     * @return 
     */
    public Integer getValue(Integer offset){
        Integer value = null;
        Object[] values = this.getTopFrameValues().toArray();
        if(values.length!=0){
            value = (Integer)values[offset];
        }
        return value;
    }
    
    /**
     * Replace value in the top frame
     * @param from
     * @param to
     * @param offset 
     */
    public void replace(Integer from, Integer to, int offset){
        int size = this.runStack.size();
        for(int i=offset; i<size; i++ ){
            if(this.runStack.contains(from)){
                int index = this.runStack.indexOf(from);
                this.runStack.remove(index);
                this.runStack.add(index, to);
            }
        }
    }
    
    /**
     * Returns popped value from the last frame
     * @return Integer
     */
    public Integer getPoppedValue(){
        return this.poppedValue;
    }
    
    /**
     * Used to store into variables
     * @param offset
     * @return 
     */
    public int store(int offset){
        int popped;
        int lastIndex = this.runStack.size() - 1;
        if(!this.runStack.isEmpty()){
            popped = this.runStack.remove(lastIndex);
            this.runStack.set(offset,popped);
        }
        else {
            popped = -1;
        }
        return popped;
    }
    
    /**
     * Used to load variables onto the stack
     * @param offset
     * @return 
     */
    public int load(int offset){
        int value = -1;
        if(!this.framePointers.empty()){
            int frameStartsAt = this.framePointers.peek();
            int loadFrom = frameStartsAt + offset;
            if(loadFrom<this.runStack.size()){
                value = this.runStack.get(loadFrom);
                this.runStack.add(value);
            }
        }
        return value;
    }
    
    /**
     * Used to load literals onto the stack - e.g. for LIT 5 we'll call 
     * push with 5
     * @param i
     * @return 
     */
    public Integer push(Integer i){
        this.runStack.add(i);
        return i;
    }
    
    /**
     * Return the cloned copy of the stack
     * @return stack
     */
    public ArrayList<Integer> getStack(){
        return (ArrayList<Integer>)this.runStack.clone();
    }
    
    /**
     * Checks is runStack is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return this.runStack.isEmpty();
    }
    
    /**
     * Returns RunStack size
     * @return 
     */
    public Integer getRunStackSize(){
        return this.runStack.size();
    }
    
    /**
     * Returns top pointer
     * @return Integer
     */
    public Integer getPointer(){
        return this.framePointers.peek();
    }
    
}
