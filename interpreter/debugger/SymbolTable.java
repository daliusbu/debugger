/**
 * SymbolTable Class
 */

package interpreter.debugger;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Set;

/** <pre>
 *  Binder objects group 3 fields
 *  1. a value
 *  2. the next link in the chain of symbols in the current scope
 *  3. the next link of a previous Binder for the same identifier
 *     in a previous scope
 *  </pre>
*/
class Binder {
    private Object value;
    private String prevtop;   // prior symbol in same scope
    private Binder tail;      // prior binder for same symbol
                              // restore this when closing scope
    private boolean watching; // are we watching this variable
    
    Binder(Object v, String p, Binder t, boolean w) {
          this.value    = v; 
          this.prevtop  = p; 
          this.tail     = t; 
          this.watching = w;
    }

    Object getValue(){ 
        return this.value; 
    }
    
    boolean hasValue(){
        if(this.value!=null){
            return true;
        }
        else{
            return false;
        }
    }
    
    void setValue(int value){
        this.value = value;
    }
    
    String getPrevtop(){ 
        return this.prevtop; 
    }
    
    Binder getTail(){ 
        return this.tail; 
    }
    
    boolean areWeWatching(){
        return this.watching;
    }
    
}


/** <pre>
 * The SymbolTable class is similar to java.util.Dictionary, except that
 * each key must be a Symbol and there is a scope mechanism.
 *
 * Consider the following sequence of events for table t:
 * t.put(Symbol("a"),5)
 * t.beginScope()
 * t.put(Symbol("b"),7)
 * t.put(Symbol("a"),9)
 * 
 * symbols will have the key/value pairs for Symbols "a" and "b" as:
 * 
 * Symbol("a") ->
 *     Binder(9, Symbol("b") , Binder(5, null, null) )
 * (the second field has a reference to the prior Symbol added in this
 * scope; the third field refers to the Binder for the Symbol("a")
 * included in the prior scope)
 * Binder has 2 linked lists - the second field contains list of symbols
 * added to the current scope; the third field contains the list of
 * Binders for the Symbols with the same string id - in this case, "a"
 * 
 * Symbol("b") ->
 *     Binder(7, null, null)
 * (the second field is null since there are no other symbols to link
 * in this scope; the third field is null since there is no Symbol("b")
 * in prior scopes)
 * 
 * top has a reference to Symbol("a") which was the last symbol added
 * to current scope
 * 
 * Note: What happens if a symbol is defined twice in the same scope??
 * </pre>
*/
public class SymbolTable {

    private HashMap<String,Binder> symbols = new HashMap<>();
    private String top;    // reference to last symbol added to
                           // current scope; this essentially is the
                           // start of a linked list of symbols in scope
    private Binder marks;  // scope mark; essentially we have a stack of
                           // marks - push for new scope; pop when closing
                           // scope
    private boolean watching;
    
    private String  watchingName;     // Variable name to watch
    private Integer watchingVariable; // Variable value to watch
    private Integer watchingLine;     // Watch variable on this line

    /**
     * Constructor
     */
    public SymbolTable(){

    }
    
    /**
     * Gets the name of the variable we are watching
     * @return string
     */
    public String getWatchingName(){
        return this.watchingName;
    }
    
    /**
     * Get the line we are watching
     * @return line number
     */
    public Integer getWatchingLine(){
        return this.watchingLine;
    }

    /**
     * Used for testing only
     * @param varName
     * @param line 
     */
    public void watchVariable(String varName, Integer line){
        this.watchingName = varName;
        this.watchingLine = line;
    }
    
    /**
     * Used for testing only
     * @return str
     */
    public String getWatchVariable(){
        String str = "";
        str += "Watching " + this.watchingName + ":" + this.watchingVariable;
        str += " on line " + this.watchingLine + "\n";
        return str;
    }
    
    /**
    * Remembers the current state of the SymbolTable; push new mark on mark stack
    */
    public void beginScope() {
        this.marks = new Binder(null,this.top,this.marks,this.watching);
        this.top=null;
        this.watching=false;
    }
    
