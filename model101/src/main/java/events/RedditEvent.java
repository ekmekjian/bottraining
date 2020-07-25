/**
 * sample for posting into a specific channel in discord
 * TextChannel textChannel = event.getGuild().getTextChannelsByName("CHANNEL_NAME",true).get(0);
textChannel.sendMessage("MESSAGE").queue();
*/

package events;

import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.List;

public class RedditEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event)
    {

        Message msg = event.getMessage();
        // Initialize REST Client
        RestClient restClient = new HttpRestClient();
        restClient.setUserAgent("news bot");
        // Connect the user
        com.github.jreddit.entity.User user = new User(restClient, "", "");


        if (msg.getContentRaw().equals("!rmeme"))
        {
            MessageChannel channel = event.getChannel();
            List<Submission> topfeed = pullMemeFeed(user,restClient);
            for(Submission sub : topfeed)
            {
                channel.sendMessage((String)sub.getUrl()+"\n").queue();
                channel.sendMessage("--------------------------------------------------" +
                        "-------------------------------------------------").queue();

            }
        }
        if (msg.getContentRaw().equals("!rnews"))
        {
            MessageChannel channel = event.getChannel();
            List<Submission> newsfeed = pullNewsFeed(user,restClient);
            for(Submission sub : newsfeed)
            {
                channel.sendMessage((String)sub.getTitle()+"\n").queue();
                channel.sendMessage("r/"+(String)sub.getSubreddit()+"\n").queue();
                channel.sendMessage((String)sub.getUrl()).queue();
                channel.sendMessage("--------------------------------------------------" +
                        "-------------------------------------------------").queue();

            }
        }
        if(msg.getContentRaw().equals("!help"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Use:\n!rnews - to pull articles from r/news\n!rmemes - "+
            "to pull memes from r/comedyhomicide").queue();
        }
        if(msg.getContentRaw().equals("!search"))
        {
            MessageChannel channel = event.getChannel();
            try {botSearch(channel);} catch (IOException e) { e.printStackTrace();}

        }
        if(msg.getContentRaw().equals("!8ball")){
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Asking the 🎱 your question..");
        }
    }
    void botSearch(MessageChannel ch) throws IOException
    {
         /**
             * Documents d = Jsoup.connect("url").get();
             * v this is loading the container element that holds the acutal data you're looking for
             * Elements ele =d.select("thetag#id")
             * for(Element element : ele.select("thetag.class"))
             */
            Document d = Jsoup.connect("https://duckduckgo.com/?t=ffab&q=witchcraft&atb=v229-5rk&ia=web").get();
            Elements ele = d.select("div#links");
            for(Element element : ele.select("div.result"))
            {
                String title= element.select("dev.result__title a").text();
                System.out.println(title);
            }
    }
    List<Submission> pullMemeFeed(com.github.jreddit.entity.User user, RestClient restClient){
        
        try {
            user.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Handle to Submissions, which offers the basic API submission functionality
        Submissions subms = new Submissions(restClient, user);

        // Retrieve submissions of a submission
        List<Submission> submissionsSubreddit = subms.ofSubreddit("comedyhomicide",
                SubmissionSort.TOP, -1, 3, null, null, true);

        return submissionsSubreddit;
    }
    List<Submission> pullNewsFeed(com.github.jreddit.entity.User user, RestClient restClient){
        
        try {
            user.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Handle to Submissions, which offers the basic API submission functionality
        Submissions subms = new Submissions(restClient, user);

        // Retrieve submissions of a submission
        List<Submission> submissionsSubreddit = subms.ofSubreddit("news",
                SubmissionSort.HOT, -1, 3, null, null, true);

        return submissionsSubreddit;
    }

}
