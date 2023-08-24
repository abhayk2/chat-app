import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


import java.io.*;

class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br ;
    PrintWriter out;

    private  JLabel heading =  new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font = new Font("Poppins",Font.PLAIN,20);
    //private JButton button  = new JButton("Send");



    //Constructor 
    public Server(){
        try {
            server  = new ServerSocket(777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept(); 

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                if(e.getKeyCode()==10){
                  //  System.out.println("You have pressed enter");
                  String contentToSend = messageInput.getText();
                  messageArea.append("Me: "+ contentToSend+"\n");
                  out.println(contentToSend);
                  out.flush();

                  //After sending the message it will put message input screen black
                  messageInput.setText(" ");
                  messageInput.requestFocus();
                }
                //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            }
            
        });
    }

    private void createGUI() {
        //Method for creation of GUI
        this.setTitle("Server Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null); // this will set our GUI screen to center of screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Coding for components
        heading.setFont(new Font("Italic",Font.BOLD,30));
        messageArea.setFont(font);
        messageArea.setEditable(false);
        messageInput.setFont(font);

        //Setting icon and heading around it
        heading.setIcon(new ImageIcon("Clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        //Setting heading at center of the GUI
        heading.setHorizontalAlignment(SwingConstants.CENTER );
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //For setting layout
        this.setLayout(new BorderLayout());
 
        //Adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }


    public void startReading() 
    {
        //thread- It will read the data
        Runnable r1 = ()->{
            System.out.println("Reader started");
            try{
            while(true){
                
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Client Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        //socket.close();
                        break;
    
                    }
                   // System.out.println("Client: "+ msg);
                   messageArea.append("Client: "+ msg+"\n");
                 
                
                
            }
        }catch (Exception e) {
            System.out.println("Connection is closed");
        }
        };
        new Thread(r1).start();;
    }

    public void startWriting()
    {
        //Thread - it will take data from user and send it to the client
        Runnable r2 = ()->{
           System.out.println("Writer Started");
           try{
           while(!socket.isClosed()){ 
            
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close(); break;
                    }
                
            }
        }catch(Exception e ){System.out.println("Connection is closed");}
        };

        new Thread(r2).start();
    }

    public static void main(String[] args){
        new Server();
     
     }

}