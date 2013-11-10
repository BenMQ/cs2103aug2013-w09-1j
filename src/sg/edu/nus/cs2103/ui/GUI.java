package sg.edu.nus.cs2103.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.Constants;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author LiuDake A0105656
 */
public class GUI extends javax.swing.JFrame implements NativeKeyListener {

	/*TEST FOR FUN*/

    private static class MyTextPane extends JTextPane {
        public MyTextPane() {
            super();
            setOpaque(false);
            // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
            setBackground(Color.BLACK);
        }
        @Override
        protected void paintComponent(Graphics g) {
            // set background green - but can draw image here too
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(GUIConstants.BACKGROUND_IMAGE_NAME));
            } catch (IOException e) {
            }
            g.drawImage(img, 0, 0, this);

            super.paintComponent(g);
        }
    }
	
    /**
     * The following code creates new GUI.
     * MainTextPane is the main text displaying area which is static so that other static objects can call it.
     */
	static public MyTextPane MainTextPane = new MyTextPane();
	
    /**
     * StyledDocument stores all text layouts for MainTextPane.
     */
	
	static public StyledDocument styledDoc = MainTextPane.getStyledDocument();
	
	/**
     * print_add is for logic part to send display message(string and color code) to GUI.
     */
	
	static public String print_add(String ipt, int colorCode){
		System.out.print(ipt);
		switch (colorCode){
		case GUIConstants.COLOR_CODE_GREEN:
			useStyle(styledDoc,ipt,"Green");
			return ipt;
		case GUIConstants.COLOR_CODE_YELLOW:
			useStyle(styledDoc,ipt,"Yellow");
			return ipt;
		case GUIConstants.COLOR_CODE_BLUE:
			useStyle(styledDoc,ipt,"Blue");
			return ipt;
		case GUIConstants.COLOR_CODE_RED:
			useStyle(styledDoc,ipt,"Red");
			return ipt;
		case GUIConstants.COLOR_CODE_WHITE:
			useStyle(styledDoc,ipt,"White");
			return ipt;
		default:
			useStyle(styledDoc,ipt,"Green");
			return ipt;
		}
	}

	public static void addNewStyle(String style,StyledDocument doc,int size,int bold,int italic,int underline,Color color,String fontName)
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
	  //StyleConstants.setBackground(s, Color.BLACK);
	 }
	
	 public static void useStyle(StyledDocument styledDoc, String content,String currentStyle)
	 {
	  try {
	   styledDoc.insertString(styledDoc.getLength(),content,styledDoc.getStyle(currentStyle));
	  } catch (BadLocationException e) {
	   System.err.println("BadLocationException: " + e);
	  }
	 }
	 
	 /**
	  * rebuildStyle resets all styles for styledDoc.
	  */
	 private void rebuildStyle(){
	    	try {
				styledDoc.remove(0, styledDoc.getLength());
				//Remove default style if anything exists in styledDoc
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	StyledDocument styledDoc = MainTextPane.getStyledDocument();
			addNewStyle("Green",styledDoc,18,1,0,0,Color.GREEN,"OCR A Std");//main text
			addNewStyle("Yellow",styledDoc,18,1,0,0,new Color(254, 254, 125),"OCR A Std");//labels
			addNewStyle("Blue",styledDoc,18,1,1,0,new java.awt.Color(145, 192, 246),"OCR A Std");//command feedback
			addNewStyle("Red",styledDoc,18,1,0,0,new java.awt.Color(254, 100, 100),"OCR A Std");//Error
			addNewStyle("White",styledDoc,18,1,1,0,Color.WHITE,"OCR A Std");//ID and highlighted texts
	    }
	/**
	 * GUI class builder. Initialize logicHandler, build text styles, then initialize GUI components.
	 */	
    public GUI() {
		manager = TaskManager.getTaskManager();
		logicHandler = LogicHandler.getLogicHandler(manager, null);
		rebuildStyle();
		initComponents();
		this.setVisible(true);
    }
    
    /*
     * jNativeHook provides global keyboard listener for shortcut key.
     * 
     * */
    
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// .TODO Auto-generated method stub
		// To implement unimplemented method
	}
	
	
	//Detect whether TAB and Space are both pressed. If so then change visibility of GUI
	public void nativeKeyPressed(NativeKeyEvent e) {
		currentKey = e.getKeyCode();
		if (currentKey == GUIConstants.KEYBOARD_TAB) {
			TABPressed = true;
		}
		if (currentKey == GUIConstants.KEYBOARD_SPACE) {
			SpacePressed = true;
		}
		if (TABPressed && SpacePressed) {
			if (isVisibleGUI) {
				isVisibleGUI = false;
				toInvisible();
			} else {
				isVisibleGUI = true;
				toVisible();
			}
		}
		if (e.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
			GlobalScreen.unregisterNativeHook();
			System.exit(0);
		}
	}

	//If TAB or Space is released then change state of TABPressed and SpacePressed
	public void nativeKeyReleased(NativeKeyEvent e) {
		currentKey = e.getKeyCode();
		if (currentKey == GUIConstants.KEYBOARD_TAB) {
			TABPressed = false;
		}
		if (currentKey == GUIConstants.KEYBOARD_SPACE) {
			SpacePressed = false;
		}
	}
	
	
	//Visibility
	public void toInvisible() {
		this.setState(Frame.ICONIFIED);
	}

	public void toVisible() {
		this.setState(Frame.NORMAL);
	}
	
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    
    private void initComponents() {
    	
    	//Initialize all GUI components
        jScrollPaneInput = new javax.swing.JScrollPane();
        jTextPaneInput = new javax.swing.JTextField();
        jTextAreaLogo = new javax.swing.JTextArea();
        jScrollPaneMainText = new javax.swing.JScrollPane();
        jScrollPaneFloating = new javax.swing.JScrollPane();
        jScrollPaneLogo = new javax.swing.JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//Scrollbar invisible
        FloatingTextArea = new javax.swing.JTextArea();
        
        //previous command
        previousInput = new ArrayList<String>();
        
        //Set caret for FloatingTextArea to ALWAYS_UPDATE
		DefaultCaret caret = (DefaultCaret) FloatingTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //KeyListeners for keyboard actions
        jTextPaneInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				//previous command
				if (e.getKeyCode() == KeyEvent.VK_UP && previousInputPointer>=0){
					previousInputPointer--;
					jTextPaneInput.setText(previousInput.get(previousInputPointer+1));
				}
				//"next" command
				if (e.getKeyCode() == KeyEvent.VK_DOWN && previousInputPointer<previousInput.size()-1){
					previousInputPointer++;
					jTextPaneInput.setText(previousInput.get(previousInputPointer));
				}
				//execute
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String ipt = jTextPaneInput.getText();
					if(!ipt.equals("")){
						previousInput.add(ipt);
						previousInputPointer=previousInput.size()-1;
						rebuildStyle();
						String userInput =  jTextPaneInput.getText();
						jTextPaneInput.setText(null);
						logicHandler.executeCommand(userInput);
						try {
							FloatingTextArea.setText("Floating tasks:\n\n"+
									manager.displayFloatingTasks());
						} catch (IllegalStateException w) {
							FloatingTextArea
									.setText(Constants.MESSAGE_EMPTY_LIST);
						}
						}
					
				}
				
				//Add color change code here
				
			}
			
		});
        
        //GUI components properties and behavior
        jScrollPaneInput.setViewportView(jTextPaneInput);
        MainTextPane.setEditable(false);
        MainTextPane.setBackground(Color.BLACK);
        MainTextPane.setAutoscrolls(false);
        MainTextPane.setDragEnabled(false);
        MainTextPane.setFocusable(false);
        ((DefaultCaret) MainTextPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        jScrollPaneMainText.setViewportView(MainTextPane);
        FloatingTextArea.setEditable(false);
        FloatingTextArea.setColumns(20);
        FloatingTextArea.setRows(5);
        FloatingTextArea.setDragEnabled(false);
        FloatingTextArea.setFocusable(false);
        FloatingTextArea.setFont(new java.awt.Font(
				"Courier", 0, 14));
        FloatingTextArea.setBackground(Color.BLACK);
        FloatingTextArea.setForeground(Color.GREEN);
        jScrollPaneFloating.setViewportView(FloatingTextArea);
        jTextAreaLogo.setEditable(false);
        jTextAreaLogo.setBackground(new java.awt.Color(0, 0, 0));
        jTextAreaLogo.setForeground(Color.GREEN);
        jTextAreaLogo.setColumns(20);
        jTextAreaLogo.setRows(5);
        jTextAreaLogo.setFocusable(false);
        jScrollPaneLogo.setViewportView(jTextAreaLogo);
        jTextAreaLogo.setText(GUIConstants.LOGO);
        jTextAreaLogo.setFont(new java.awt.Font(
				"Courier", 1, 13));
        
        //Welcome message
        if (manager.isReloaded()) {
			GUI.print_add((Constants.MESSAGE_WELCOME_TO_SUDO_RELOAD_A),GUIConstants.COLOR_CODE_GREEN);
			GUI.print_add((Constants.MESSAGE_WELCOME_TO_SUDO_RELOAD_B),GUIConstants.COLOR_CODE_WHITE);
			//GUI.print_add((UIConstants.MESSAGE_BETTER_ON_MAC),4);
		} else {
			GUI.print_add((Constants.MESSAGE_WELCOME_TO_SUDO_FIRST_A),GUIConstants.COLOR_CODE_GREEN);
			GUI.print_add((Constants.MESSAGE_WELCOME_TO_SUDO_FIRST_B),GUIConstants.COLOR_CODE_WHITE);
		}
        
        //Floating task area
		try {
			FloatingTextArea.setText("Floating tasks:\n\n"+
					manager.displayFloatingTasks());
		} catch (IllegalStateException e) {
			FloatingTextArea.setText(e.getMessage());
		}
		
		//Layout setup - do not modify
	    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneInput)
            .add(layout.createSequentialGroup()
                .add(jScrollPaneMainText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 940, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneFloating, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneLogo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPaneMainText)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPaneLogo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPaneFloating, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 579, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPaneInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        pack();
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
        } catch (NativeHookException ex) {
			System.err
			.println("There was a problem registering the native hook.");
	System.err.println(ex.getMessage());
	System.exit(1);
}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GlobalScreen.getInstance().addNativeKeyListener(new GUI());
            }
        });
    }
    
    //variables
	private int currentKey;
	private Boolean TABPressed = false;
	private Boolean SpacePressed = false;
	private Boolean isVisibleGUI = true;
	private TaskManager manager;
	private LogicHandler logicHandler;
    private javax.swing.JTextArea FloatingTextArea;
    private javax.swing.JScrollPane jScrollPaneMainText;
    private javax.swing.JScrollPane jScrollPaneFloating;
    private javax.swing.JScrollPane jScrollPaneLogo;
    private javax.swing.JScrollPane jScrollPaneInput;
    private javax.swing.JTextField jTextPaneInput;
    private javax.swing.JTextArea jTextAreaLogo;
    private ArrayList<String> previousInput;
    private int previousInputPointer;
    // End of variables declaration                   
}
