package sk.kosickaakademia.stovcikova.company.log;

public class Log {
    public void error(String msg){
        System.out.println("[Error] : "+msg);
    }
    public void print(String msg){
        System.out.println("[OK] :"+msg);
    }
    public void info(String msg){
        System.out.println("[INFO] :" + msg);
    }
}
