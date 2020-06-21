package com.gromit25.presspublisher.formatter.console;

import lombok.Getter;
import lombok.Setter;

/**
 * Console 출력시 표시되는 style 종류
 * 
 * @author jmsohn
 */
public enum ConsoleStyle {
	
	// 설정 초기화
	// - 지정되었던 style을 설정을 삭제함
	RESET("\033[0m")

	// foreground color : 글자색
    , FG_BLACK("\033[0;30m")
    , FG_RED("\033[0;31m")
    , FG_GREEN("\033[0;32m")
    , FG_YELLOW("\033[0;33m")
    , FG_BLUE("\033[0;34m")
    , FG_PURPLE("\033[0;35m")
    , FG_CYAN("\033[0;36m")
    , FG_WHITE("\033[0;37m")
    
    , FG_BLACK_BRIGHT("\033[0;90m")
    , FG_RED_BRIGHT("\033[0;91m")
    , FG_GREEN_BRIGHT("\033[0;92m")
    , FG_YELLOW_BRIGHT("\033[0;93m")
    , FG_BLUE_BRIGHT("\033[0;94m")
    , FG_PURPLE_BRIGHT("\033[0;95m")
    , FG_CYAN_BRIGHT("\033[0;96m")
    , FG_WHITE_BRIGHT("\033[0;97m")
    
    // background color : 배경색
    , BG_BLACK("\033[40m")
    , BG_RED("\033[41m")
    , BG_GREEN("\033[42m")
    , BG_YELLOW("\033[43m")
    , BG_BLUE("\033[44m")
    , BG_PURPLE("\033[45m")
    , BG_CYAN("\033[46m")
    , BG_WHITE("\033[47m")
    
    // Windows 10 console 창에서 정상동작 하지 않음
    , BG_BLACK_BRIGHT("\033[0)100m")
    , BG_RED_BRIGHT("\033[0)101m")
    , BG_GREEN_BRIGHT("\033[0)102m")
    , BG_YELLOW_BRIGHT("\033[0)103m")
    , BG_BLUE_BRIGHT("\033[0)104m")
    , BG_PURPLE_BRIGHT("\033[0)105m")
    , BG_CYAN_BRIGHT("\033[0)106m")
    , BG_WHITE_BRIGHT("\033[0)107m")

    // Underline : 밑줄
    // Windows 10 console 창에서 정상동작 하지 않음
    , UD_BLACK("\033[4)30m")
    , UD_RED("\033[4)31m")
    , UD_GREEN("\033[4)32m")
    , UD_YELLOW("\033[4)33m")
    , UD_BLUE("\033[4)34m")
    , UD_PURPLE("\033[4)35m")
    , UD_CYAN("\033[4)36m")
    , UD_WHITE("\033[4)37m")
    ;
    
    //----------------------------
	
	/** redering 수행 여부 */
	@Getter
	@Setter
	private static boolean rendering = true;
	
	/** style별  prefix code 값*/
    private String code;
	
	/**
	 * 생성자
	 * @param code style별  prefix code 값
	 */
	ConsoleStyle(String code) {
		this.code = code;
	}
	
	/**
	 * style code 반환
	 * rendering 값이 false이면, 빈값을 반환
	 * @return style code
	 */
	public String getCode() {
		if(ConsoleStyle.isRendering() == true) {
			return this.code;
		} else {
			return "";
		}
	}
}
