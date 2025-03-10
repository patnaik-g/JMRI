package jmri.util.swing;

import java.awt.Component;

import javax.swing.*;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.*;
import org.netbeans.jemmy.util.NameComponentChooser;

import jmri.util.JmriJFrame;

/**
 * Utility Methods for Jemmy Tests.
 * 
 * @author Paul Bender Copyright (C) 2018
 */

public class JemmyUtil {
    static public void pressDialogButton(JmriJFrame f, String buttonName) {
        JFrameOperator jfo = new JFrameOperator(f);
        JDialogOperator jdo = new JDialogOperator(jfo, 1); // wait for the first dialog.
        NameComponentChooser bChooser = new NameComponentChooser(buttonName);
        JButtonOperator jbo = new JButtonOperator(jdo, bChooser);
        // Click button
        jbo.push();
    }
    
    static public void pressDialogButton(String dialogTitle, String buttonName) {
        JDialogOperator jdo = new JDialogOperator(dialogTitle); // wait for the first dialog.
        JButtonOperator jbo = new JButtonOperator(jdo, buttonName);
        // Click button
        jbo.push();
    }

    static public void pressDialogButton(JmriJFrame f, String dialogTitle, String buttonName) {
        JFrameOperator jfo = new JFrameOperator(f);
        JDialogOperator jdo = new JDialogOperator(jfo, dialogTitle); // wait for the first dialog.
        JButtonOperator jbo = new JButtonOperator(jdo, buttonName);
        // Click button
        jbo.push();
    }

    static public void enterClickAndLeave(JButton comp) {
        JButtonOperator jbo = new JButtonOperator(comp);
        jbo.push();
    }
    
    static public void enterClickAndLeaveThreadSafe(JButton comp) {
        Thread t = new Thread(() -> {
            JButtonOperator jbo = new JButtonOperator(comp);
            jbo.push();
        });
        t.start();
        jmri.util.JUnitUtil.waitFor(() -> {
            return t.getState().equals(Thread.State.TERMINATED);
        }, "wait for dialog window to appear");
    }

    static public void enterClickAndLeave(JCheckBox comp) {
        JCheckBoxOperator jbo = new JCheckBoxOperator(comp);
        jbo.doClick();
    }

    static public void enterClickAndLeave(JRadioButton comp) {
        JRadioButtonOperator jbo = new JRadioButtonOperator(comp);
        jbo.doClick();
    }

    static public void enterClickAndLeave(JToggleButton comp) {
        JToggleButtonOperator jtbo = new JToggleButtonOperator(comp);
        jtbo.doClick();
    }

    static public void pressButton(WindowOperator frame, String text) {
        JButtonOperator jbo = new JButtonOperator(frame, text);
        jbo.push();
    }
    
    static public void clickOnCellThreadSafe(JTableOperator tbl, int row, String columnName) {
        new Thread(() -> {
            tbl.clickOnCell(row, tbl.findColumn(columnName));
        }).start();
    }

    static public void confirmJOptionPane(WindowOperator wo, String title, String message, String buttonLabel) {
        // the previous version of this message verified the text string
        // if the dialog matched the passed message value.  We need to
        // determine how to do that using Jemmy.
        JDialogOperator jdo = new JDialogOperator(wo, title);
        JButtonOperator jbo = new JButtonOperator(jdo, buttonLabel);
        jbo.push();
    }

    public static Thread createModalDialogOperatorThread(String dialogTitle, String buttonText) {
        Thread t = new Thread(() -> {
            // constructor for jdo will wait until the dialog is visible
            JDialogOperator jdo = new JDialogOperator(dialogTitle);
            JButtonOperator jbo = new JButtonOperator(jdo, buttonText);
            jbo.pushNoBlock();
        });
        t.setName(dialogTitle + " Close Dialog Thread");
        t.start();
        return t;
    }

    static public JLabel getLabelWithText(String frameName, String text) {
        // Find window by name
        JmriJFrame frame = JmriJFrame.getFrame(frameName);

        // find label within that
        JLabel jl = JLabelOperator.findJLabel(frame,new ComponentChooser(){
               @Override
            public boolean checkComponent(Component comp){
                   if(comp == null){
                      return false;
                   } else if (comp instanceof JLabel ) {
                      return ((JLabel)comp).getText().equals(text);
                   } else {
                      return false;
                   }
               }
               @Override
            public String getDescription(){
                  return "find JLabel with text: " + text;
               }
        });
        return jl;
    }
    
    static public void waitFor(JmriJFrame f) {
        f.requestFocus();
        jmri.util.JUnitUtil.waitFor(() -> {
            return f.isActive();
        }, "wait for frame to be active");
    }

}
