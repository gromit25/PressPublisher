package com.gromit25.presspublisher.formatter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author jmsohn
 */
public class XmlLocInputStream extends InputStream {
	
	private static int BUFFER_SIZE = 1024;
	
	private enum ParsingStatus {
		CONTENTS,
		TAG_START,
		TAG_NAME,
		TAG_CONTENTS,
		TAG_END,
		QUOTO_STRING,
		QUOTO_ESCAPE,
		D_QUOTO_STRING,
		D_QUOTO_ESCAPE
	}
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private Loc startTagLoc;
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private int lineNum;
	
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private InputStream orgInputStream;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ByteBuffer columnBuffer;
	
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ByteBuffer tagNameBuffer;

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ParsingStatus status;

	/**
	 * 
	 * getter는 실제 구현함
	 */
	private Hashtable<String, ArrayList<Loc>> tagMap;
	
	/**
	 * 
	 * getter는 실제 구현함
	 */
	private Hashtable<String, Integer> tagSearchBaseMap;
	
	/**
	 * 생성자
	 * @param is
	 */
	public XmlLocInputStream(InputStream is) {
		
		this.setOrgInputStream(is);
		this.setLineNum(1);
		this.setColumnBuffer(ByteBuffer.allocate(BUFFER_SIZE));
		this.setTagNameBuffer(ByteBuffer.allocate(BUFFER_SIZE));
		this.setStatus(ParsingStatus.CONTENTS);
	}

	@Override
	public int read() throws IOException {
		
		// XML 스트림에서 한문자를 읽어옴
		int read = this.getOrgInputStream().read();
		
		// 읽어온 문자가 0 이상일 경우 처리함
		// -> 0 미만은 Stream 종료를 의미하기 때문에 처리하지 않음
		if(read >= 0) {
			
			if('\n' == (char)read) {
				
				// 새로운 라인 시작일 경우,
				// line number를 하나 올리고
				// buffer와 임시컬럼길이 변수(tempColumnNum)를 초기화함
				this.setLineNum(this.getLineNum() + 1);
				this.getColumnBuffer().clear();
				
			} else {
				
				// 새로운 라인이 아닐 경우,
				// parsing status를 업데이트 함
				this.updateParsingStatus(read);
				
				// buffer에 현재 읽은 내용을 추가함
				// -> 추후 column 수를 계산하기 위함임
				this.putToColumnBuffer((byte)read);
			}
			
		}
		
		return read;
	}
	
	/**
	 * 
	 * @param tagName
	 * @param tagEnd
	 * @return
	 */
	public Loc findTagStartLoc(String tagName, Loc tagEndLoc) throws Exception {
		
		if(null == tagName) {
			throw new Exception("tagName param is null.");
		}
		
		if(null == tagEndLoc) {
			throw new Exception("tagEndLoc param is null.");
		}
		
		// 1. 기준위치(search base)의 loc와 다음 tag의 시작 위치 loc를 가져옴
		int tagSearchBase = this.getTagSearchBase(tagName);
		
		ArrayList<Loc> tagStartLocList = this.getTagStartLocList(tagName);
		Loc baseStartLoc = tagStartLocList.get(tagSearchBase);
		Loc nextStartLoc = null;
		
		// 기준위치(search base)가 가장 마지막이 아니면
		// 다음 tag 위치를 가져옴
		// 기준위치가 마지막이면, nextStartLoc는 null임
		if(tagSearchBase + 1 < this.getTagStartLocList(tagName).size()) {
			nextStartLoc = tagStartLocList.get(tagSearchBase + 1);
		}
		
		// 2.
		
		// 기준위치(search base)와 tag 종료 위치가 동일할 수 없음
		if(0 == baseStartLoc.compareTo(tagEndLoc)) {
			throw new Exception("tag start loc equals tag end loc.");
		}
		
		// 기준위치(search base)가 가장 마지막인 경우
		// 무조건 가장 마지막 위치를 반환함
		// -> 순서대로 검색하기 때문에 앞으로 가지 않고 반환함
		if(null == nextStartLoc) {
			return baseStartLoc;
		}

		// 기준위치(search base)가 마지막이 아닌 경우
		
		// tag 종료 위치가 다음 tag 시작위치 보다 뒤쪽에 있으면,
		// 기준위치를 다음 tag 시작위치로 변경하여 검색함
		if(0 > nextStartLoc.compareTo(tagEndLoc)) {
			this.setTagSearchBase(tagName, tagSearchBase + 1);
			return this.findTagStartLoc(tagName, tagEndLoc);
		}
		
		// 순서대로 검색하기 때문에,
		// tag 종료 위치가 tag 시작위치 보다 앞쪽에 있을 수 없음
		if(0 < baseStartLoc.compareTo(tagEndLoc)) {
			throw new Exception("tag start loc is not greater then tag end loc.");
		}

		// 마지막까지 오면,
		// tag 종료 위치가 tag의 시작 위치와 다음 tag 시작위치 사이에 있는 경우이므로
		// 현재 위치를 반환함
		return baseStartLoc;
	}
	
