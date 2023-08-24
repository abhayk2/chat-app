public class driverApplication {
    public static void main(String[] args){
        Runnable r1=()->{
            new Client();
        };
        new Thread(r1).start();
        Runnable r2=()->{
            new Server();
        };
        new Thread(r2).start();
        
     
     }
}
