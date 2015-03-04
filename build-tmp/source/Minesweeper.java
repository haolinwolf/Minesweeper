import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;//Declare and initialize NUM_ROWS and NUM_COLS = 20
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined
public boolean gameOver = false;
public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    
    buttons = new MSButton [NUM_ROWS] [NUM_COLS];
    for(int r=0;  r < NUM_ROWS; r++)
    {
        for(int c =0; c < NUM_COLS; c++)
        {
            buttons[r][c] = new MSButton(r,c);
        }
    }
    //declare and initialize buttons
    setBombs();
}
public void setBombs()
{
    while(bombs.size() < 50)
    {
      int row = (int)(Math.random()*NUM_ROWS);
      int col = (int)(Math.random()*NUM_COLS);
      if (!bombs.contains(buttons[row][col]))
          bombs.add(buttons[row][col]);
    }          //your code
}

public void draw()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    for (int r = 0; r < NUM_ROWS; r++)
    {
        for (int c = 0; c < NUM_COLS; c++)
        {
            if(!bombs.contains(buttons[r][c]) && !buttons[r][c].isClicked())
            {
                return false;
            }
        }
    } //your code here
    return true;
}
public void displayLosingMessage()
{

    gameOver = true;
    for (int r = 0; r < NUM_ROWS; r++)
    {
        for (int c = 0; c < NUM_COLS; c++)
        {
            if(bombs.contains(buttons[r][c]))
                buttons[r][c].setLabel("B");
        }
    }  
 String sign = new String ("GAME OVER!");
    for (int i = 0; i < sign.length(); i++)
    { 
       buttons[10][5+i].setLabel(sign.substring(i, i+1));
    }
}
public void displayWinningMessage()
{ 
    String sign = new String ("YOU WON!");
    for (int i = 0; i < sign.length(); i++)
    {
       buttons[10][6+i].setLabel(sign.substring(i, i+1));
    }
//your code here
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        
        if (gameOver || isWon())
            return; //your code here
        if ( mouseButton == LEFT)
        { 
           clicked = true;
        }
        if (mouseButton == RIGHT && isClicked())
        {
           marked = !marked;
        }
        else if (bombs.contains(this) && !marked) 
        {
            gameOver=true;
            displayLosingMessage();
        }
        else if (countBombs(r,c) > 0)
        {
            label = "" + countBombs(r,c);
        }
        else  
        {
            if(isValid(r, c-1) && buttons[r][c-1].clicked == false)
            {
                buttons[r][c-1].mousePressed();
            }
            if(isValid(r, c+1) && buttons[r][c+1].clicked == false)
            {
                buttons[r][c+1].mousePressed();
            }
            if(isValid(r-1, c) && buttons[r-1][c].clicked == false)
            {
                buttons[r-1][c].mousePressed();
            }
            if(isValid(r+1, c) && buttons[r+1][c].clicked == false)
            {
                buttons[r+1][c].mousePressed();
            }
             if(isValid(r+1, c+1) && buttons[r+1][c+1].clicked == false)
            {
                buttons[r+1][c+1].mousePressed();
            }
             if(isValid(r-1, c-1) && buttons[r-1][c-1].clicked == false)
            {
                buttons[r-1][c-1].mousePressed();
            }
             if(isValid(r+1, c-1) && buttons[r+1][c-1].clicked == false)
            {
                buttons[r+1][c-1].mousePressed();
            }
             if(isValid(r-1, c+1) && buttons[r-1][c+1].clicked == false)
            {
                buttons[r-1][c+1].mousePressed();
            }

        }

    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
             fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if(r >= 0 && r < NUM_ROWS && c >= 0 && c < NUM_COLS)
         {
             return true;
         }   
        //your code here
        return false;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        if(isValid(r, c+1) && bombs.contains(buttons[r][c+1]))
            numBombs++;
        if(isValid(r, c-1) && bombs.contains(buttons[r][c-1]))
           numBombs++; //your code here
        if(isValid(r+1, c) && bombs.contains(buttons[r+1][c]))
            numBombs++;
        if(isValid(r-1, c) && bombs.contains(buttons[r-1][c]))
            numBombs++;
        if(isValid(r-1, c-1) && bombs.contains(buttons[r-1][c-1]))
            numBombs++;
        if(isValid(r+1, c+1) && bombs.contains(buttons[r+1][c+1]))
            numBombs++;
        if(isValid(r-1, c+1) && bombs.contains(buttons[r-1][c+1]))
            numBombs++;
        if(isValid(r+1, c-1) && bombs.contains(buttons[r+1][c-1]))
            numBombs++;

        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
