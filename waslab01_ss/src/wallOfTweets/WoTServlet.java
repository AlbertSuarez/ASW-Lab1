
package wallOfTweets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WoTServlet extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5207702297571272100L;
	Locale currentLocale = new Locale("en");
	String ENCODING = "ISO-8859-1";


	@Override
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {
			Vector<Tweet> tweets = Database.getTweets();
			if(req.getHeader("Accept").equals("text/plain")) printPLAINresult(tweets,req,res);
			else printHTMLresult(tweets, req, res);
		}

		catch (SQLException ex ) {
			throw new ServletException(ex);
		}
	}

	@Override
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// This method does NOTHING but to redirect to the main page
		String author = req.getParameter("author");
		String text = req.getParameter("tweet_text");
		try {
			Database.insertTweet(author,text);
		} catch (SQLException e) {
		 System.out.println("Error a la inserció: " + e);
		}
		res.sendRedirect("wot");

	}

	private void printHTMLresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
		String currentDate = dateFormatter.format(new java.util.Date());
		res.setContentType ("text/html");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<html>");
		out.println("<head><title>Wall of Tweets</title>");
		out.println("<link href=\"wallstyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
		out.println("<body class=\"wallbody\">");
		out.println("<h1>Wall of Tweets</h1>");
		out.println("<div class=\"walltweet\">"); 
		out.println("<form action=\"wot\" method=\"post\">");
		out.println("<table border=0 cellpadding=2>");
		out.println("<tr><td>Your name:</td><td><input name=\"author\" type=\"text\" size=70></td><td/></tr>");
		out.println("<tr><td>Your tweet:</td><td><textarea name=\"tweet_text\" rows=\"2\" cols=\"70\" wrap></textarea></td>"); 
		out.println("<td><input type=\"submit\" name=\"action\" value=\"Tweet!\"></td></tr>"); 
		out.println("</table></form></div>"); 
		for (Tweet tweet: tweets) {
			String messDate = dateFormatter.format(tweet.getDate());
			if (!currentDate.equals(messDate)) {
				out.println("<br><h3>...... " + messDate + "</h3>");
				currentDate = messDate;
			}
			out.println("<div class=\"wallitem\">");
			out.println("<h4><em>" + tweet.getAuthor() + "</em> @ "+ timeFormatter.format(tweet.getDate()) +"</h4>");
			out.println("<p>" + tweet.getText() + "</p>");
			out.println("</div>");
		}
		out.println ( "</body></html>" );
	}
	
	private void printPLAINresult(Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType ("text/html");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		for(Tweet tweet: tweets){
			out.println("tweet #" + tweet.getTwid() + ": " +
					tweet.getAuthor() + ": " + tweet.getText() + " [" + tweet.getDate() + "]");
		}
	}
}