	/**
	 * 
	 * @param read
	 */
	private void updateParsingStatus(int read) {
		
		char ch = (char)read;
		
		switch(this.getStatus()) {
		case CONTENTS:
			if(ch == '<') {
				// 현재 위치(line/column number)를 Tag시작지점으로 설정
				this.setStartTagLoc(this.getLineNum(), this.getColumnNumInBuffer());
				this.setStatus(ParsingStatus.TAG_START);
			}
			break;
			
		case TAG_START:
			if((ch >= 'a' && ch <= 'z') ||
				(ch >= 'A' && ch <= 'Z') ||
				ch == '_') {
				
				this.putToTagNameBuffer((byte)read);
				this.setStatus(ParsingStatus.TAG_NAME);
				
			} else {
				this.setStatus(ParsingStatus.CONTENTS);
			}
			break;
			
		case TAG_NAME:
			if((ch >= 'a' && ch <= 'z') ||
				(ch >= 'A' && ch <= 'Z') ||
				(ch >= '0' && ch <= '9') ||
				ch == '_' || ch == '-' || ch =='.' || ch == ':') {
				
				this.putToTagNameBuffer((byte)read);
				
			} else {
				
				// Tag 이름이 끝나는 경우,
				// Tag 이름에 현재 위치를 저장함
				String tagName = new String(this.getTagNameBuffer().array(), 0, this.getTagNameBuffer().position());
				this.addTagMap(tagName, this.getStartTagLoc());
				
				// 다음 Tag 이름을 저장하기 위해,
				// Tag 이름 Buffer를 클리어함
				this.getTagNameBuffer().clear();
				
				if(ch == '/') {
					this.setStatus(ParsingStatus.TAG_END);
				} else if(ch == '>') {
					this.setStatus(ParsingStatus.CONTENTS);
				} else {
					this.setStatus(ParsingStatus.TAG_CONTENTS);
				}
			}
			
			break;
			
		case TAG_CONTENTS:
			if(ch == '/') {
				this.setStatus(ParsingStatus.TAG_END);
			} else if(ch == '>') {
				this.setStatus(ParsingStatus.CONTENTS);
			} else if(ch == '\'') {
				this.setStatus(ParsingStatus.QUOTO_STRING);
			} else if(ch == '"') {
				this.setStatus(ParsingStatus.D_QUOTO_STRING);
			}
			
			break;
			
		case TAG_END:
			//
			this.setStatus(ParsingStatus.CONTENTS);
			break;
			
		case QUOTO_STRING:
			if(ch == '\\') {
				this.setStatus(ParsingStatus.QUOTO_ESCAPE);
			} else if(ch == '\'') {
				this.setStatus(ParsingStatus.TAG_CONTENTS);
			}
			break;
			
		case QUOTO_ESCAPE:
			this.setStatus(ParsingStatus.QUOTO_STRING);
			break;
			
		case D_QUOTO_STRING:
			if(ch == '\\') {
				this.setStatus(ParsingStatus.D_QUOTO_ESCAPE);
			} else if(ch == '"') {
				this.setStatus(ParsingStatus.TAG_CONTENTS);
			}
			break;
			
		case D_QUOTO_ESCAPE:
			this.setStatus(ParsingStatus.D_QUOTO_STRING);
			break;
			
		default:
			// do nothing
			break;
		}
	}
	
	/**
	 * buffer 내의 컬럼의 길이를 산출하여 반환
	 * @return buffer 내의 컬럼의 길이
	 */
	private int getColumnNumInBuffer() {
		
		// byte buffer내의 데이터를 
		// 문자열로 치환하여 길이를 계산함
		// -> 한글의 경우 한자로 세기위해서임
		byte[] byteBuffer = this.getColumnBuffer().array();
		int columnNum = new String(byteBuffer, 0, this.getColumnBuffer().position()).length();
		
		return columnNum;
	}
	
