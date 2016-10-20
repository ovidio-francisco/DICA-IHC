package command;

import java.util.ArrayList;

public class Encoder {
	
	public enum Stage {DONE, WRONG, INCOMPLET};

	static ArrayList<Command> commandList = new ArrayList<>();
	
	public static final char charUP    = '^';
	public static final char charDOWN  = 'V';
	public static final char charLEFT  = '<';
	public static final char charRIGHT = '>';

	public static final String _A = "A";
	public static final String _B = "B";
	public static final String _C = "C";
	public static final String _D = "D";
	public static final String _E = "E";
	public static final String _F = "F";
	public static final String _G = "G";
	public static final String _H = "H";
	public static final String _I = "I";
	public static final String _J = "J";
	public static final String _K = "K";
	public static final String _L = "L";
	public static final String _M = "M";
	public static final String _N = "N";
	public static final String _O = "O";
	public static final String _P = "P";
	public static final String _Q = "Q";
	public static final String _R = "R";
	public static final String _S = "S";
	public static final String _T = "T";
	public static final String _U = "U";
	public static final String _V = "V";
	public static final String _X = "X";
	public static final String _Y = "Y";
	public static final String _W = "W";
	public static final String _Z = "Z";
	
	public static final Command command_A = new Command("><", _A);
	public static final Command command_E = new Command(">V", _E);
	public static final Command command_I = new Command(">^", _I);
	public static final Command command_B = new Command("<>V", _B);
	public static final Command command_C = new Command("<V<", _C);
	public static final Command command_D = new Command("<><", _D);
	public static final Command command_F = new Command("V><", _F);
	public static final Command command_G = new Command("VV>", _G);
	public static final Command command_H = new Command("<VV", _H);
	public static final Command command_J = new Command("<^<", _J);
	public static final Command command_K = new Command("V^^", _K);
	public static final Command command_L = new Command("V>V", _L);
	public static final Command command_M = new Command("V<>", _M);
	public static final Command command_N = new Command("V<<", _N);
	public static final Command command_O = new Command("<V>", _O);
	public static final Command command_P = new Command("V<V", _P);
	public static final Command command_Q = new Command("V<^", _Q);
	public static final Command command_R = new Command("<>^", _R);
	public static final Command command_S = new Command("<>>", _S);
	public static final Command command_T = new Command("V<<", _T);
	public static final Command command_U = new Command("V>>", _U);
	public static final Command command_V = new Command("VVV", _V);
	public static final Command command_X = new Command("<V^", _X);
	public static final Command command_Y = new Command("<^>", _Y);
	public static final Command command_W = new Command("VV^", _W);
	public static final Command command_Z = new Command("<^V", _Z);

	static {
		commandList.add(command_A);
		commandList.add(command_B);
		commandList.add(command_C);
		commandList.add(command_D);
		commandList.add(command_E);
		commandList.add(command_F);
		commandList.add(command_G);
		commandList.add(command_H);
		commandList.add(command_I);
		commandList.add(command_J);
		commandList.add(command_K);
		commandList.add(command_L);
		commandList.add(command_M);
		commandList.add(command_N);
		commandList.add(command_O);
		commandList.add(command_P);
		commandList.add(command_Q);
		commandList.add(command_R);
		commandList.add(command_S);
		commandList.add(command_T);
		commandList.add(command_U);
		commandList.add(command_V);
		commandList.add(command_X);
		commandList.add(command_Y);
		commandList.add(command_W);
		commandList.add(command_Z);
	}
	
	public static String find(Command c) {
		int i = commandList.indexOf(c);
		
		if (i >=0) {
			return commandList.get(i).getTarget();
		}
		
		return null;
	}

	public static ArrayList<Command> nextCommands (Command cmd) {
		ArrayList<Command> result = new ArrayList<>();
		
		for (Command c : commandList) {
			if (c.touchesToString().startsWith(cmd.touchesToString()))
				result.add(c);
		}
		
		return result;
	}
	
	public static Stage getStage(Command cmd) {

		if (commandList.contains(cmd))
			return Stage.DONE;

		for (Command c : commandList) {
			
			System.out.println(String.format("%s&%s", c, cmd));
			
			if (c.touchesToString().startsWith(cmd.touchesToString()))
				return Stage.INCOMPLET;
		}
		
		return Stage.WRONG;
	}
	
	
	
	
	public static ArrayList<Command> getCommandList() {
		return commandList;
	}
}
