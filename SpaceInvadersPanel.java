
import java.awt.*;


import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class SpaceInvadersPanel extends JPanel implements ChangeListener {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Game");
		SpaceInvadersPanel content = new SpaceInvadersPanel();
		window.setContentPane(content);
		window.setSize(800,600);
	    window.setLocation(400,100);
	    window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    window.setJMenuBar(menuBar); 
	    //window.setContentPane( new ContentPanel() );
	    window.setVisible(true);
	}
	
	public SpaceInvadersPanel() {
	      JPanel inner = new JPanel();
	    
	      JButton startGameButton = new JButton("Check Score");
	      ButtonHandler listener = new ButtonHandler();
	      startGameButton.addActionListener(listener);
	      colorSlider.addChangeListener(this);
	      colorChoice.addItem("Red");
	      colorChoice.addItem("Blue");
	      colorChoice.addItem("Green");
	      
	      JMenuItem drawCommand = new JMenuItem("Exit");   // Create a menu item.
	      drawCommand.addActionListener(exitListener);         // Add listener to menu item.
	      menuBar.add(drawCommand); 
	      
	      inner.add(startGameButton);
	      inner.add(showIns);
	      inner.add(colorSlider);
	      inner.add(colorChoice);
	      inner.add(new JTextArea("Enter Name: ", 1, 20));
	      this.setLayout(new BorderLayout());
	      this.add(inner, BorderLayout.SOUTH);
	      this.add(new ContentPanel(), BorderLayout.CENTER);
	      
	      

	      
	}
 
   /**
    * The init() method of the applet just sets the content pane
    * of the applet to be a panel of type ContentPane, a nested class
    * that is defined below, which does all the work.
    */
