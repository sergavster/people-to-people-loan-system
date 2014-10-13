package com.p2psys.model.borrow.protocol;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class TemplateReader extends FilterReader {
	private ArrayList lines = new ArrayList();
	StringBuffer lineBuf = new StringBuffer();
	StringBuffer variables=new StringBuffer();
	public TemplateReader(Reader r) {
         super(r);
     }
	@Override
	public int read() throws IOException {
        int c = in.read();
        handleChar(c);
        return c;
    }
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int numchars = in.read(cbuf, off, len);
        for (int i=off; i < off+numchars; i++) {
            char c = cbuf[i];
            handleChar(c);
        }
        return numchars;
	}

	private void handleChar(int c) {
		if(c=='$'){
			variables.setLength(0);
			variables.append((char)c);
			lines.add(lineBuf.toString());
			lineBuf.setLength(0);
			lineBuf.append((char)c);
		}else if(c=='{'){
			if(variables.length()>0&&variables.toString().equals("$")){
				variables.append((char)c);
			}else{
				variables.setLength(0);
				variables.append((char)c);
			}
			lineBuf.append((char)c);
		}else if(c=='}'){
			if(variables.length()>0&&variables.toString().equals("${")){
				lineBuf.append((char)c);
				lines.add(lineBuf.toString());
				lineBuf.setLength(0);
				variables.setLength(0);
			}else{
				lineBuf.append((char)c);
			}
		}else{
			lineBuf.append((char) c);
		}
		
    }
	
	@Override
	public void close() throws IOException {
		 super.close();
		 in.close();
	}
	public ArrayList getLines() {
		lines.add(lineBuf.toString());
		return lines;
	}
	public void setLines(ArrayList lines) {
		this.lines = lines;
	}
	
}
