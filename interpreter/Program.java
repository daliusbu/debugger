/**
 * Program Class
 */
package interpreter;
 
import interpreter.bytecode.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Program {
    
    protected ArrayList<ByteCode> bytecodes;
    protected HashMap<Integer,ByteCode> resolvedCodes;
    protected HashMap<String,Integer> labelCodes;
    
    /**
     * Constructor
     */
    public Program(){
        this.bytecodes = new ArrayList<>();
        this.resolvedCodes = new HashMap<>();
        this.labelCodes = new HashMap<>();
    }
    
    /**
     * Adds byte code into ArrayList
     * Makes LABEL addresses HashMap with the same iteration to be used for 
     * resolving addresses
     * @param bytecode 
     */
    public void addByteCode(ByteCode bytecode){
        this.bytecodes.add(bytecode);
        
        // Will make LABEL addresses HashMap with the same iteration
        if("LABEL".equals(bytecode.getCodeName())){
            String arg = bytecode.getArg1();
            // arg->key | possition->value
            this.labelCodes.put(arg,bytecode.getPosition());
        }
    }
    
    /**
     * Returns label codes HashMap used to resolve addresses
     * @return labelCodes HashMap
     */
    public HashMap<String,Integer> getLabels(){
        return labelCodes;
    }
    
    /**
     * Returns the size of a ByteCodes ArrayList
     * @return int
     */
    public int getByteCodesSize(){
        return this.bytecodes.size();
    }
    
    /**
     * Gets Byte Code from position
     * @param pc
     * @return ByteCode
     */
    public ByteCode getCode(int pc){       
        return this.bytecodes.get(pc);
    }
    
    /**
     * Resolves addresses
     */
    public void resolveAddress(){
        int size = this.bytecodes.size();
        Integer index;
        for(int i=0; i<size; i++){
            ByteCode code = this.getCode(i);    
            if(code.getNumberOfAgrs()>0){
                String currentCodeName = code.getCodeName();
                String currentArg = code.getArg1();
                /*
                 * Will look only for GOTO, CALL or FALSEBRANCH codes
                 */
                if("GOTO".equals(currentCodeName) || 
                   "CALL".equals(currentCodeName) ||
                   "FALSEBRANCH".equals(currentCodeName)){
                    if(labelCodes.containsKey(currentArg)){
                        Integer goToPosition = labelCodes.get(currentArg);
                        code.setGoToPosition(goToPosition);
                    }
                }

                // Getting the position of the code line
                index = code.getPosition();
            }
            else{
                index = code.getPosition();       
            }
            // Add to resolvedCodes HashMap
            this.resolvedCodes.put(index, code);
        }
    }
    
    /**
     * Prints resolved addresses, used for debugging only
     */
    public void printResolveAddress(){
        int size = this.bytecodes.size();
        for(int i=0; i<size; i++){
            ByteCode code = this.getCode(i);    
            if(code.getNumberOfAgrs()>0){
                String currentCodeName = code.getCodeName();
                String currentArg = code.getArg1();
                if("GOTO".equals(currentCodeName)){
                    if(labelCodes.containsKey(currentArg)){
                        Integer goToPosition = labelCodes.get(currentArg);
                        code.setGoToPosition(goToPosition);
                        System.out.print(code.getPosition()+" ");
                        System.out.print(currentCodeName+" ");
                        System.out.print(currentArg+" ");
                        System.out.print(goToPosition+" ");
                        System.out.println();
                    }
                }
                else{
                    System.out.print(code.getPosition()+" ");
                    System.out.print(currentCodeName+" ");
                    System.out.print(currentArg+" ");
                    System.out.println();
                }
            }
            else{
                System.out.print(code.getPosition()+" ");
                System.out.print(code.getCodeName()+" "); 
                System.out.println();
            }
        }
    }
}