	/**
	 * 
	 * @param b
	 */
	private void putToColumnBuffer(byte b) {
		
		// 현재 buffer가 다 찻으면,
		// buffer의 크기를 BUFFER_SIZE만큼 증가시키고,
		// 기존 데이터를 복사해 넣음
		if(false == this.getColumnBuffer().hasRemaining()) {
			
			ByteBuffer temp = this.getColumnBuffer();
			this.setColumnBuffer(ByteBuffer.allocate(temp.position() + BUFFER_SIZE));
			this.getColumnBuffer().put(temp.array());
		}
		
		// buffer에 현재 읽은 내용을 추가함
		// -> 추후 column 수를 계산하기 위함임
		this.getColumnBuffer().put(b);
	}
	
	/**
	 * 
	 * @param b
	 */
	private void putToTagNameBuffer(byte b) {
		
		// 현재 buffer가 다 찻으면,
		// buffer의 크기를 BUFFER_SIZE만큼 증가시키고,
		// 기존 데이터를 복사해 넣음
		if(false == this.getTagNameBuffer().hasRemaining()) {
			
			ByteBuffer temp = this.getTagNameBuffer();
			this.setTagNameBuffer(ByteBuffer.allocate(temp.position() + BUFFER_SIZE));
			this.getTagNameBuffer().put(temp.array());
		}
		
		// buffer에 현재 읽은 내용을 추가함
		// -> 추후 column 수를 계산하기 위함임
		this.getTagNameBuffer().put(b);
	}
	
	/**
	 * 
	 * @param lineNum
	 * @param columnNum
	 */
	private void setStartTagLoc(int lineNum, int columnNum) {
		this.setStartTagLoc(new Loc(lineNum, columnNum));
	}

	/**
	 * 
	 * @param tagName
	 * @param loc
	 */
	private void addTagMap(String tagName, Loc loc) {
		
		ArrayList<Loc> map = this.getTagMap().get(tagName);
		
		if(null == map) {
			map = new ArrayList<Loc>();
			this.getTagMap().put(tagName, map);
		}
		
		map.add(loc);
	}
	
	/**
	 * 
	 * @return
	 */
	private Hashtable<String, ArrayList<Loc>> getTagMap() {
		
		if(null == this.tagMap) {
			this.tagMap = new Hashtable<String, ArrayList<Loc>>();
		}
		
		return this.tagMap;
	}
	
	/**
	 * 
	 * @param tagName
	 * @return
	 */
	private ArrayList<Loc> getTagStartLocList(String tagName) {
		
		ArrayList<Loc> tagStartLocList = this.getTagMap().get(tagName);
		
		if(null == tagStartLocList) {
			tagStartLocList = new ArrayList<Loc>();
			this.getTagMap().put(tagName, tagStartLocList);
		}
		
		return tagStartLocList;
	}
	
	/**
	 * 
	 * @return
	 */
	private Hashtable<String, Integer> getTagSearchBaseMap() {
		
		if(null == this.tagSearchBaseMap) {
			this.tagSearchBaseMap = new Hashtable<String, Integer>();
		}
		
		return this.tagSearchBaseMap;
	}
	
	/**
	 * 
	 * @param tagName
	 * @return
	 */
	private int getTagSearchBase(String tagName) {
		
		Integer tagSearchBase = this.getTagSearchBaseMap().get(tagName);
		
		if(null == tagSearchBase) {
			tagSearchBase = new Integer(0);
			this.getTagSearchBaseMap().put(tagName, tagSearchBase);
		}
		
		return tagSearchBase;
	}
	
	/**
	 * 
	 * @param tagName
	 * @param newBase
	 */
	private void setTagSearchBase(String tagName, int newBase) throws Exception {
		
		int listSize = 0;
		if(this.getTagStartLocList(tagName) != null) {
			listSize = this.getTagStartLocList(tagName).size();
		}
		
		if(listSize <= newBase || newBase < 0) {
			throw new Exception(String.format("Out of range(%s:0~%d):%d", tagName, listSize, newBase));
		}
		
		this.getTagSearchBaseMap().put(tagName, newBase);
	}
}