    /**
    * Puts the specified value into the SymbolTable, bound to the specified Symbol.<br>
    * Maintain the list of symbols in the current scope (top);<br>
    * Add to list of symbols in prior scope with the same string identifier
    */
    public void put(String key, Object value, boolean watch) {
        this.symbols.put(key, new Binder(value, this.top, this.symbols.get(key), watch));
        this.top = key;
    }

    /**
    * Gets the object associated with the specified symbol in the SymbolTable.
    */
    public int get(String key) {
        Binder e = this.symbols.get(key);
        return (Integer)e.getValue();
    }
    
    /**
     * Check if symbol table is empty
     * @return boolean
     */
    public boolean isTableEmpty(){
        return this.symbols.isEmpty();
    }
    
    /**
     * Checking if watched value changed
     * @param key
     * @param var
     * @return boolean
     */
    public boolean watchedValueChanged(String key, Integer var) {
        boolean changed = false;
        if(this.symbols.get(key)!=null){
            Binder e = this.symbols.get(key);
            if(e.hasValue()){
                Integer stored = (Integer)e.getValue();
                if(stored!=var){
                    changed =  true;
                }
                else{
                    changed =  false;
                }
            }
        }
        return changed;
    }
    
    /**
    * Removes number of Objects from the SymbolTable.
    */
    public void remove(int number) {
        for(int i=0; i<number; i++){
           Binder e = this.symbols.get(this.top);
           if (e.getTail()!=null) {
                this.symbols.put(this.top,e.getTail());
           }
           else {
                this.symbols.remove(this.top);
           }
           this.top = e.getPrevtop();
        }
    }
    
    /**
     * Peeks to value
     * @return string
     */
    public String peekTop(){
        Binder e = this.symbols.get(this.top);
        return e.getPrevtop();
    }
    
    /**
     * Gets top value
     * @return string
     */
    public String getTop(){
        return this.top;
    }
    
    /**
     * Remove top value
     * @return string
     */
    public String removeTop(){
        Binder e = this.symbols.remove(this.top);
        return e.getPrevtop();
    }
    
    /**
     * Gets number of objects in the table - used for testing only
     * @return 
     */
    public int getNumberOfObjectsIn(){
        return this.keys().size();
    }
    
    /**
     * Returns all keys and values from the Table in the format:
     * <k,v><k,v>...
     * @return string of <key,val>
     */
    @Override
    public String toString(){
        String str;
        int size = this.getNumberOfObjectsIn();
        Object keys[] = this.keys().toArray();
        str = "";
        //str += "(";
        if(size==0){
            str += "<>";
        }
        else{
            for(int i = 0; i< size; i++){
                str += "<" + keys[i] + "," + this.get((String)keys[i]) + ">";
            }
        }
        //str += ")";
        
        return str;
    }

    /**
     * @return a set of the SymbolTable's symbols.
     */
    public Set<String> keys() {
        return symbols.keySet();
    }
    
    /**
     * @return HashMap of values.
     */
    public HashMap<String,Integer> getVariables() {
        HashMap<String,Integer> values = new HashMap<>();
        int size = this.getNumberOfObjectsIn();
        Object keys[] = this.keys().toArray();
        for(int i = 0; i< size; i++){
            values.put((String)keys[i], (Integer)this.get((String)keys[i]) );
        }

        return values;
    }
    
    /**
     * Get variables as a string - used for testing only
     * @return string
     */
    public String getVariablesAsString() {
        String str = "";
        int size = this.getNumberOfObjectsIn();
        Object keys[] = this.keys().toArray();
        for(int i = 0; i< size; i++){
            str += (String)keys[i] + ": " + this.get((String)keys[i]) + "\n";
        }

        return str;
    }
  
    
    public static void main(String[] paramArrayOfString){
        String str1 = "a";
        String str2 = "b";
        String str3 = "c";
        SymbolTable t = new SymbolTable();
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.put(str1, 0, false);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.put(str2, 1, false);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.put(str3, 2, false);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.put(str1, 10, true);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.remove(2);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        t.put(str3, 22, true);
        t.remove(1);
        System.out.println(t);
        System.out.println(t.getNumberOfObjectsIn());
        System.out.println(t.getVariablesAsString());
    }
}