//	
//	
//   
//   public void init() {
//	      setContentPane( new ContentPanel() );
//      
//
//   }
   
   private static int score;
	private static int squareColor;
	private static JCheckBox showIns = new JCheckBox("Show Instructions");
	private static JSlider colorSlider = new JSlider(0, 255, 0);
	private static JComboBox colorChoice = new JComboBox();
	private static JMenuBar menuBar = new JMenuBar();
	
  
  private class ButtonHandler implements ActionListener {
		  public void actionPerformed(ActionEvent e) {
			  JOptionPane.showMessageDialog( null, "Score: " + score );
			  
	
		  }
  }
  
  public void stateChanged(ChangeEvent evt) {
	         squareColor = colorSlider.getValue();
	         //repaint();
	   }
  
  ActionListener exitListener = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
    	  System.exit(0);
      }
  };
   
   /**
    * Defines the content pane that is used in both the applet and
    * the stand-alone application version of this program.  The
    * panel displays a colored square that can be moved by the user,
    * by pressing the arrow keys.  The color of the square can be
    * changed by pressing the R, G, B, and K keys.  The panel also
    * displays a border and a message.
    */
   public static class ContentPanel extends JPanel
                  implements KeyListener, FocusListener, MouseListener {
      
         // (Note:  MouseListener is implemented only so that
         //         the applet can request the input focus when
         //         the user clicks on it.)
	  
      
      private static final int SQUARE_SIZE = 20;  // Length of side of square.
      
      
      private int squareTop, squareLeft, bulletLeft, bulletTop, alienTop, numAliens = 10, alienSpeed = 500; 
      
      private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, SPACING = 3;
      
      private int direction;
      
      private boolean gameOver;
      
      private Color alienColor;

      /**
       * The constructor sets the initial position and color of the square
       * and registers itself to act as a listener for Key, Focus, and 
       * Mouse events.
       */
      
      public ContentPanel() {
    	  
         squareTop = 400;  // Initial position of top-left corner of square.
         squareLeft = 400;
         bulletLeft = 1000;
         bulletTop = 100000;
         alienTop = 30;
         score = 0;
         
         setBackground(Color.WHITE);

         addKeyListener(this);     // Set up event listening.
         addFocusListener(this);
         addMouseListener(this);
         
         
      } // end init();
      
      
      /**
       * Draws a border, square, and message in the panel.  The message and
       * the color of  the border depend on whether or not the pane has
       * the input focus.
       */
      public void stateChanged(ChangeEvent evt) {
	         squareColor = colorSlider.getValue();
	         repaint();
	   }
      public void paintComponent(Graphics g) {
         
         super.paintComponent(g);  // Fills the panel with its
                                   // background color, which is black.

         int width = getSize().width;  // Width of the applet.
         int height = getSize().height; // Height of the applet.

         
         /* Draw the square. */
         
         Color finalColor = new Color(150, squareColor, squareColor);
         g.setColor(finalColor);
         g.fillRect(squareLeft, squareTop, SQUARE_SIZE, SQUARE_SIZE);
        
        g.setColor(Color.BLACK);
        g.fillRect(bulletLeft, bulletTop, 3, 10);
        
        if (colorChoice.getSelectedIndex() == 0)
        	alienColor = Color.RED;
        if (colorChoice.getSelectedIndex() == 1)
        	alienColor = Color.BLUE;
        if (colorChoice.getSelectedIndex() == 2)
        	alienColor = Color.GREEN;
        
	    g.setColor(alienColor);
	    for (int i = 0; i < numAliens; i++) {
	    	g.fillRect((i*60) + 100, alienTop, SQUARE_SIZE, SQUARE_SIZE);
	    }

         g.setColor(Color.magenta);
         if (showIns.isSelected())
    		 g.drawString("Arrow Keys Move Square, Space Button Fires, 10 Points For Every Kill",7,20);

         if (hasFocus() == false)
        	 g.drawString("Click to Start",7,35);

         
         if (gameOver) {
        	 g.drawString("Game Over ",650,35);
	         timerAlien.stop();
	    	 timerSquare.stop();
	    	 timerBullet.stop();
         }
         
         if (bulletTop <= alienTop) {
        	 for (int i = 0; i < numAliens; i++) {
        		 if (bulletLeft >= (i*60) + 100 && bulletLeft <= (i*60) + SQUARE_SIZE + 100  ) {
        			 score +=10;
	            	 numAliens--; 
	            	 timerBullet.stop();
	            	 bulletTop = 100000;
	            	 bulletLeft = 1000;
	            	 repaint();
	            	 break;
        		 }
        	 }
        	 
         }
         
         if (numAliens == 0) {
        	 alienTop = 30;
        	 numAliens = 10;
        	 g.setColor(Color.green);
 	         for (int i = 0; i < numAliens; i++) {
 	        	g.fillRect((i*60) + 100, alienTop, SQUARE_SIZE, SQUARE_SIZE);
 	        }
         }
        	 
      }  // end paintComponent()

      
      // ------------------- Event handling methods ----------------------

      /**
       * This will be called when the panel gains the input focus.  It just
       * calls repaint().  The panel will be redrawn with a cyan-colored border
       * and with a message about keyboard input.
       */
      public void focusGained(FocusEvent evt) {
    	 timerAlien.start();
    	 timerBullet.start();
         repaint();  // redraw with cyan border
      }
      
      
      /**
       * This will be called when the panel loses the input focus.  It just
       * calls repaint().  The panel will be redrawn with a gray-colored border
       * and with the message "Click to activate."
       */
      public void focusLost(FocusEvent evt) {
    	 timerAlien.stop();
//    	 timerSquare.stop();
    	 timerBullet.stop();
         repaint();  
      }
      
      
      /**
       * This method is called when the user types a character on the keyboard
       * while the panel has the input focus.  If the character is R, G, B, or K
       * (or the corresponding lower case characters), then the color of the
       * square is changed to red, green, blue, or black, respectively.
       */
      public void keyTyped(KeyEvent evt) {
    	  int key = evt.getKeyCode();  // keyboard code for the pressed key
          
          char ch = evt.getKeyChar(); // The character typed.
    	  
	    	  if (ch == KeyEvent.VK_SPACE) {
	              bulletLeft = squareLeft + 10;
	              bulletTop = squareTop - 20;
	              repaint();
	           
	    	  }
	    	  timerBullet.start();
         
       
     }  // end keyTyped()
      
      
      /**
       * This is called each time the user presses a key while the panel has
       * the input focus.  If the key pressed was one of the arrow keys,
       * the square is moved (except that it is not allowed to move off the
       * edge of the panel).
       */
      public void keyPressed(KeyEvent evt) { 
         
         int key = evt.getKeyCode();  // keyboard code for the pressed key
         
         char ch = evt.getKeyChar();
         
         if (key == KeyEvent.VK_LEFT) {  // left arrow key
        	direction = LEFT;
            
         }
         else if (key == KeyEvent.VK_RIGHT) {  // right arrow key
        	 direction = RIGHT;
            
         }
         else if (key == KeyEvent.VK_UP) {  // up arrow key
        	 direction = UP;
        	    
         }
         else if (key == KeyEvent.VK_DOWN) {  // down arrow key
        	 direction = DOWN;

         }
    	 timerSquare.start();
    	 
      }  // end keyPressed()
      
      /**
       * This is called each time the user releases a key while the panel
       * has the input focus.  In this class, it does nothing, but it is
       * required to be here by the KeyListener interface.
       */
      public void keyReleased(KeyEvent evt) {
      }
      
      
      /**
       * This is called when the user clicks the panel with the mouse.
       * It just requests that the input focus be given to this panel.
       */
      public void mousePressed(MouseEvent evt) {
         requestFocus();       
      }   
      
      
      ActionListener moveAlien = new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	    	  alienTop = alienTop + 10;
	    	  repaint();
	    	  
	    	  if (alienTop >= 505) {
	    		  timerSquare.stop();
	    	      timerAlien.stop();
	    	      timerBullet.stop();
	    	      gameOver = true;
	    	  }
	      }
      };
      
      ActionListener moveSquare = new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	    
	    	  switch(direction) {
	    	  case LEFT:
		    	  if (squareLeft > 0) 
		    		  squareLeft = squareLeft - SPACING;
		          repaint();
		          break;
		    	  
		          
	    	  case RIGHT:
	    		  if (squareLeft < 765) 
	    			  squareLeft = squareLeft + SPACING;
	              repaint();
	              break;
	              
	    	  case UP:
	    		  if (squareTop > alienTop + 20)
	    			  squareTop = squareTop - SPACING;
	    		  else
	    			  gameOver = true;
	              repaint();
	              break;
	              
	    	  case DOWN:
	    		  if (squareTop < 505)
	    		  squareTop = squareTop + SPACING;
	              repaint();
	              break;
	    	  }
	    	  
	    		  
	      }
      };
      
      ActionListener moveBullet = new ActionListener() {
	      public void actionPerformed(ActionEvent evt) {
	    	  bulletTop = bulletTop - 8;
	    	  repaint();
	      }
      };

     
      Timer timerSquare = new Timer(3, moveSquare);
      Timer timerAlien = new Timer(alienSpeed, moveAlien);
      Timer timerBullet = new Timer(1, moveBullet);
      
      
      public void mouseEntered(MouseEvent evt) { }  // Required by the
      public void mouseExited(MouseEvent evt) { }   //    MouseListener
      public void mouseReleased(MouseEvent evt) { } //       interface.
      public void mouseClicked(MouseEvent evt) { }
      
   } // end nested class ContentPanel
  

   
} // end class spaceinvaders