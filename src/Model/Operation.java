package Model;
import java.io.Serializable;
public class Operation implements Serializable {
    private static final long serialVersionUID=1L;
    private int op1;
    private String op;
    private int op2;
    public Operation(int op1, String op, int op2){
        this.op1=op1;
        this.op=op;
        this.op2=op2;
    }
    public int getOp1(){return op1;}
    public String getOp(){return op;}
    public int getOp2(){return op2;}
}