/* The following code was generated by JFlex 1.4.3 on 13-3-27 下午11:08 */

package cnx.syntactic;

import java.io.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 13-3-27 下午11:08 from the specification file
 * <tt>cnx.flex</tt>
 */
class Yylex implements java_cup.runtime.Scanner, Symbols {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYCOMMENT = 2;
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\10\1\1\1\0\1\10\1\2\22\0\1\10\1\62\1\64"+
    "\1\0\1\3\1\60\1\53\1\65\1\40\1\41\1\12\1\56\1\43"+
    "\1\57\1\63\1\11\1\5\11\4\1\0\1\42\1\54\1\44\1\55"+
    "\2\0\27\7\1\6\2\7\1\47\1\0\1\50\1\52\1\3\1\0"+
    "\1\26\1\35\1\24\1\17\1\16\1\20\1\7\1\25\1\23\1\7"+
    "\1\36\1\33\1\7\1\30\1\22\1\15\1\7\1\27\1\31\1\13"+
    "\1\32\1\21\1\34\1\6\1\14\1\37\1\45\1\51\1\46\1\61"+
    "\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\2\2\1\3\2\4\1\5\1\6\13\3"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\4\1\1\0\1\33\1\34"+
    "\1\35\1\36\4\3\1\37\11\3\1\40\1\41\1\42"+
    "\1\43\1\44\1\45\1\46\1\47\1\50\1\51\1\52"+
    "\1\53\1\54\1\55\1\56\1\57\1\60\2\0\1\61"+
    "\1\0\1\62\1\63\1\4\2\3\1\64\1\3\1\65"+
    "\10\3\1\66\1\67\1\70\1\71\1\3\1\72\1\73"+
    "\1\3\1\74\13\3\1\75\1\76\1\77\2\3\1\100"+
    "\1\101\1\102\1\103\1\3\1\104";

