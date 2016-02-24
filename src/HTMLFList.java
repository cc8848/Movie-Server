import java.io.File;
import java.util.Arrays;

public class HTMLFList {
	
	public HTMLFList(UI uI) {
		this.uI = uI;
	}
	
	public String generateHTMLListDoc(String folder) {
		uI.println("Sent folder index for: " + folder);
		String doc = startHTML + lineBreak;
		File[] files = listFiles(folder);
		int toSkip = 0;
		boolean hasDirectories = false;
		for(File file: files) {
			if (file.isDirectory()) {
				if (file.getName().toLowerCase().startsWith("$recycle") || file.getName().toLowerCase().startsWith("system")) {
				} else {
					if (!hasDirectories) {
						hasDirectories = true;
						doc = doc + "<b>Folders: </b>";
						doc = doc + lineBreak;
					}
					doc = doc + fileAsLink(file.getName() + "/") + lineBreak;
				}
			}
		}
		if (hasDirectories) {
			doc = doc + lineBreak;
			doc = doc + lineBreak;
		}
		boolean hasMovies = false;
		for(File file: files) {
			if (file.isFile() && file.getName().contains("mp4")) {
				if (!hasMovies) {
					hasMovies = true;
					doc = doc + "<b>Movies: </b>";
					doc = doc + lineBreak;
				}
				doc = doc + buttonForDL(file.getName(), fileAsLink(file.getName())) + lineBreak;
				//doc = doc + buttonForDL(file.getName()) + fileAsLink(file.getName()) + lineBreak;
			}
		}
		if (hasMovies == false && hasDirectories == false) {
			doc = doc + "<b>Nothing to display here...</b>";
		}
		doc = doc + endHTML;
		System.out.println("Sending List...");
		return doc;
	}
	
	public String fileAsLink(String file) {
		if (file.endsWith("mp4")) {
			return "<a href=\"" + file + "?watch=1\">" + file + "<a>";
		} else {
			return "<a href=\"" + file + "\">" + file + "<a>";
		}
	}
	
	public String buttonForDL(String file, String link) {
		return "<form action=\"" + file + "\"> <input type=\"submit\" value=\"DL\">" + link + "</form>";
	}
	
	public File[] listFiles(String folder) {
		File[] fileList = new File(uI.config.getSite() + folder).listFiles();
		Arrays.sort(fileList);
		return folderFile(fileList);
	}
	
	public File[] folderFile(File[] files) {
		int filePoint = 0;
		File[] folderFiles = new File[files.length];
		for (File file: files) {
			if (file.isDirectory()) {
				folderFiles[filePoint] = file;
				filePoint++;
			}
		}
		for (File file: files) {
			if (file.isFile()) {
				folderFiles[filePoint] = file;
				filePoint++;
			}
		}
		return folderFiles;
	}
	
	public UI uI;
	public String lineBreak = "<br>";
	public String startHTML = "<html>";
	public String endHTML = "</html>";
}