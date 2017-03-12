/**
 * ByteCodeLoader class
 */
package interpreter;

import interpreter.bytecode.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ByteCodeLoader {
    //uigew ue
    
    protected String codeFile;
    protected BufferedReader in;
    protected Program program;
    protected static Integer position;
    
    /**
     * Constructor
     * @param programFile
     * @throws IOException 
     */
    public ByteCodeLoader(String programFile) throws IOException {
        this.program = new Program();
        this.codeFile=programFile;
        ByteCodeLoader.position = 0;
        try{
            in = new BufferedReader(new FileReader(this.codeFile));  
        }catch(IOException e){
            System.out.println(e);
        }       
    }
    
    /**
     * loadCodes() loads all CodeByte objects to the Program
     * Program resolves addresses 
     * returns program object
     * @return Program
     */   
    public Program loadCodes(){ 
        this.loadAllLinesToProgram();
        this.program.resolveAddress();
        return this.program;
    }
      
    /**
     * loadAllLinesToProgram() method runs loadByteCodeLineToProgram() method
     * to load all byte code lines from the file
     */
    protected void loadAllLinesToProgram(){
        while (true) {
            try {
                this.loadByteCodeLineToProgram();
            }catch (IOException e) {
                break;
            }
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
    protected void loadByteCodeLineToProgram() throws IOException {
        try {
            
            ArrayList<String> codeArgs = new ArrayList<>();
            
            StringTokenizer st = new StringTokenizer(this.in.readLine());
            // Reading a first token from the line in the file
            String code = st.nextToken();
            String classname = CodeTable.get(code);
            
            // Creating an instance for a class name in the CodeTable
            /*
            Class c = Class.forName("interpreter.bytecode."+classname);
            ByteCode newByteCode = (ByteCode)c.newInstance();
            */
            
            
            if(
               "DebuggerCallCode".equals(classname)||
               "DebuggerFormalCode".equals(classname)||
               "DebuggerFunctionCode".equals(classname)||
               "DebuggerLineCode".equals(classname)||
               "DebuggerLitCode".equals(classname)||
               "DebuggerPopCode".equals(classname)||
               "DebuggerReturnCode".equals(classname)
                    ){
                //Skip there codes
                
            }
            else {
                ByteCode newByteCode;
                // Creating an instance for a class name in the CodeTable
                Class c = Class.forName("interpreter.bytecode."+classname);
                newByteCode = (ByteCode)c.newInstance();
            
                /* Added lines above for debugger to work */

                // Adding arguments for the ByteCode to the ArrayList
                while(st.hasMoreTokens()){
                    codeArgs.add(st.nextToken());
                }
                // Initialize arguments into the respective ByteCode
                newByteCode.init(codeArgs);

                newByteCode.setPosition(ByteCodeLoader.position);

                // Adding just created instance into the Program
                this.program.addByteCode(newByteCode);
                ByteCodeLoader.position++;
            }
            
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (NullPointerException e) {
            throw new IOException("***End of File***");
        } 
    }
    
}
