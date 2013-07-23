package dialPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

public class DataProcess {
	public String content = "";
	private int lastClicked = 0;
	private int clickCount = 0;
	private ArrayList<String []> al = new ArrayList<String []>();
		
	/**
	 * Constructor
	 * Button 1: DialPanel Button 1
	 * Button 2: DialPanel Button 2
	 * ...
	 * Button 9: DialPanel Button 9
	 * Button 10: DialPanel Button *
	 * Button 11: DialPanel Button 0
	 * Button 12: DialPanel Button #
	 * 
	 * the timer will be used to detect the pause when user input, the delay will be set as 3 seconds.
	 */
	DataProcess(){		
		final String[] sBtn2 = {"A","B","C"};
		final String[] sBtn3 = {"D","E","F"};
		final String[] sBtn4 = {"G","H","I"};
		final String[] sBtn5 = {"J","K","L"};
		final String[] sBtn6 = {"M","N","O"};
		final String[] sBtn7 = {"P","Q","R","S"};
		final String[] sBtn8 = {"T","U","V"};
		final String[] sBtn9 = {"W","X","Y","Z"};
		
		al.add(null);
		al.add(null);
		al.add(sBtn2);
		al.add(sBtn3);
		al.add(sBtn4);
		al.add(sBtn5);
		al.add(sBtn6);
		al.add(sBtn7);
		al.add(sBtn8);
		al.add(sBtn9);
	}
    
	public void Input(char c){
		switch (c){
			case '1':
				confirmCharacter();
				break;
			case '*':
				confirmCharacter();
				break;
			case '0':
				inputSpace();
				break;
			case ' ':
				inputSpace();
				break;
			case '#':
				deleteLast();
				break;
			default:
				int n = Integer.parseInt(Character.toString(c));
				selectCharacter(n);
		}
	}
	
	/**
	 * Confirm the input of last character
	 * this function will be invoked when user clicked Dial Panel button 1 and *
	 */
	private void confirmCharacter(){
		if (lastClicked > 0){
			String[] sBtn = al.get(lastClicked);
			String c = sBtn[clickCount % sBtn.length]; 
			lastClicked = 0;
			clickCount = 0;
		
			if (content.length() > 0)
				content = content.substring(0,content.length()-1);
			content += c;
		}
	}
	
	/**
	 * Delete last character in screen
	 * this function will be invoked when user clicked Dial Panel button #
	 */
	private void deleteLast(){
		if (content.length() > 0)
			content = content.substring(0,content.length()-1);
		lastClicked = 0;
		clickCount = 0;
		
		return;
	}
	
	/*
	 * Input a space in the screen
	 * this function will be invoked when user clicked Dial Panel button 0
	 */
	private void inputSpace(){
		if (lastClicked != 0)
			Input('1');
		
		content += " ";
		
		return;
	}
	
	/**
	 * This is the character selecting function, 3-4 characters could be selected through 1 button iteratively
	 * @param n
	 */
	private void selectCharacter(int n){
		String character = new String();
		character = getNext(n);		
		if (content.length() > 0 && clickCount > 0)
			content = content.substring(0,content.length()-1);
	
		content += character;
		
		return;
	}
	
	private String getNext(int n){
		String[] sBtn;
		
		if (lastClicked != n && lastClicked > 0)
			Input('1');
		
		if (lastClicked == n)
			clickCount += 1;
		else if (lastClicked == 0)
			lastClicked = n;
		
		sBtn = al.get(n);
		return sBtn[clickCount % sBtn.length]; 
	}
}
