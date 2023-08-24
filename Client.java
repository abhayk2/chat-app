import java.net.*;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {
    
    Socket socket;

    BufferedReader br ;
    PrintWriter out;

    //Components of GUI
    private final JLabel heading =  new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font = new Font("Poppins",Font.PLAIN,20);

    //Constructor
    public Client(){
        try {
            try (Scanner sc = new Scanner(System.in)) {
                System.out.println("Enter the ip address of System");
                String ip = sc.next();
                System.out.println("Sending request to server");
                socket = new Socket(ip,777);

                
                
            }
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();
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
        this.setTitle("Client Messenger");
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
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
    
                    }
                    //System.out.println("Server: "+ msg);
                    messageArea.append("Server: "+ msg+"\n");
                
                
                
            }
        }catch(Exception e ){System.out.println("Connection is closed");}
        };
        new Thread(r1).start();
    }

    public void startWriting()
    {
        //Thread - it will take data from user and send it to the client
        Runnable r2 = ()->{
           System.out.println("Writer Started");
            while(!socket.isClosed()){
                try{
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close(); break;
                    }
                }catch(Exception e){
                    System.out.println("Connection is closed");
                }
            }
        };

        new Thread(r2).start();
    }
    public static void main(String[] args){
        new Client();
     
     }
}

