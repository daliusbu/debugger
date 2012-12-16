/**
 * Debugger Code Table Class
 */
package interpreter.debugger;

import java.util.HashMap;

public class DebuggerCodeTable {

    private static HashMap<String,String> hashTable = new HashMap<>();
    
    /**
     * Constructor
     */
    public DebuggerCodeTable(){
        DebuggerCodeTable.init();
    }

    /**
     * Creates Hash Table of Byte Code names
     */
    public static void init(){       
        hashTable.put("HALT",       "HaltCode");
        hashTable.put("FALSEBRANCH","FalseBranchCode");
        hashTable.put("GOTO",       "GotoCode");
        hashTable.put("LOAD",       "LoadCode");
        hashTable.put("ARGS",       "ArgsCode");
        hashTable.put("BOP",        "BopCode");
        hashTable.put("WRITE",      "WriteCode");
        hashTable.put("LABEL",      "LabelCode");
        hashTable.put("DUMP",       "DumpCode");
        // debugger codes:
        hashTable.put("CALL",       "DebuggerCallCode");
        hashTable.put("LIT",        "DebuggerLitCode");
        hashTable.put("POP",        "DebuggerPopCode");
        hashTable.put("RETURN",     "DebuggerReturnCode");
        hashTable.put("FORMAL",     "DebuggerFormalCode");
        hashTable.put("FUNCTION",   "DebuggerFunctionCode");
        hashTable.put("LINE",       "DebuggerLineCode"); 
        hashTable.put("READ",       "DebuggerReadCode");
        hashTable.put("STORE",      "DebuggerStoreCode");
    }
    
    /**
     * Returns Byte Code
     * @param code
     * @return code
     */
    public static String get(String code){      
        return (String) hashTable.get(code);
    }
}
