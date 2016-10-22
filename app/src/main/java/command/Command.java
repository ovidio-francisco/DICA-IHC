package command;

import java.util.ArrayList;

public class Command extends ArrayList<Integer> {

	private static final long serialVersionUID = 6781377494867334709L;
	private String target = null;
	private String display = null;

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

	public Command(String cmd, String target, String display) {
		for (char c: cmd.toCharArray()) {
			if (c == Encoder.charUP)    add(Touch.UP);
			if (c == Encoder.charDOWN)  add(Touch.DOWN);
			if (c == Encoder.charLEFT)  add(Touch.LEFT);
			if (c == Encoder.charRIGHT) add(Touch.RIGHT);
		}

		this.target = target;
		this.display = display;
	}

	public String touchesToString(int start) {
		StringBuilder result = new StringBuilder();
		
		for(int i=start; i<size();i++) {
			switch (get(i)) {
			case Touch.UP    : result.append(Encoder.STR_UP);    break;
			case Touch.DOWN  : result.append(Encoder.STR_DOWN);  break;
			case Touch.LEFT  : result.append(Encoder.STR_LEFT);  break;
			case Touch.RIGHT : result.append(Encoder.STR_RIGHT); break;
			}
		}
		
		return result.toString();
	}
	
	@Override
	public String toString() {
		return touchesToString(0) + (display==null?target:display);
	}

	public String toString(int start){
		return touchesToString(start)+(display==null?target:display);
	}

	public void add(int t) {
		super.add(t);
	}
	
	public String getTarget() {
		return target;
	}

	public String getDisplay() {
		return display;
	}
}
