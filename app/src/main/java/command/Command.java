package command;

import java.util.ArrayList;

public class Command extends ArrayList<Integer> {

	private static final long serialVersionUID = 6781377494867334709L;
	private String target = null;

	public Command(ArrayList<Integer> a) {
		this.addAll(a);
	}
	
	public Command(String cmd, String target) {
		for (char c: cmd.toCharArray()) {
			if (c == Encoder.charUP)    add(Touch.UP);
			if (c == Encoder.charDOWN)  add(Touch.DOWN);
			if (c == Encoder.charLEFT)  add(Touch.LEFT);
			if (c == Encoder.charRIGHT) add(Touch.RIGHT);
		}
		
		this.target = target;
	}

	public String touchesToString() {
		StringBuilder result = new StringBuilder();
		
		for(Object o : toArray()) {
			switch ((Integer)o) {
			case Touch.UP    : result.append(Encoder.charUP);    break;
			case Touch.DOWN  : result.append(Encoder.charDOWN);  break;
			case Touch.LEFT  : result.append(Encoder.charLEFT);  break;
			case Touch.RIGHT : result.append(Encoder.charRIGHT); break;
			}
		}
		
		return result.toString();
	}
	
	@Override
	public String toString() {
		return touchesToString()+"-"+target;
	}

	public void add(int t) {
		super.add(t);
	}
	
	public String getTarget() {
		return target;
	}	
}
