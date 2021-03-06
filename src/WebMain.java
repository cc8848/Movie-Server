import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class WebMain {
	
	public WebMain(UI uI) {
		this.uI = uI;
	}

	protected void start() {
		uI.println("Webserver starting up");
		try {
			serverSocket = new ServerSocket(uI.config.getPort());
		} catch (Exception e) {
			uI.println("Error: " + e);
			e.printStackTrace();
			return;
		}
		uI.println("Waiting for connection");
		while(running) {
			try {
				Socket remote = serverSocket.accept();
				System.gc();
				(new Thread() {
					public void run() {
						handleConnection(remote);
					}
				}).start();
			} catch (Exception e) {
				uI.println("Error: " + e);
				e.printStackTrace();
			}
		}
	}
  
	public void handleConnection(Socket remote) {
		try {
			uI.println("Connection accepted from: " + remote.getInetAddress().getHostAddress());
			BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
			String dataIn = in.readLine();
			handleGet(remote, dataIn);
			/*if (dataIn.split(" ")[0] == "PUSH") {
				handlePush(remote, dataIn);
			} else if (dataIn.split(" ")[0] == "GET") {
				handleGet(remote, dataIn);
			}*/
		} catch (Exception e) {
			uI.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void handlePush(Socket remote, String dataIn) {
		uI.println("Connection PUSH attempt found.");
		sendGeneratedHTML(remote, "<html><b>Sorry, I currently do not handle PUSH requests</b></html>");
	}
	
	public void handleGet(Socket remote, String dataIn) {
		try {
			String requestedFile = dataIn.split(" ")[1];
			requestedFile = fixHTMLLinked(requestedFile);
			if (requestedFile.endsWith("mp4?watch=1")) {
				handleMoviePage(remote, requestedFile);
				return;
			}
			if (requestedFile.length() <= 1) {
				if (new File(uI.config.getSite() + "index.html").exists()) {
					sendFile("index.html", remote);
				} else {
					sendGeneratedHTML(remote, new HTMLFList(uI).generateHTMLListDoc(""));
				}
			} else {
				if (new File(uI.config.getSite() + requestedFile).isDirectory()) {
					if (new File(uI.config.getSite() + requestedFile + "index.html").exists()) {
						requestedFile = requestedFile + "index.html";
					} else {
						sendGeneratedHTML(remote, new HTMLFList(uI).generateHTMLListDoc(requestedFile));
						return;
					}
				}
				sendFile(requestedFile, remote);
			}
		} catch (Exception e) {
			uI.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void handleMoviePage(Socket remote, String requestedMovie) {
		sendGeneratedHTML(remote, new MoviePage(uI).genMoviePage(requestedMovie));
	}
	
	public String fixHTMLLinked(String fix) {
		String newFix = fix.replace("%20", " ");
		return newFix;
	}
  
	public void sendGeneratedHTML(Socket remote, String dataToWrite) {
		try {
			OutputStream out = remote.getOutputStream();
			out.write(dataToWrite.getBytes());
			out.flush();
			remote.close();
		} catch (Exception e) {
			uI.println("Error: " + e);
			e.printStackTrace();
		}
	}
  
	public void sendFile(String file, Socket remote) {
		try {
			if (file.endsWith(".mp4?")) {
				file = file.replace(".mp4?", ".mp4");
			}
			uI.println("Sending file: " + file + "...");
			File fileToSend = new File(uI.config.getSite() + file);
			BufferedInputStream d =new BufferedInputStream(new FileInputStream(fileToSend));
			BufferedOutputStream outStream = new BufferedOutputStream(remote.getOutputStream());
			if (!fileToSend.getName().toLowerCase().endsWith("html")) {
				outStream.write("HTTP/1.1 200 OK\r\n".getBytes());
				
				//code does not support ranges
				//outStream.write("Accept-Ranges: bytes\r\n".getBytes());
				
				outStream.write("Connection: close\r\n".getBytes());
				outStream.write(("Content-Length: " + fileToSend.length() + "\r\n").getBytes());
				String contentType = "Content-Type: " + getMimeType(fileToSend.getName()) + "\r\n";
				System.out.println(getMimeType(fileToSend.getName()));
				outStream.write(contentType.getBytes());
				outStream.write(("Content-Disposition: attachment; filename=\"" + fileToSend.getName() + "\"\r\n").getBytes());
				outStream.write("\r\n".getBytes());
			}
			byte buffer[] = new byte[4096];
			int read;
			while((read = d.read(buffer))!=-1) {
				outStream.write(buffer, 0, read);
				outStream.flush();
			}
			d.close();
			remote.close();
			uI.println("Sent file: " + file + "...");
		} catch (Exception e) {
			uI.println("Error when sending file: " + e);
			e.printStackTrace();
		}
	}
	
	public String getMimeType(String extension) {
		if (extension.endsWith("mp4")) {
			return "video/mp4";
		}
		if (extension.endsWith("html")) {
			return "text/html";
		}
		if (extension.endsWith("txt")) {
			return "text/html";
		}
		if (extension.endsWith("ico")) {
			return "image/x-icon";
		}
		if (extension.endsWith("png")) {
			return "image/png";
		}
		if (extension.endsWith("jpg")) {
			return "image/jpeg";
		}
		if (extension.endsWith("gif")) {
			return "image/gif";
		}
		return "application/octet-stream";
	}
	
	public Boolean running = false;
	public UI uI;
	public ServerSocket serverSocket;
}
