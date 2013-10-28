
package sg.edu.nus.cs2103.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Liu Dake
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.(NetBeans)
 */

/**
 * This is the GUI class.
 * @author Liu Dake
 */
public class GUI extends javax.swing.JFrame implements NativeKeyListener {

    /**
     * Creates new form GUI
     */
    public GUI() {
    	manager = TaskManager.getTaskManager();
		//parser = InputParser.getInputParser(manager);
		logicHandler = LogicHandler.getLogicHandler(manager, null);
        initComponents();
        this.setVisible(true);
    }

	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		// To implement unimplemented method
	}
    
	 /**
     * Implemented methods for NativeHook.
     * nativeKeyPressed: detect which key is pressed. Here when Space and Tab are detected the visibility
     * of the window will be changed.
     * nativeKeyReleased: detect which key is released, then set the boolean values for the key to false.
     * 
     */
    public void nativeKeyPressed(NativeKeyEvent e) {
            currentKey = e.getKeyCode();
            if(currentKey == Constants.KEYBOARD_TAB){
            	keyOne = true;
            }
            if(currentKey == Constants.KEYBOARD_SPACE){
            	keyTwo = true;
            }
            if(keyOne && keyTwo){
            	if(isShown){
            		isShown = false;
            		toInvisible();
            	}else{
            		isShown = true;
            		toVisible();
            	}
            }
            if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
                    GlobalScreen.unregisterNativeHook();
                    System.exit(0);
            }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    	currentKey = e.getKeyCode();
    	if(currentKey == Constants.KEYBOARD_TAB){
        	keyOne = false;
        }
        if(currentKey == Constants.KEYBOARD_SPACE){
        	keyTwo = false;
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	
    	//Capture all system output
    	outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		//Initialization of all the components for GUI
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

        //Initialization of properties
        mainTextFrame.setEditable(false);
        mainTextFrame.setFont(new java.awt.Font("Monaco", 0, 13));
        mainTextFrame.setForeground(new java.awt.Color(92, 100, 245));
        mainTextFrame.setColumns(20);
        mainTextFrame.setRows(5);
        mainTextFrameScrollPane.setViewportView(mainTextFrame);
        if(manager.isReloaded()){
        	mainTextFrame.setText(Constants.MESSAGE_WELCOME_TO_SUDO_RELOAD);
        }else{
        	mainTextFrame.setText(Constants.MESSAGE_WELCOME_TO_SUDO_FIRST);
        }
        
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
                    	logicHandler.executeCommand(userInput);
            			inputText.setText("");
            			mainTextFrame.setText(outContent.toString());
            			outContent.reset();
            			progressBar.setValue(manager.getCompletedPercentage());
    //        			String[] floatings = updateFloating(manager.getFloatingTask());
            			
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
                String[] strings = {"Learning Java", "Buy the milk", "Buy Surface 2", "Book flight ticket"};
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            floatingTaskScrollPane.setViewportView(floatingList);

            labelFloatingTasks.setText("Floating Tasks");

            complecationRate.setText("Completion rate");
          //Initialization of the window frame. DO NOT MODIFY ANYTHING HERE.
            org.jdesktop.layout.GroupLayout foundationPanelLayout = new org.jdesktop.layout.GroupLayout(foundationPanel);
            foundationPanel.setLayout(foundationPanelLayout);
            foundationPanelLayout.setHorizontalGroup(
                foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(foundationPanelLayout.createSequentialGroup()
                    .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(foundationPanelLayout.createSequentialGroup()
                            .add(inputText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 918, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(sudooleButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(foundationPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(foundationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(foundationPanelLayout.createSequentialGroup()
                                    .add(6, 6, 6)
                                    .add(complecationRate)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 921, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
       //Not used now
    }                        

    private void sudooleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        	String userInput =  inputText.getText();
        	logicHandler.executeCommand(userInput);
			inputText.setText("");
			mainTextFrame.setText(outContent.toString());
			progressBar.setValue(manager.getCompletedPercentage());
    }

   //Set visibility
    public void toInvisible(){
    	this.setVisible(false);
    }
    public void toVisible(){
    	this.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
        	GlobalScreen.registerNativeHook();
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
        }catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
    }
        //</editor-fold>

        /* Create and display the form, set up the key listener */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	GlobalScreen.getInstance().addNativeKeyListener(new GUI());
            }
        });
    }
    
    /*private String[] updateFloating(ArrayList<FloatingTask> ft){
    	ArrayList<String> FloatingTaskString = new ArrayList<String>();
    	for(FloatingTask flt:ft){
    		FloatingTaskString.add(flt.toString());
    	}
    	return (String[]) FloatingTaskString.toArray();
    }
    */
    
    
    // Variables declaration
	private int currentKey;
	private Boolean keyOne = false;
	private Boolean keyTwo = false;
	private Boolean isShown = true;
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
    private LogicHandler logicHandler; 
    //////////////////private InputParser parser;
    //use InputParser only when testing
    // End of variables declaration                   
}