  private static int [] zzUnpackAction() {
    int [] result = new int[132];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\66\0\154\0\154\0\242\0\330\0\u010e\0\u0144"+
    "\0\u017a\0\u01b0\0\u01e6\0\u021c\0\u0252\0\u0288\0\u02be\0\u02f4"+
    "\0\u032a\0\u0360\0\u0396\0\u03cc\0\u0402\0\154\0\154\0\154"+
    "\0\154\0\u0438\0\154\0\154\0\154\0\154\0\u046e\0\u04a4"+
    "\0\u04da\0\u0510\0\u0546\0\u057c\0\u05b2\0\u05e8\0\154\0\u061e"+
    "\0\u0654\0\u068a\0\u06c0\0\u06f6\0\u072c\0\u0762\0\154\0\154"+
    "\0\154\0\154\0\u0798\0\u07ce\0\u0804\0\u083a\0\330\0\u0870"+
    "\0\u08a6\0\u08dc\0\u0912\0\u0948\0\u097e\0\u09b4\0\u09ea\0\u0a20"+
    "\0\154\0\154\0\154\0\154\0\154\0\154\0\154\0\u0a56"+
    "\0\154\0\u0a8c\0\154\0\154\0\154\0\154\0\154\0\154"+
    "\0\154\0\u0ac2\0\u068a\0\u068a\0\u0af8\0\154\0\154\0\u0762"+
    "\0\u0b2e\0\u0b64\0\330\0\u0b9a\0\330\0\u0bd0\0\u0c06\0\u0c3c"+
    "\0\u0c72\0\u0ca8\0\u0cde\0\u0d14\0\u0d4a\0\154\0\154\0\154"+
    "\0\154\0\u0d80\0\330\0\330\0\u0db6\0\330\0\u0dec\0\u0e22"+
    "\0\u0e58\0\u0e8e\0\u0ec4\0\u0efa\0\u0f30\0\u0f66\0\u0f9c\0\u0fd2"+
    "\0\u1008\0\330\0\330\0\330\0\u103e\0\u1074\0\330\0\330"+
    "\0\330\0\330\0\u10aa\0\330";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[132];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\6\1\7\1\10\2\6\1\4"+
    "\1\11\1\12\1\13\2\6\1\14\1\6\1\15\1\16"+
    "\1\6\1\17\1\20\2\6\1\21\1\6\1\22\1\23"+
    "\1\6\1\24\1\25\2\6\1\26\1\27\1\30\1\31"+
    "\1\32\1\33\1\34\1\35\1\36\1\37\1\40\1\41"+
    "\1\42\1\43\1\44\1\45\1\46\1\47\1\50\1\51"+
    "\1\52\1\53\11\3\1\54\1\55\53\3\67\0\1\4"+
    "\67\0\5\6\3\0\25\6\32\0\2\7\64\0\2\7"+
    "\1\56\71\0\1\57\31\0\1\60\32\0\1\61\32\0"+
    "\1\62\24\0\5\6\3\0\1\6\1\63\23\6\31\0"+
    "\5\6\3\0\20\6\1\64\4\6\31\0\5\6\3\0"+
    "\7\6\1\65\15\6\31\0\5\6\3\0\7\6\1\66"+
    "\15\6\31\0\5\6\3\0\5\6\1\67\7\6\1\70"+
    "\7\6\31\0\5\6\3\0\7\6\1\71\2\6\1\72"+
    "\12\6\31\0\5\6\3\0\3\6\1\73\21\6\31\0"+
    "\5\6\3\0\1\74\7\6\1\75\14\6\31\0\5\6"+
    "\3\0\15\6\1\76\7\6\31\0\5\6\3\0\12\6"+
    "\1\77\12\6\31\0\5\6\3\0\14\6\1\100\10\6"+
    "\72\0\1\101\65\0\1\102\4\0\1\103\60\0\1\104"+
    "\65\0\1\105\6\0\1\106\56\0\1\107\7\0\1\110"+
    "\55\0\1\111\10\0\1\112\54\0\1\113\11\0\1\114"+
    "\53\0\1\115\10\0\1\116\1\0\1\117\52\0\1\120"+
    "\65\0\1\121\104\0\1\122\2\0\1\123\1\0\62\123"+
    "\1\124\1\123\1\125\1\0\64\125\12\0\1\126\64\0"+
    "\1\127\60\0\4\130\3\0\25\130\31\0\5\6\3\0"+
    "\2\6\1\131\22\6\31\0\5\6\3\0\16\6\1\132"+
    "\6\6\31\0\5\6\3\0\14\6\1\133\10\6\31\0"+
    "\5\6\3\0\10\6\1\134\14\6\31\0\5\6\3\0"+
    "\1\135\24\6\31\0\5\6\3\0\15\6\1\136\7\6"+
    "\31\0\5\6\3\0\13\6\1\137\11\6\31\0\5\6"+
    "\3\0\1\140\24\6\31\0\5\6\3\0\14\6\1\141"+
    "\10\6\31\0\5\6\3\0\24\6\1\142\31\0\5\6"+
    "\3\0\10\6\1\143\14\6\31\0\5\6\3\0\10\6"+
    "\1\144\14\6\31\0\5\6\3\0\3\6\1\145\21\6"+
    "\72\0\1\146\65\0\1\147\104\0\1\150\67\0\1\151"+
    "\3\0\5\6\3\0\3\6\1\152\21\6\31\0\5\6"+
    "\3\0\3\6\1\153\21\6\31\0\5\6\3\0\4\6"+
    "\1\154\20\6\31\0\5\6\3\0\1\155\24\6\31\0"+
    "\5\6\3\0\14\6\1\156\10\6\31\0\5\6\3\0"+
    "\17\6\1\157\5\6\31\0\5\6\3\0\17\6\1\160"+
    "\5\6\31\0\5\6\3\0\3\6\1\161\21\6\31\0"+
    "\5\6\3\0\7\6\1\162\15\6\31\0\5\6\3\0"+
    "\20\6\1\163\4\6\31\0\5\6\3\0\13\6\1\164"+
    "\11\6\31\0\5\6\3\0\4\6\1\165\20\6\31\0"+
    "\5\6\3\0\10\6\1\166\14\6\31\0\5\6\3\0"+
    "\14\6\1\167\10\6\31\0\5\6\3\0\11\6\1\170"+
    "\13\6\31\0\5\6\3\0\7\6\1\171\15\6\31\0"+
    "\5\6\3\0\15\6\1\172\7\6\31\0\5\6\3\0"+
    "\3\6\1\173\21\6\31\0\5\6\3\0\23\6\1\174"+
    "\1\6\31\0\5\6\3\0\3\6\1\175\21\6\31\0"+
    "\5\6\3\0\15\6\1\176\7\6\31\0\5\6\3\0"+
    "\15\6\1\177\7\6\31\0\5\6\3\0\1\200\24\6"+
    "\31\0\5\6\3\0\5\6\1\201\17\6\31\0\5\6"+
    "\3\0\5\6\1\202\17\6\31\0\5\6\3\0\17\6"+
    "\1\203\5\6\31\0\5\6\3\0\3\6\1\204\21\6"+
    "\26\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4320];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\2\11\21\1\4\11\1\1\4\11\10\1\1\11"+
    "\6\1\1\0\4\11\16\1\7\11\1\1\1\11\1\1"+
    "\7\11\2\0\1\1\1\0\2\11\16\1\4\11\33\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[132];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
	private boolean commentCount = false;

	private void err(String message) {
		System.out.println("Scanning error in line " + yyline + ", column " + yycolumn + ": " + message);
	}

	private java_cup.runtime.Symbol tok(int kind) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn);
	}

	private java_cup.runtime.Symbol tok(int kind, Object value) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn, value);
	}


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Yylex(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  Yylex(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 142) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 50: 
          { commentCount=true;
          }
        case 69: break;
        case 38: 
          { System.out.println(yytext());return tok(LE);
          }
        case 70: break;
        case 5: 
          { System.out.println(yytext());return tok(DIVIDE);
          }
        case 71: break;
        case 2: 
          { /* skip */
          }
        case 72: break;
        case 33: 
          { System.out.println(yytext());return tok(ORASS);
          }
        case 73: break;
        case 43: 
          { System.out.println(yytext());return tok(INC);
          }
        case 74: break;
        case 15: 
          { System.out.println(yytext());return tok(RBRACK);
          }
        case 75: break;
        case 53: 
          { System.out.println(yytext());return tok(INT);
          }
        case 76: break;
        case 47: 
          { System.out.println(yytext());return tok(MODASS);
          }
        case 77: break;
        case 41: 
          { System.out.println(yytext());return tok(SHR);
          }
        case 78: break;
        case 37: 
          { System.out.println(yytext());return tok(AND);
          }
        case 79: break;
        case 25: 
          { System.out.println(yytext());return tok(NOT);
          }
        case 80: break;
        case 42: 
          { System.out.println(yytext());return tok(ADDASS);
          }
        case 81: break;
        case 8: 
          { System.out.println(yytext());return tok(RPAREN);
          }
        case 82: break;
        case 36: 
          { System.out.println(yytext());return tok(ANDASS);
          }
        case 83: break;
        case 24: 
          { System.out.println(yytext());return tok(BITNOT);
          }
        case 84: break;
        case 18: 
          { System.out.println(yytext());return tok(BITAND);
          }
        case 85: break;
        case 20: 
          { System.out.println(yytext());return tok(GT);
          }
        case 86: break;
        case 3: 
          { System.out.println(yytext());return tok(ID, yytext());
          }
        case 87: break;
        case 35: 
          { System.out.println(yytext());return tok(XORASS);
          }
        case 88: break;
        case 19: 
          { System.out.println(yytext());return tok(LT);
          }
        case 89: break;
        case 23: 
          { System.out.println(yytext());return tok(MOD);
          }
        case 90: break;
        case 30: 
          { System.out.println(yytext());return tok(MULASS);
          }
        case 91: break;
        case 12: 
          { System.out.println(yytext());return tok(LBRACE);
          }
        case 92: break;
        case 57: 
          { System.out.println(yytext());return tok(CHR,yytext());
          }
        case 93: break;
        case 63: 
          { System.out.println(yytext());return tok(BREAK);
          }
        case 94: break;
        case 62: 
          { System.out.println(yytext());return tok(WHILE);
          }
        case 95: break;
        case 11: 
          { System.out.println(yytext());return tok(ASSIGN);
          }
        case 96: break;
        case 49: 
          { System.out.println(yytext());return tok(STR,yytext());
          }
        case 97: break;
        case 64: 
          { System.out.println(yytext());return tok(RETURN);
          }
        case 98: break;
        case 1: 
          { throw new RuntimeException("Illegal character " + yytext() + " in line " + (yyline + 1) + ", column " + (yycolumn + 1));
          }
        case 99: break;
        case 52: 
          { System.out.println(yytext());return tok(FOR);
          }
        case 100: break;
        case 21: 
          { System.out.println(yytext());return tok(PLUS);
          }
        case 101: break;
        case 16: 
          { System.out.println(yytext());return tok(BITOR);
          }
        case 102: break;
        case 56: 
          { System.out.println(yytext());return tok(ELLIPSIS);
          }
        case 103: break;
        case 9: 
          { System.out.println(yytext());return tok(SEMICOLON);
          }
        case 104: break;
        case 67: 
          { System.out.println(yytext());return tok(TYPEDEF);
          }
        case 105: break;
        case 44: 
          { System.out.println(yytext());return tok(SUBASS);
          }
        case 106: break;
        case 14: 
          { System.out.println(yytext());return tok(LBRACK);
          }
        case 107: break;
        case 22: 
          { System.out.println(yytext());return tok(MINUS);
          }
        case 108: break;
        case 6: 
          { System.out.println(yytext());return tok(ASTER);
          }
        case 109: break;
        case 59: 
          { System.out.println(yytext());return tok(VOID);
          }
        case 110: break;
        case 31: 
          { System.out.println(yytext());return tok(IF);
          }
        case 111: break;
        case 58: 
          { System.out.println(yytext());return tok(ELSE);
          }
        case 112: break;
        case 29: 
          { err("Comment symbol do not match!");
          }
        case 113: break;
        case 51: 
          { if (commentCount == true) {commentCount=false;yybegin(YYINITIAL);}
          }
        case 114: break;
        case 61: 
          { System.out.println(yytext());return tok(UNION);
          }
        case 115: break;
        case 7: 
          { System.out.println(yytext());return tok(LPAREN);
          }
        case 116: break;
        case 65: 
          { System.out.println(yytext());return tok(STRUCT);
          }
        case 117: break;
        case 66: 
          { System.out.println(yytext());return tok(SIZEOF);
          }
        case 118: break;
        case 28: 
          { System.out.println(yytext());return tok(DIVASS);
          }
        case 119: break;
        case 34: 
          { System.out.println(yytext());return tok(OR);
          }
        case 120: break;
        case 32: 
          { System.out.println(yytext());return tok(EQ);
          }
        case 121: break;
        case 54: 
          { System.out.println(yytext());return tok(SHLASS);
          }
        case 122: break;
        case 46: 
          { System.out.println(yytext());return tok(DEC);
          }
        case 123: break;
        case 10: 
          { System.out.println(yytext());return tok(COMMA);
          }
        case 124: break;
        case 17: 
          { System.out.println(yytext());return tok(INDEX);
          }
        case 125: break;
        case 13: 
          { System.out.println(yytext());return tok(RBRACE);
          }
        case 126: break;
        case 60: 
          { System.out.println(yytext());return tok(CHAR);
          }
        case 127: break;
        case 39: 
          { System.out.println(yytext());return tok(SHL);
          }
        case 128: break;
        case 55: 
          { System.out.println(yytext());return tok(SHRASS);
          }
        case 129: break;
        case 27: 
          { System.out.println(yytext());commentCount = true; yybegin(YYCOMMENT);
          }
        case 130: break;
        case 4: 
          { System.out.println(yytext());return tok(NUM, new Integer(yytext()));
          }
        case 131: break;
        case 45: 
          { System.out.println(yytext());return tok(PTR);
          }
        case 132: break;
        case 26: 
          { System.out.println(yytext());return tok(DOT);
          }
        case 133: break;
        case 68: 
          { System.out.println(yytext());return tok(CONTINUE);
          }
        case 134: break;
        case 40: 
          { System.out.println(yytext());return tok(GE);
          }
        case 135: break;
        case 48: 
          { System.out.println(yytext());return tok(NE) ;
          }
        case 136: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { 	{
		if (yystate() == YYCOMMENT) {
			err("Comment symbol do not match (EOF)!");
		}
		return tok(EOF, null);
	}
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
