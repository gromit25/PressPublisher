package com.gromit25.presspublisher.evaluator.parser;

import java.io.PushbackReader;

class ParserUtil {
	
	static boolean isSpaceChar(char ch) {
		if(ch == ' ' || ch =='\t' || ch == '\n' || ch =='\r') {
			return true;
		} else {
			return false;
		}
	}
	
	static boolean isNameChar(char ch) {
		
		if((ch >= 'a' && ch <= 'z')
			|| (ch >= 'A' && ch <= 'Z')
			|| (ch >= '0' && ch <= '9')
			|| ch == '_') {
			return true;
		} else {
			return false;
		}
	}
	
	static void unread(PushbackReader reader, int read) throws Exception {
		if(read != -1) {
			reader.unread(read);
		}
	}

}
