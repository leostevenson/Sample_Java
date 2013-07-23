Name:	Fan Zhang
Email:	fzhang15@iit.edu
Phone:	312-395-0422
School:	IIT

Brief overview:
First of all, define a string array to every specific button, like {"A","B","C"} to button 1, circular display the character in this array by applying a mod function. It means all the Characters in the array could be output through pressing this button repeatedly.

After that, define the action for each button in the dial panel, like what's gonna happen if we press button 1, to remember the button that we pressed last time, a tag named as "lastChicked" is defined, also, we need to know how many times this button was clicked before, therefore, another tag like "clickCount" is necessary.

Then, it's time to check out the output, to achieve that, I defined a string called content to receive all the characters that might be input, it's public, since we could invoke it from some other class, like a Jframe class if we wanna build a GUI for it. 

All the contents that I mentioned before could be found in "src\dialPanel\DataProcess.java" 

Finally, to be convenient, I'm gonna use Junit to test the class that I've done, the test class could be located in "src\dialPanel\DataProcessTest.java", you can compile and run it to confirm if the output is right. 
