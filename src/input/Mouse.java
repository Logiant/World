package input;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.BufferUtils;

/**
 * A mouse class implementation for a single active window
 * Does not calculate the mouse DX if the cursor is not grabbed
 * @author Logiant
 *
 */
public class Mouse {

	//static mouse state for the active window
	public static float DX;
	public static float DY;
	public static float X;
	public static float Y;
	
	public static boolean LeftDown;
	public static boolean RightDown;

	//doublebuffers for grabbing mouse and screen information
	DoubleBuffer xPos;
	DoubleBuffer yPos;
	IntBuffer screenWidth;
	IntBuffer screenHeight;
	
	//last mouse position
	double lastX;
	double lastY;
	
	//current window/context ID
	long window;
		
	public void initialize(long window) {
		//create the mouse position doublebuffers
		xPos = BufferUtils.createDoubleBuffer(1);
		yPos = BufferUtils.createDoubleBuffer(1);
		//create screen size intbuffers
		screenWidth = BufferUtils.createIntBuffer(1);
		screenHeight = BufferUtils.createIntBuffer(1);
		//active window ID
		this.window = window;
	}
	
	public void update() {
		//rewind the position buffers to recieve fresh data
        xPos.rewind(); yPos.rewind();
   		//get the cursor position
        glfwGetCursorPos(window, xPos, yPos);
        //get last cursor position
        lastX = xPos.get(0);
        lastY = yPos.get(0);
        
        Mouse.DX = (Mouse.X - (float)lastX);
        Mouse.DY = (Mouse.Y - (float)lastY);

        
        //convert to floats for static usage
        Mouse.X = (float) lastX;
        Mouse.Y = (float) lastY;
        
                
        Mouse.LeftDown = glfwGetMouseButton(window, 0) == 1; 
        Mouse.RightDown = glfwGetMouseButton(window, 1) == 1; 

        //if the cursor is grabbed
		if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
			//get screen size
			screenWidth.rewind(); screenHeight.rewind();
			glfwGetWindowSize(window, screenWidth, screenHeight);
			double width = screenWidth.get(0); double height = screenHeight.get(0);
			//calculate DX, DY
			Mouse.DX = (float) (lastX - width/2);
			Mouse.DY = (float) (lastY - height/2);
			//reset cursor to center of screen
            glfwSetCursorPos(window, width/2, height/2);
		}
        //convert to floats for static usage
        Mouse.X = (float) lastX;
        Mouse.Y = (float) lastY;
	}
}
