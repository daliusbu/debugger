/**
 * DebuggerCodeLoader Class
 */

package interpreter.debugger;

import interpreter.ByteCodeLoader;
import interpreter.bytecode.ByteCode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class DebuggerCodeLoader extends ByteCodeLoader {
    
    private BufferedReader input;
    private DebuggerProgram debuggerProgram;
    private ArrayList<String> initialSourceCode;


    /**
     * Constructor
     * @param filename - *.x file
     * @param codeFile - *.x.cod file
     * @throws IOException 
     */
    public DebuggerCodeLoader(String xfile, String xcodfile) throws IOException {
        super(xcodfile);
        this.initialSourceCode = new ArrayList<>();
        this.debuggerProgram = new DebuggerProgram();
        DebuggerCodeLoader.position = 0;
        try{
            this.input = new BufferedReader(new FileReader(xfile));  
        }catch(IOException e){
            System.out.println(e);
            System.exit(1);
        }
    }
    
    @Override
    /**
     * Loads all byteCodes
     * Loads all lines from the .x file
     */
    public DebuggerProgram loadCodes(){ 
        this.loadAllLinesToProgram();
        this.readLines();
        this.debuggerProgram.resolveAddress();
        return this.debuggerProgram;
    }
    
    /**
     * Reads line from .x file creates the DebuggerEntry object 
     * and adds it to the DebuggerProgram
     */
    private void readLines(){
        try{
            String line;
            while ((line = this.input.readLine()) != null){
                DebuggerEntry debuggerEntry = new DebuggerEntry();
                debuggerEntry.setEntry(line, false);
                this.debuggerProgram.addEntry(debuggerEntry);
                this.initialSourceCode.add(line);
            }
        }
        catch (IOException e){}
    }
    
    /**
     * Returns the SourceCode array
     * @return source code array
     */
    public ArrayList<String> getSourceCode(){
        return this.initialSourceCode;
    }
    
    public String getLineFromSourceCode(int lineNumber){
        int size = this.initialSourceCode.size();
        int index = lineNumber - 1;
        if(index < size){
            return this.initialSourceCode.get(index);
        }
        else{
            return null;
        }
    }
    
    /**
     * loadByteCodeLineToProgram() method reads through the tokens. 
     * 1.First token will be used to create an object using CodeTable and 
     * reflections
     * 2.If there are more tokens, they will be added as arguments to the above
     * created object.
     * 3.Object added to the Program object
     * @throws IOException 
     */
    @Override
    protected void loadByteCodeLineToProgram() throws IOException {
        try {
            
            ArrayList<String> codeArgs = new ArrayList<>();
            
            StringTokenizer st = new StringTokenizer(this.in.readLine());
            // Reading a first token from the line in the file
            String code = st.nextToken();
            String classname = DebuggerCodeTable.get(code);
            ByteCode newByteCode;
            
            
            if(
               "DebuggerCallCode".equals(classname)||
               "DebuggerFormalCode".equals(classname)||
               "DebuggerFunctionCode".equals(classname)||
               "DebuggerLineCode".equals(classname)||
               "DebuggerLitCode".equals(classname)||
               "DebuggerPopCode".equals(classname)||
               "DebuggerReturnCode".equals(classname)||
               "DebuggerReadCode".equals(classname)||
               "DebuggerStoreCode".equals(classname)
                    ){
                // Creating an instance for a class name in the CodeTable
                Class c = Class.forName("interpreter.bytecode.debuggerByteCodes."+classname);
                newByteCode = (ByteCode)c.newInstance();
            }
            else {
                // Creating an instance for a class name in the CodeTable
                Class c = Class.forName("interpreter.bytecode."+classname);
                newByteCode = (ByteCode)c.newInstance();
            }
            
            // Adding arguments for the ByteCode to the ArrayList
            while(st.hasMoreTokens()){
                codeArgs.add(st.nextToken());
            }
            // Initialize arguments into the respective ByteCode
            newByteCode.init(codeArgs);
                        
            newByteCode.setPosition(ByteCodeLoader.position);

            // Adding just created instance into the Program
            this.debuggerProgram.addByteCode(newByteCode);
            ByteCodeLoader.position++;
            
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println(e);
        } catch (NullPointerException e) {
            throw new IOException("***End of File***");
        } 
    }
    
}





