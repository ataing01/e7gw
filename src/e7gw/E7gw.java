/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e7gw;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.awt.Rectangle;
import java.awt.image.*;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.tess4j.*;

public class E7gw {

    public static void main(String[] args) throws IOException {
        List<Integer> order = new ArrayList<>();
        List<Integer> enemy = new ArrayList<>();
        List<Integer> self = new ArrayList<>();
        boolean yesturn1 = false;
        
        JFrame frame = new JFrame("E7 GW Speed Checker");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel selfSpd = new JLabel("Highest self spd?");
        selfSpd.setBounds(20, 60, 100, 20);
        textfield SSPD = new textfield(frame, 3, 20, 80, 100, 20);
        
        JButton choosefile = new JButton("Open IMG");
        choosefile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //choosing file
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("you chose a file");
                }

                File file = chooser.getSelectedFile();

                //converting to buffered image
                BufferedImage bimg = new BufferedImage(2778, 1284, BufferedImage.TYPE_BYTE_GRAY);
                int width = 2778;
                int height = 1284;
                try {
                    bimg = ImageIO.read(file);
                } 
                catch (IOException e) {
                    System.out.print(e);
                }

                //set image to grayscale for tesseract
                BufferedImage gs = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = gs.getGraphics();
                g.drawImage(bimg, 0 , 0, null);

                /*
                JFrame frame = new JFrame();
                frame.getContentPane().setLayout(new FlowLayout());
                frame.setVisible(true);
                */

                //OCR
                Tesseract t = new Tesseract();
                t.setTessVariable("tessedit_char_whitelist", "0123456789");
                t.setDatapath(System.getProperty("user.dir") + "tessdata");

                String text = "";
                int clr = 0;
                //340 to 970 / 6
                int y = 340;
                while (y <= 970) {
                    BufferedImage digs = gs.getSubimage(792, y, 40, 35);
                    try {
                        String ph = t.doOCR(digs);
                        
                        //if ph is not empty then append to result text and clear ph
                        if (!"".equals(ph)) {
                            text += ph;
                            ph = "";
                            //check for order
                            clr = gs.getRGB(704, y);
                            order.add(clr);
                        }
                        //System.out.print(text);
                        //frame.getContentPane().add(new JLabel(new ImageIcon(digs)));
                        //frame.pack();
                        //frame.repaint();
                    }
                    catch (TesseractException e) {
                        System.out.println(e);
                        break;
                    }
                    y += 115;
                }
                
                System.out.println("order: ");
                for (int i = 0; i < order.size(); i++) {
                    System.out.println(order.get(i));
                }
                
                int j = 0; //to track order
                for (int i = 0; i < text.length(); i++) {
                    String ph = "";
                    if (Character.isDigit(text.charAt(i))) {
                        //get rest of num
                        while (Character.isDigit(text.charAt(i))) {
                            ph += text.charAt(i);
                            i++;
                        }
                        //set CR and erase ph
                        System.out.println(ph);     
                        if (order.get(j) < -10000000) {
                            enemy.add(Integer.parseInt(ph));
                        }
                        else {
                            self.add(Integer.parseInt(ph));
                        }           
                        j++;
                        ph = "";
                    }
                }
                self.add(100); //add first unit, assuming you take t1
                System.out.println("enemy: ");
                for (int i = 0; i < enemy.size(); i++) {
                    System.out.println(enemy.get(i));
                }
                System.out.println("self: ");
                for (int i = 0; i < self.size(); i++) {
                    System.out.println(self.get(i));
                }
            }
        });
        choosefile.setBounds(140, 20, 100, 20);
        
        JLabel result0 = new JLabel();
        result0.setBounds(20, 160, 200, 20);
        JLabel result1 = new JLabel();
        result1.setBounds(20, 180, 200, 20);
        JLabel result2 = new JLabel();
        result2.setBounds(20, 200, 200, 20);
        JLabel result3 = new JLabel();
        result3.setBounds(20, 220, 200, 20);
        JLabel result4 = new JLabel();
        result4.setBounds(20, 240, 200, 20);
        
        JButton button2 = new JButton("Calculate");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                result0.setText("");
                result1.setText("");
                result2.setText("");
                result3.setText("");
                result4.setText("");
                double selfSpeed = SSPD.getValue();
                double selfCR = self.get(2); //assume that you take t1
                double self1CR = self.get(1);
                double self2CR = self.get(0);
                double enemyCR = enemy.get(2);
                double enemy1CR = enemy.get(1);
                double enemy2CR = enemy.get(0);
                
                double enemy0Speed = enemyCR / selfCR * selfSpeed;   
                result0.setText("Fastest enemy unit speed: " + enemy0Speed);
                
                double enemy1Speed = enemy1CR / enemyCR * enemy0Speed;  
                result1.setText("Enemy unit 1 speed: " + enemy1Speed);
                
                double enemy2Speed = enemy2CR / enemyCR * enemy0Speed;
                result2.setText("Enemy unit 2 speed: " + enemy2Speed);
                
                double self1Speed = self1CR / selfCR * selfSpeed;
                result3.setText("Own unit 1 speed: " + self1Speed);
                
                double self2Speed = self2CR / selfCR * selfSpeed;
                result4.setText("Own unit 2 speed: " + self2Speed);

                if (result0.getText() != null) {
                    result0.setVisible(true);
                    frame.add(result0);
                }
                if (result1.getText() != null) {
                    result1.setVisible(true);
                    frame.add(result1);
                }
                if (result2.getText() != null) {
                    result2.setVisible(true);
                    frame.add(result2);
                }
                if (result3.getText() != null) {
                    result3.setVisible(true);
                    frame.add(result3);
                }
                if (result4.getText() != null) {
                    result4.setVisible(true);
                    frame.add(result4);
                }
                frame.repaint();
            }
        });
        button2.setBounds(20, 20, 100, 20);
        
        frame.setLayout(null);
        frame.add(button2);
        frame.add(choosefile);
        frame.add(selfSpd);
        frame.add(SSPD.getTField());
        frame.setVisible(true);
        }
}
