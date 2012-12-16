/**
 * DebuggerUI Class
 */
package interpreter.debugger.ui;

import interpreter.Interpreter;
import interpreter.debugger.CommandTable;
import interpreter.debugger.DebugCommand;
import interpreter.debugger.DebuggerCodeLoader;
import interpreter.debugger.DebuggerCodeTable;
import interpreter.debugger.DebuggerProgram;
import interpreter.debugger.DebuggerVirtualMachine;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class ConsoleUI {
    
    private DebuggerCodeLoader dcl;
    private DebuggerVirtualMachine dvm;
    private boolean toKeepRunning;
    
    
    /**
     * Constructor
     */
    public ConsoleUI(String xfile, String xcodfile){
        try {
            DebuggerCodeTable.init();
            this.dcl = new DebuggerCodeLoader(xfile, xcodfile);
        } catch (IOException e) {
                System.out.println("**** " + e + " ****");
        }
        DebuggerProgram debuggerProgram = this.getDcl().loadCodes();
        this.dvm = new DebuggerVirtualMachine(debuggerProgram);
        this.toKeepRunning = true;
    }
    
    /**
     * DebuggerCodeLoader getter
     * @return dcl
     */
    private DebuggerCodeLoader getDcl(){
        return this.dcl;
    }
    
    /**
     * toKeepRunning setter
     */
    public void setToKeepRunning(boolean keepRunning){
        this.toKeepRunning = keepRunning;
    }
    
    /**
     * toKeepRunning getter
     * @return true or false
     */
    public boolean getToKeepRunning(){
        return this.toKeepRunning;
    }

    /**
     * Menu - the main user interface communication method
     */
    public void menu(){
        //this.dvm = new DebuggerVirtualMachine(debuggerProgram);
        String str;
        ArrayList<String> inputArray;

        System.out.println("Type ? for help");
        inputArray = this.getUserInput();
        str = inputArray.get(0);
        while("?".equals(str)){
            this.displayMenu();
            System.out.println("Type ? for help");
            inputArray = this.getUserInput();
            str = inputArray.get(0);
        }
        
        try {

            String command = inputArray.get(0);
            String className = CommandTable.get(command);

            // Creating an instance for a class name in the CommandTable
            Class c;
            c = Class.forName("interpreter.debugger."+className);
            DebugCommand newDebugCommand = (DebugCommand)c.newInstance();

            // Let's get arguments - start with second index
            for(int i=1; i<inputArray.size(); i++){
                newDebugCommand.setArgs(inputArray.get(i));
            }


            this.dvm.executeCommand(newDebugCommand);
            this.dvm.execute();

            if(this.dvm.getContinueExecuting()){
                this.setToKeepRunning(true);		
            }
            else{
                this.setToKeepRunning(false);	
            }
            // If came back from VM with input request
            if(dvm.getReturnForInput()){
                ArrayList<String> newInputArray;
                int replaceLine = this.dvm.getLineToReplace();
                String toInput = "";
                String operation = this.dvm.getInputOperation();
                switch(operation){
                    case "replace":
                        System.out.println("Enter a new statement on line " + replaceLine + ":");
                        newInputArray = this.getUserInput();
                        toInput += "     ";
                        for(int i=0; i<newInputArray.size(); i++){
                            toInput += newInputArray.get(i);
                            toInput += " ";
                        }
                        this.dvm.replace(replaceLine, toInput);
                        this.dvm.setReturnForInput(false);
                        System.out.println("Statement on line " + replaceLine + " was replaced.");
                        break;
                    case "insert":
                        System.out.println("Enter a new statement after line " + replaceLine + ":");
                        newInputArray = this.getUserInput();
                        toInput += "     ";
                        for(int i=0; i<newInputArray.size(); i++){
                            toInput += newInputArray.get(i);
                            toInput += " ";
                        }
                        this.dvm.insert(replaceLine, toInput);
                        this.dvm.setReturnForInput(false);
                        replaceLine = replaceLine + 1;
                        System.out.println("Statement on line " + replaceLine + " was inserted.");
                        break;
                    case "save":
                        Scanner stdin = new Scanner(System.in);
                        String answer;
                        System.out.print("Save changes to file (y/n)? ");
                        answer = stdin.next();
                        if(answer.charAt(0)=='y'){
                            System.out.print("Enter a filename: ");
                            String filename = stdin.next();
                            try {
                                this.dvm.writeToFile(this.dvm.generateSourceCode(), filename);
                            } catch (FileNotFoundException e) {
                                System.out.println(e);
                            }
                            System.out.println("Changes were saved to " + filename);
                            System.out.println("Exiting...");
                            this.setToKeepRunning(false);
                        }
                        else{
                            System.out.println("Exiting...");
                            this.setToKeepRunning(false);
                        }
                        break;
                    default:
                        System.out.println("ERROR!");
                        break;
                }
            }
            else{
                /* Checking for errors */
                if(this.dvm.printError()==null){
                    /* Should we print output? */
                    if(dvm.getWatchedValueChanged()){
                        System.out.println(dvm.outputWatch());
                        // Reset the watch
                        dvm.setWatchedValueChanged(false);
                        if(dvm.isBreakpointSetOnLine(dvm.getCurrentLine())){
                            System.out.println(this.dvm.printCommand(this.dvm.getCurrentLine()));
                        }
                    }
                    else{
                        if(this.dvm.getPrintOutput()){
                            System.out.println(this.dvm.printCommand(this.dvm.getCurrentLine()));
                            this.dvm.setCurrentFunctionToNull();
                        }
                    }
                }
                else{
                    System.out.println(this.dvm.printError());	
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e);
        }

    }
    
    /**
     * Reads a line from the console - inputs an integer
     */
    public void readLine(){
        Scanner stdin = new Scanner(System.in);
        int input;
        System.out.print("Enter an integer: ");
        input = stdin.nextInt();
        dvm.pushRunStack(input);
        // Add read argument into the Symbol table
        dvm.peekFromEnvironmentStack().enter("n", input, false);

        if(dvm.isBreakpointSet()){
            System.out.println("*** READ ***");
            this.dvm.setIsRunning(true);
        }
        else{
            // If no breakpoin - keep on running
            this.dvm.setIsRunning(true);
            this.dvm.execute();
        }
        this.dvm.setToReadLine(false);
    }
    
    /**
     * Get user input
     * @return array of user input tokens
     */
    public ArrayList<String> getUserInput(){
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(input);
        StringTokenizer token;
        String str = null;
        ArrayList<String> inputArray = new ArrayList<>();
        try {
            str=in.readLine();
        } catch (IOException e) {
            System.out.println(e);
        }
        
        token = new StringTokenizer(str);
        while(token.hasMoreTokens()){
            inputArray.add(token.nextToken());
        }
        return inputArray;
    }
    
    /**
     * Outputs the source code from the *.x file
     */
    public void showInitialSourceCode() {
        ArrayList<String> code = this.dcl.getSourceCode();
        for(int i=1; i<=code.size(); i++){
            System.out.print(" "+" "+i+". ");
            System.out.println(code.get(i-1));
        }
        System.out.println();
    }
    
    /**
     * Lists the menu
     */
    public void displayMenu(){
        System.out.println("*************************************************");
        System.out.println("*********************  MENU  ********************");
        System.out.println("*************************************************");
        System.out.println("?:             Help. <?>");
        System.out.println("stepover:      Step over a line");
        System.out.println("stepin:        Step into a function");
        System.out.println("stepout:       Step out of current activation of a function <stepout>");
        System.out.println("set:           Set breakpoint at line. <set n>");
        System.out.println("list:          List current breakpoint settings. <list>");
        System.out.println("clear:         Clear designated breakpoints. <clear n>");
        System.out.println("lvars:         Display local variables. <lvars>");
        System.out.println("changevar:     Change value of local variable. <changevar>");
        System.out.println("funct:         Display the source code of the current function. <funct>");
        System.out.println("cont:          Continue execution. <cont>");
        System.out.println("watch:         Watch variables. <watch x n> x - variable, n - line");
        System.out.println("replace:       Replace statement at line. <replace n> n - line number");
        System.out.println("insert:        Insert statement after line. <insert n> n - line number");
        System.out.println("preview:       Preview source code changes. <preview>");
        System.out.println("save:          Save program to file.x <save filename.x>");
        System.out.println("halt:          Halt execution. <halt>");
        System.out.println("*************************************************");
    }

    
    /**
     * Main Method
     * @param args 
     */
    public static void main(String args[]){
        String option   = null;
        String argument = null;
        String xfile; 
        String xcodfile; 
        ConsoleUI ui;
        if (args.length == 0||args.length > 2) {
            System.out.println("USAGE: java -jar Debugger.jar [-d] <base source file name>");
            System.exit(1);
        }
        else if (args.length == 1){
            if("-d".equals(args[0])){
                System.out.println("USAGE: java -jar Debugger.jar [-d] <base source file name>");
                System.exit(1);
            }
            // interpreter mode
            else{
                argument = args[0];
            }
        }
        else if (args.length == 2){
            if(!"-d".equals(args[0])){
                System.out.println("USAGE: java -jar Debugger.jar [-d] <base source file name>");
                System.exit(1);
            }
            // debugger mode
            else {
                option   = args[0];
                argument = args[1]; 
            }
        }
        // debugger mode
        if(option != null){
            xfile = argument + ".x";     
            xcodfile = argument + ".x.cod"; 
            ui = new ConsoleUI(xfile,xcodfile);
            ui.showInitialSourceCode();
            while(ui.getToKeepRunning()){
                ui.menu();
            }

        }
        //interpreter mode
        else{
            xcodfile = argument + ".x.cod"; 
            (new Interpreter(xcodfile)).run();
        }
    }
    
}
