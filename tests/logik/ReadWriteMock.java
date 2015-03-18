package logik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;


public class ReadWriteMock {

	private int symIdx;
	private String msgForInput;
	private AtomicBoolean wasWrite;
	
	public ReadWriteMock(int symIdx, String msgForInput){
		this.symIdx = symIdx;
		this.msgForInput = msgForInput;
		wasWrite = new AtomicBoolean();
		wasWrite.set(true); 
	}
	
	public void setNewMsg(String msg){
		this.msgForInput = msg;
		symIdx = 0;
	}
	
	public OutputStream getOutputStream(){
		return new OutputStreamMock();
	}
	
	public InputStream getInputStream(){
		return new InputStreamMock();
	}
	
	public class OutputStreamMock extends java.io.OutputStream{
		
		@Override
		public void write(byte[] msg) throws IOException {
			while(wasWrite.get() == true);
			wasWrite.set(true);
		}

		@Override
		public void write(int b) throws IOException {
		}

	}
	
	public class InputStreamMock extends java.io.InputStream{

		private boolean isInterrupted = false;

		@Override
		public int read() throws IOException {
			int sym = 0x08;
			while(wasWrite.get() == false && !isInterrupted);
			if(msgForInput.length() > symIdx){
				sym = msgForInput.getBytes()[symIdx];
				symIdx++;
			}else{
				symIdx = 0;
				wasWrite.set(false);
			}
			return sym;
		}
		
		@Override
		public void close(){
			this.isInterrupted = true;
		}
		
	}
	
}
