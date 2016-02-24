public class MoviePage {
	
	public MoviePage(UI uI) {
		this.uI = uI;
	}
	
	public String genMoviePage(String movie) {
		String page = startHTML + embedMovie(movie) + endHTML;
		return page;
	}
	
	public String embedMovie(String movie) {
		uI.println("Watching: " + movie.split("/")[1]);
		movie = movie.replace("?watch=1", "");
		return "<video width=100% height=100% id=\"" + getFromOtherHost(movie) + "\" src=\"" + getFromOtherHost(movie) + "\" controls></video>";
	}
	
	public String getFromOtherHost(String movie) {
		movie = movie.replaceFirst("/", "");
		return uI.config.getMovieLinkback() + movie;
	}
	
	public String startHTML = "<html>";
	public String endHTML = "</html>";
	public UI uI;
}