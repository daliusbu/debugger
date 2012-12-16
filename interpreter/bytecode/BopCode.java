/**
 * BopCode Class
 */
package interpreter.bytecode;

import interpreter.VirtualMachine;
import java.util.ArrayList;

public class BopCode extends ByteCode {

    private final String codeName = "BOP";
    private String arg1;
    private int size = 0;
    private int position = 0;
    
    public BopCode(){
        
    }
    
    @Override
    public void init(ArrayList<String> codeArgs){
        this.size = codeArgs.size();
        this.arg1 = codeArgs.get(0);
    }
    
    @Override
    public Object execute(VirtualMachine vm){
        int rightValue = vm.popRunStack();
        int leftValue = vm.popRunStack();
        int result = this.performOperation(leftValue,rightValue);
        vm.pushRunStack(result);
        return vm;
    }
    
    @Override
    public void setPosition(int position){
        this.position = position;
    }
    
    @Override
    public int getPosition(){
        return this.position;
    }
    
    @Override
    public String getCodeName(){
        return this.codeName;
    }
    
    @Override
    public int getNumberOfAgrs(){
        return this.size;
    }
    
    @Override
    public String getArg1(){
        return this.arg1;
    }
    
    @Override
    public void setGoToPosition(Integer position){
        throw new UnsupportedOperationException("Not supported.");
    }
    
    /**
     * Performs operation on 2 operands
     * @param leftValue
     * @param rightValue
     * @return result
     */
    private int performOperation(int leftValue,int rightValue){
        int result=0;
        String operand = this.getArg1();
        switch(operand){
            case "+":   result = leftValue + rightValue;
                        break;
            case "-":   result = leftValue - rightValue;
                        break;
            case "/":   result = leftValue / rightValue;
                        break;
            case "*":   result = leftValue * rightValue;
                        break;
            case "==":  if(leftValue == rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
            case "!=":  if(leftValue != rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
            case "<=":  if(leftValue <= rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
            case "<":   if(leftValue < rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
                
            case ">=":  if(leftValue >= rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
                
            case ">":   if(leftValue > rightValue){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
                
            case "|":   if( leftValue==0 && rightValue==0 ){
                            result = 0;
                        }
                        else {
                            result = 1;
                        }
                        break;
                
            case "&":   if(leftValue!=0 && rightValue!=0){
                            result = 1;
                        }
                        else {
                            result = 0;
                        }
                        break;
        }      
        
        return result;
    }
    
    @Override
    public void dumpCode(ArrayList<Integer> stack){
        System.out.print(this.getCodeName()+" ");
        System.out.print(this.getArg1()+"\n");
    }
}
