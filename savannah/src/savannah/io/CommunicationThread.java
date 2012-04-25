package savannah.io;

import savannah.device.Connection;
import savannah.io.DOMinator.DOMinator;
import savannah.server.*;

import java.net.Socket;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.lang.Thread;

import org.jdom.JDOMException;

public class CommunicationThread extends Thread {
//	private IOHandler ioHandler;
	private Socket socket;
	private TransmissionHandler handle = null;
	private String folder;
	private int bufferSize;

	public CommunicationThread(Socket _socket, String _folder, int _bufferSize) {
		//		this.ioHandler = IOHandler.getInstance();
		this.socket = _socket;
		this.folder = _folder;
		this.bufferSize = _bufferSize;
		//		start();
	}

	public void run() {
//		this.ioHandler = IOHandler.getInstance();
		try {
			InputStream inputStream = new DataInputStream(this.socket.getInputStream());
			//			boolean hasRead = false;
			//			while (this.isAlive() == true) {
			//				String message = inputStream.readUTF();
			//				if (hasRead == false) {
			IOHandler.getInstance().displayMessage("Connecting from: " + this.socket);
//			savannah.device.Connection con = new Connection(folder); 
//			handle = new TransmissionHandler(con);
			handle = new TransmissionHandler(this.socket, this.folder);
			IOHandler.getInstance().logIt(this.socket, handle.CR().toString());
			//TransmissionHandler now has a DEBUG statement implemented ...

			//CommitEvent
			if (handle.CR() == CRUD.COMMIT) {
				//					DOMinator domI = new DOMinator();
				IOHandler.getInstance().displayMessage("CommitEvent created by: " + this.socket);
				//					CommitEvent comEvt = new CommitEvent(domI.Dominate(handle.XML()), this.socket, handle.anyFiles());
				//					EventQueue.getInstance().add(comEvt);
				IOHandler.getInstance().logIt(true);
			}
			//RequestEvent
			else if (handle.CR() == CRUD.REQUEST) {
				IOHandler.getInstance().displayMessage("RequestEvent created by: " + this.socket);
				//					RequestEvent reqEvt = new RequestEvent(handle.XML(), this.socket);
				//					EventQueue.getInstance().add(reqEvt);
				IOHandler.getInstance().logIt(true);
			}
			//Ping
			else if (handle.CR() == CRUD.PING) {
				IOHandler.getInstance().displayMessage("Ping sent from: " + this.socket);
				IOHandler.getInstance().respond(this.socket, handle.XML());
				IOHandler.getInstance().logIt(true);
			}
			else if (handle.CR() == CRUD.ERROR){
				IOHandler.getInstance().displayMessage("TransmissionHandler could not resolve to a known package");
				IOHandler.getInstance().logIt(false);
			}
			else {
				IOHandler.getInstance().displayMessage("TransmissionHandler could not resolve to a known package");
			}
			//					hasRead = true;
			//				}
			//				handle = new TransmissionHandler(inputStream, this.bufferSize);
			//				this.ioHandler.displayMessage(message);
			//				
			//				this.ioHandler.sendToConncted(message);
			//			}
		}	catch (IOException e) {
			System.out.println("CommunicationThread: Could not initiate inputStream for the connection");
		} 	/*catch (JDOMException e) {
			System.err.println("CommunicationThread: Could not DOMinate");
		}*/	finally {
			IOHandler.getInstance().removeConnection(this.socket);
		}
	}

}

