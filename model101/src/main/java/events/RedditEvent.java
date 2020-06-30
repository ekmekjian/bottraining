package events;
import com.github.jreddit.entity.Submission;
import com.github.jreddit.entity.User;
import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.retrieval.params.SubmissionSort;
import com.github.jreddit.utils.restclient.HttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class RedditEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event)
    {

        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!rtop"))
        {
            MessageChannel channel = event.getChannel();
            List<Submission> topfeed = pullTopFeed();
            List<Submission> newsfeed = pullTopFeed();
            for(Submission sub : topfeed)
            {
                channel.sendMessage((String)sub.getTitle()+"\n").queue();
                channel.sendMessage("r/"+(String)sub.getSubreddit()+"\n").queue();
                channel.sendMessage((String)sub.getUrl()).queue();
                channel.sendMessage("--------------------------------------------------" +
                        "-------------------------------------------------").queue();

            }
        }
        if (msg.getContentRaw().equals("!rnews"))
        {
            MessageChannel channel = event.getChannel();
            List<Submission> newsfeed = pullNewsFeed();
            for(Submission sub : newsfeed)
            {
                channel.sendMessage((String)sub.getTitle()+"\n").queue();
                channel.sendMessage("r/"+(String)sub.getSubreddit()+"\n").queue();
                channel.sendMessage((String)sub.getUrl()).queue();
                channel.sendMessage("--------------------------------------------------" +
                        "-------------------------------------------------").queue();

            }
        }
    }
    List<Submission> pullTopFeed(){
        // Initialize REST Client
        RestClient restClient = new HttpRestClient();
        restClient.setUserAgent("news bot");
        // Connect the user
        com.github.jreddit.entity.User user = new User(restClient, "jokesterae", "");
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
    List<Submission> pullNewsFeed(){
        // Initialize REST Client
        RestClient restClient = new HttpRestClient();
        restClient.setUserAgent("bot/1.0 by name");
        // Connect the user
        com.github.jreddit.entity.User user = new User(restClient, "", "");
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
