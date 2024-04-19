/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e7gw;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
/**
 *
 * @author Andrew
 */
public class textfield implements ActionListener {
    JTextField tfield;
    String text;
    
    public textfield(JFrame frame, int col, int x, int y, int w, int h) {
        tfield = new JTextField(col);
        tfield.addActionListener(this);
        tfield.setVisible(true);
        tfield.setBounds(x, y, w, h);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        text = tfield.getText();
    }
    
    public JTextField getTField() {
        return tfield;
    }
    
    public double getValue() {
        if (tfield.getText().equals("")) {
            return 0;
        }
        return Double.parseDouble(tfield.getText().trim());
    }
    
    public void visibility(boolean a) {
        tfield.setVisible(a);
    }
}
