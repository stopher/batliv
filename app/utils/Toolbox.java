package utils;

import java.util.Random;

public class Toolbox {

	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static int winningNumber(int num1, int num2, int num3) {
		int randInt = Toolbox.randInt(0, 100);		
		if(randInt < 33) {
			return num1;
		} else if(randInt < 66) {
			return num2;
		} else {
			return num3;
		}
	}
	
	public static int bonusPoints(int mod) {
		int modifier = mod;
		if(mod > 3) {
			modifier = mod*mod;
		}
		int randInt = Toolbox.randInt(0, 1000*modifier);
		return randInt;
	}
}
