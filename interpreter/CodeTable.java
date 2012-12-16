/**
 * Code Table Class
 */
package interpreter;

import java.util.HashMap;

public class CodeTable {

    private static HashMap<String,String> hashTable = new HashMap<>();
    
    /**
     * Constructor
     */
    public CodeTable(){
        CodeTable.init();
    }

    /**
     * Creates Hash Table of Byte Code names
     */
    public static void init(){       
        hashTable.put("HALT",       "HaltCode");
        hashTable.put("POP",        "PopCode");
        hashTable.put("FALSEBRANCH","FalseBranchCode");
        hashTable.put("GOTO",       "GotoCode");
        hashTable.put("STORE",      "StoreCode");
        hashTable.put("LOAD",       "LoadCode");
        hashTable.put("LIT",        "LitCode");
        hashTable.put("ARGS",       "ArgsCode");
        hashTable.put("CALL",       "CallCode");
        hashTable.put("RETURN",     "ReturnCode");
        hashTable.put("BOP",        "BopCode");
        hashTable.put("READ",       "ReadCode");
        hashTable.put("WRITE",      "WriteCode");
        hashTable.put("LABEL",      "LabelCode");
        hashTable.put("DUMP",       "DumpCode");
        // debugger codes:
        /*
        hashTable.put("CALL",       "DebuggerCallCode");
        hashTable.put("LIT",        "DebuggerLitCode");
        hashTable.put("POP",        "DebuggerPopCode");
        hashTable.put("RETURN",     "DebuggerReturnCode");
        hashTable.put("FORMAL",     "DebuggerFormalCode");
        hashTable.put("FUNCTION",   "DebuggerFunctionCode");
        hashTable.put("LINE",       "DebuggerLineCode");  
        * */
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
