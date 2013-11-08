package sg.edu.nus.cs2103.ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.UIConstants;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Liu Dake
 */










import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;


/**
 *
 * @author LiuDake
 */
public class MainFrame extends javax.swing.JFrame implements NativeKeyListener {

    /**
     * Creates new form MainFrame
     */
	static public javax.swing.JTextPane MainTextPane = new javax.swing.JTextPane();
	static public StyledDocument styledDoc = MainTextPane.getStyledDocument();
	static public void print_add(String ipt, int colorCode){
		switch (colorCode){
		case 0:
			insertDoc(styledDoc,ipt,"Green");
			break;
		case 1:
			insertDoc(styledDoc,ipt,"Yellow");
			break;
		case 2:
			insertDoc(styledDoc,ipt,"Blue");
			break;
		case 3:
			insertDoc(styledDoc,ipt,"Red");
			break;
		case 4:
			insertDoc(styledDoc,ipt,"White");
			break;
		default:
			insertDoc(styledDoc,ipt,"Green");
		}
	}
	
    public MainFrame() {
		manager = TaskManager.getTaskManager();
		logicHandler = LogicHandler.getLogicHandler(manager, null);
		rebuildStyle();
		initComponents();
		toVisible();
    }
    
    private void rebuildStyle(){
    	try {
			styledDoc.remove(0, styledDoc.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//MainTextPane.setBackground(Color.WHITE);
    	StyledDocument styledDoc = MainTextPane.getStyledDocument();
		createStyle("Green",styledDoc,18,1,0,0,Color.GREEN,"OCR A Std");
		createStyle("Yellow",styledDoc,18,1,0,0,new Color(254, 254, 125),"OCR A Std");
		createStyle("Blue",styledDoc,18,1,1,0,new java.awt.Color(145, 192, 246),"OCR A Std");
		createStyle("Red",styledDoc,18,1,0,0,new java.awt.Color(254, 100, 100),"OCR A Std");
		createStyle("White",styledDoc,18,1,1,0,Color.WHITE,"OCR A Std");
    }
    
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// .TODO Auto-generated method stub
		// To implement unimplemented method
	}
	public void nativeKeyPressed(NativeKeyEvent e) {
		currentKey = e.getKeyCode();
		if (currentKey == UIConstants.KEYBOARD_TAB) {
			keyOne = true;
		}
		if (currentKey == UIConstants.KEYBOARD_SPACE) {
			keyTwo = true;
		}
		if (keyOne && keyTwo) {
			if (isShown) {
				isShown = false;
				toInvisible();
			} else {
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
		if (currentKey == UIConstants.KEYBOARD_TAB) {
			keyOne = false;
		}
		if (currentKey == UIConstants.KEYBOARD_SPACE) {
			keyTwo = false;
		}
	}
	
	
	public void toInvisible() {
		this.setVisible(false);
	}

	public void toVisible() {
		this.setVisible(true);
	}
	
	public static void createStyle(String style,StyledDocument doc,int size,int bold,int italic,int underline,Color color,String fontName)
	 {
	  Style sys = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
	  try { doc.removeStyle(style); } catch(Exception e) {}
	  
	  Style s = doc.addStyle(style,null); // add
	  StyleConstants.setFontSize(s,size); // size
	  StyleConstants.setBold(s,(bold==1)?true:false); // bold
	  StyleConstants.setItalic(s,(italic==1)?true:false); // italian
	  StyleConstants.setUnderline(s,(underline==1)?true:false); // downline
	  StyleConstants.setForeground(s,color); // color
	  StyleConstants.setFontFamily(s,fontName);  // font
	  StyleConstants.setBackground(s, Color.BLACK);
	 }
	
	 public static void insertDoc(StyledDocument styledDoc, String content,String currentStyle)
	 {
	  try {
	   styledDoc.insertString(styledDoc.getLength(),content,styledDoc.getStyle(currentStyle));
	  } catch (BadLocationException e) {
	   System.err.println("BadLocationException: " + e);
	  }
	 }
	
	
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	//to take over the output
    	
    	
    	outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
        jScrollPaneInput = new javax.swing.JScrollPane();
        jTextPaneInput = new javax.swing.JTextField();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        FloatingTextArea = new javax.swing.JTextArea();
		DefaultCaret caret = (DefaultCaret) FloatingTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTextPaneInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextPaneInputKeyPressed(evt);
            }
        });
        
        jTextPaneInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					jScrollPane3.setAlignmentY(jScrollPane3.getAlignmentY()+10);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String ipt = jTextPaneInput.getText();
					if ( ipt.equals("demo")) {
						if (isDemo) {
							MainTextPane.setFont(new java.awt.Font("Monaco",
									0, 17));
							FloatingTextArea.setFont(new java.awt.Font(
									"Monaco", 0, 12));
							 jTextPaneInput.setText(null);
							isDemo = false;
						} else {
							MainTextPane.setFont(new java.awt.Font("Monaco",
									0, 25));
							FloatingTextArea.setFont(new java.awt.Font(
									"Monaco", 0, 16));
							// mainTextFrame.setText("Font:Demo mode on");
							 jTextPaneInput.setText(null);
							isDemo = true;
						}
					} else {
						rebuildStyle();
						String userInput =  jTextPaneInput.getText();
						jTextPaneInput.setText(null);
						logicHandler.executeCommand(userInput);
						if(isDemo){
						//MainTextPane.setText(outContent.toString());
						}
						//MainTextPane.updateUI();
						outContent.reset();
						//rebuildStyle();
						// String[] floatings =
						// updateFloating(manager.getFloatingTask());
						try {
							FloatingTextArea.setText(
									manager.displayFloatingTasks());
						} catch (IllegalStateException w) {
							FloatingTextArea
									.setText(UIConstants.MESSAGE_EMPTY_LIST);
						}
					}
				}
				
				//Add color change code here
				
			}
			
		});

        
        
        
        jScrollPaneInput.setViewportView(jTextPaneInput);

        MainTextPane.setEditable(false);
       // MainTextPane.setForeground(Color.BLACK);
        MainTextPane.setBackground(Color.BLACK);
        MainTextPane.setAutoscrolls(false);
        MainTextPane.setDragEnabled(false);
        MainTextPane.setFocusable(false);
        
        ((DefaultCaret) MainTextPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        jScrollPane1.setViewportView(MainTextPane);
        
        FloatingTextArea.setEditable(false);
        FloatingTextArea.setColumns(20);
        FloatingTextArea.setRows(5);
        FloatingTextArea.setDragEnabled(false);
        FloatingTextArea.setFocusable(false);
        FloatingTextArea.setFont(new java.awt.Font(
				"Courier", 0, 14));
        FloatingTextArea.setBackground(Color.BLACK);
        FloatingTextArea.setForeground(Color.GREEN);
        jScrollPane2.setViewportView(FloatingTextArea);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        
        jTextArea1.setForeground(Color.GREEN);
        
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea1);
        
        jTextArea1.setText(UIConstants.LOGO);
        jTextArea1.setFont(new java.awt.Font(
				"Courier", 1, 13));
        
        
        if (manager.isReloaded()) {
			MainFrame.print_add((UIConstants.MESSAGE_WELCOME_TO_SUDO_RELOAD),0);
			MainFrame.print_add((UIConstants.MESSAGE_BETTER_ON_MAC),4);
		} else {
			MainFrame.print_add((UIConstants.MESSAGE_WELCOME_TO_SUDO_FIRST),0);
		}
		try {
			FloatingTextArea.setText(manager.displayFloatingTasks());
		} catch (IllegalStateException e) {
			FloatingTextArea.setText(UIConstants.MESSAGE_EMPTY_LIST);
		}
		
	    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneInput)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 940, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 579, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPaneInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>                        

    private void jTextPaneInputKeyPressed(java.awt.event.KeyEvent evt) {                                          
    	
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
        	GlobalScreen.registerNativeHook();
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (NativeHookException ex) {
			System.err
			.println("There was a problem registering the native hook.");
	System.err.println(ex.getMessage());
	System.exit(1);
}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GlobalScreen.getInstance().addNativeKeyListener(new MainFrame());
            }
        });
    }
  
	private int currentKey;
	private Boolean keyOne = false;
	private Boolean keyTwo = false;
	private Boolean isShown = true;
	private Boolean isDemo = false;
	private ByteArrayOutputStream outContent;
	private TaskManager manager;
	private LogicHandler logicHandler;
    private javax.swing.JTextArea FloatingTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneInput;
    private javax.swing.JTextField jTextPaneInput;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration                   
}
