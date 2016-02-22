public class MoviePage {
	
	public MoviePage() {
	}
	
	public String genMoviePage(String movie) {
		String page = startHTML + embedMovie(movie) + endHTML;
		return page;
	}
	
	public String embedMovie(String movie) {
		movie = movie.replace("?watch=1", "");
		return "<video width=100% height=100% id=\"" + getFromOtherHost(movie) + "\" src=\"" + getFromOtherHost(movie) + "\" controls></video>";
	}
	
	public String getFromOtherHost(String movie) {
		movie = movie.replaceFirst("/", "");
		return "http://192.168.0.182:9002/H%3A/" + movie;
	}
	
	public String startHTML = "<html>";
	public String endHTML = "</html>";
}