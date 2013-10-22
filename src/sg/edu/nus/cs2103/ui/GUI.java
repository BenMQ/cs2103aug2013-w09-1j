
package sg.edu.nus.cs2103.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Liu Dake
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author GekimoeAsagi
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public GUI() {
    	manager = TaskManager.getTaskManager();
		parser = InputParser.getInputParser(manager);
        initComponents();
		
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	
    	outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
        foundationPanel = new javax.swing.JPanel();
        mainTextFrameScrollPane = new javax.swing.JScrollPane();
        mainTextFrame = new javax.swing.JTextArea();
        inputText = new javax.swing.JTextField();
        sudooleButton = new javax.swing.JButton();
        floatingTaskScrollPane = new javax.swing.JScrollPane();
        floatingList = new javax.swing.JList();
        labelFloatingTasks = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        complecationRate = new javax.swing.JLabel();
        complecationRate.setVisible(true);
        progressBar.setValue(manager.getCompletedPercentage());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainTextFrame.setEditable(false);
        mainTextFrame.setColumns(20);
        mainTextFrame.setRows(5);
        mainTextFrameScrollPane.setViewportView(mainTextFrame);

        inputText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextActionPerformed(evt);
            }
        });
        inputText.addKeyListener(new KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    	//mainTextFrame.setText("Search result for \"homework\":\n1. CS2101 homework by tomorrow 4pm.\n2. CS1101s JFDI homework by Sep 25th.");
                        //inputText.setText("");
                    	String userInput =  inputText.getText();
            			parser.parseCommand(userInput);
            			inputText.setText("");
            			mainTextFrame.setText(outContent.toString());
            			progressBar.setValue(manager.getCompletedPercentage());
                    }
                }
            });

        
            sudooleButton.setText("Sudoole");
            sudooleButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    sudooleButtonActionPerformed(evt);
                }
            });

            floatingList.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { "This part is", "currently", "underconstruction"};
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            floatingTaskScrollPane.setViewportView(floatingList);

            labelFloatingTasks.setText("Floating Tasks");

            complecationRate.setText("Complecation rate");

            org.jdesktop.layout.GroupLayout foundationPanelLayout = new org.jdesktop.layout.GroupLayout(foundationPanel);
            foundationPanel.setLayout(foundationPanelLayout);
            foundationPanelLayout.setHorizontalGroup(
                foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(foundationPanelLayout.createSequentialGroup()
                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(foundationPanelLayout.createSequentialGroup()
                            .add(inputText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 518, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(sudooleButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(foundationPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(foundationPanelLayout.createSequentialGroup()
                                    .add(6, 6, 6)
                                    .add(complecationRate)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 521, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(foundationPanelLayout.createSequentialGroup()
                                    .add(mainTextFrameScrollPane)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(floatingTaskScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, labelFloatingTasks))))))
                    .addContainerGap())
            );
            foundationPanelLayout.setVerticalGroup(
                foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, foundationPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(foundationPanelLayout.createSequentialGroup()
                            .add(labelFloatingTasks)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(floatingTaskScrollPane))
                        .add(mainTextFrameScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 193, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(complecationRate))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(inputText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(sudooleButton)))
            );

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(foundationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(foundationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            );

            pack();
        }// </editor-fold>                        

    private void inputTextActionPerformed(java.awt.event.ActionEvent evt) {                                            
       
    }                                           

    private void sudooleButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	String userInput =  inputText.getText();
		parser.parseCommand(userInput);
		inputText.setText("");
		mainTextFrame.setText(outContent.toString());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    
    
    
    // Variables declaration - do not modify   
    private ByteArrayOutputStream outContent;
    private javax.swing.JButton sudooleButton;
    private javax.swing.JLabel labelFloatingTasks;
    private javax.swing.JLabel complecationRate;
    private javax.swing.JList floatingList;
    private javax.swing.JPanel foundationPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane floatingTaskScrollPane;
    private javax.swing.JScrollPane mainTextFrameScrollPane;
    private javax.swing.JTextArea mainTextFrame;
    private javax.swing.JTextField inputText;
    private TaskManager manager;
    private InputParser parser;
    // End of variables declaration                   
}
