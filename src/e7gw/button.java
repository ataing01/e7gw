/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e7gw;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
/**
 *
 * @author Andrew
 */
public class button implements ActionListener {
    JButton button;
    
    public button(JFrame frame, String label, int x, int y, int w, int h) {     
        button = new JButton(label);
        button.addActionListener(this);
        button.setBounds(x, y, w, h);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public JButton getButton() {
        return button;
    }
}
