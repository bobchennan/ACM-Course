package cnx.env;

import cnx.translate.Level;
import cnx.temp.Label;

public class Constants {
	public static Level now = new Level();
	public static Label exit;
	public static final int intSize = 4;
	public static final int charSize = 4;
	public static final int pointerSize = 4;
	public static final String top_level = "PROGRAM";
	
	public static final String[] bopStr = {
		"+", "-", "*", "/", "%", "|", "^", "&", "==", "!=", "<", "<=", ">", ">=", "||", "&&", "<<", ">>"
	};
	
	public static final String[] sopStr = {
		"-", "&", "*", "~", "!", "++", "--"
	};
	
	public static int baseOfSavedRegisters = 8;	// start from $t0
	public static int numOfSavedRegisters = 18;
	public static final int paramRegBase = 4;	// start from $a0
	public static final int paramRegNum  = 4;	// $a0-$a3
	public static final String[] regNames = {
		"zero", "at",
		"v0", "v1",
		"a0", "a1", "a2", "a3",
		"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
		"s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
		"t8", "t9",
		"k0", "k1",
		"gp", "sp", "fp", "ra",
		"spill"	// special register
	};
	
	public static final int spillReg = regNames.length - 1;
}
