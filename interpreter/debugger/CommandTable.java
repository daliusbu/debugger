/**
 * Command Table Class
 */

package interpreter.debugger;

import java.util.HashMap;

public class CommandTable {

    private static HashMap<String,String> hashTable = new HashMap<>();
    
    /**
     * Constructor
     */
    public CommandTable(){
        CommandTable.init();
    }

    /**
     * Creates Hash Table of Command names
     */
    public static void init(){       
        hashTable.put("stepover",      "StepOver");
        hashTable.put("stepin",        "StepInto");
        hashTable.put("stepout",       "StepOut");
        hashTable.put("set",           "SetBreakpoint");
        hashTable.put("list",          "ListBreakpoints");
        hashTable.put("clear",         "ClearBreakpoint");
        hashTable.put("lvars",         "LocalVariables");
        hashTable.put("changevar",     "ChangeLocalVariable");
        hashTable.put("funct",         "CurrentFunction");
        hashTable.put("cont",          "ContinueExecution");
        hashTable.put("watch",         "WatchVariable");
        hashTable.put("modify",        "ModifyProgram");
        hashTable.put("halt",          "HaltExecution");
        
        hashTable.put("replace",       "ReplaceStatement");
        hashTable.put("insert",        "InsertStatement");
        hashTable.put("preview",       "PreviewStatement");
        hashTable.put("save",          "SaveStatement");
    }
    
    /**
     * Returns Command Class Name
     * @param code
     * @return code
     */
    public static String get(String code){ 
        CommandTable.init();
        return (String) hashTable.get(code);
    }
}
