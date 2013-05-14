package cnx.translate;

import java.util.TreeSet;

import cnx.assem.*;
import cnx.env.Constants;
import cnx.temp.*;

public class Level{
	
	public Level parent = null;
	public Label label = null;
	
	public Level() {
		label = new Label(Constants.top_level);
	}
	
	public Level(Level parentLevel, String name, Label label) {
		parent = parentLevel;
		this.label = label;
	}
	
	private int numOfLocals = 0;
	
	public Temp newLocal() {
		return new Temp(this, numOfLocals++);
	}

	public int localSize(){
		return numOfLocals;
	}
	
	public int frameSize() {
		return Constants.pointerSize * (1 + 18 + numOfLocals);
	}
	
	public TreeSet<Integer> usedRegisters = new TreeSet<Integer>();

	public void useRegister(int r) {
		usedRegisters.add(r);
	}

	public AssemList saveRegisters() {
		AssemList saves = null;
		for (int i = 0; i < Constants.numOfSavedRegisters; i++) {
			int r = Constants.baseOfSavedRegisters + i;
			if (usedRegisters.contains(r)) {
				saves = new AssemList(
						new Assem("sw $%, %($sp)", Constants.regNames[r], (numOfLocals + i) * Constants.pointerSize),
						saves);
			}
		}
		return saves;
	}

	public AssemList loadRegisters() {
		AssemList loads = null;
		for (int i = 0; i < Constants.numOfSavedRegisters; i++) {
			int r = Constants.baseOfSavedRegisters + i;
			if (usedRegisters.contains(r)) {
				loads = new AssemList(
						new Assem("lw $%, %($sp)", Constants.regNames[r], (numOfLocals + i) * Constants.pointerSize),
						loads);
			}
		}
		return loads;
	}
}
