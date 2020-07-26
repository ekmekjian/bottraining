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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RedditEvent extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event)
    {

        Message input = event.getMessage();
        String msg [] = input.getContentRaw().split(" ");
        // Initialize REST Client
        RestClient restClient = new HttpRestClient();
        restClient.setUserAgent("news bot");
        // Connect the user
        com.github.jreddit.entity.User user = new User(restClient, "", "");


        if (msg[0].equals("!rmeme"))
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
        if (msg[0].equals("!rnews"))
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
                ;

            }
        }
        if(msg[0].equals("!help"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Use:\n!rnews - to pull articles from r/news\n!rmeme - "+
            "to pull memes from r/comedyhomicide\n!search - to pull first 5 links from duckduck got ex: !search pummel party"+
            "\n!8ball - shake the 8 ball while keeping your question in mind then press enter to get your answer").queue();
        }
        if(msg[0].equals("!search"))
        {
            MessageChannel channel = event.getChannel();
            try {botSearch(channel,msg);} catch (IOException e) { e.printStackTrace();}

        }
        if(msg[0].equals("!8ball")){
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Asking the 🎱 your question..").queue();
            shake8ball(channel);
        }
    }
    //method for !8ball
    void shake8ball(MessageChannel ch)
    {
        int min,max;
        Random rand = new Random();
        min = 0;
        List<String> predictions = new ArrayList<String>();
        predictions.add("As I see it, yes");
        predictions.add("Ask again later");
        predictions.add("Better not tell you now");
        predictions.add("Cannot predict now");
        predictions.add("Concentrate and ask again");
        predictions.add("Don’t count on it");
        predictions.add("It is certain");
        predictions.add("It is decidedly so");
        predictions.add("Most likely");
        predictions.add("My reply is no");
        predictions.add("My sources say no");
        predictions.add("Outlook good");
        predictions.add("Outlook not so good");
        predictions.add("Reply hazy try again");
        predictions.add("Signs point to yes");
        predictions.add("Very doubtful");
        predictions.add("Without a doubt");
        predictions.add("Yes");
        predictions.add("Yes, definitely");
        predictions.add("You may rely on it");
        max = predictions.size();
        ch.sendMessage(predictions.get(rand.nextInt((max - min) + 1) + min)).queue();


    }
    //method for !search
    void botSearch(MessageChannel ch,String[]msg) throws IOException
    {
        Document doc;
        String title,url,urlend,htmlfeed;
        int counter=0;
        urlend =String.join("+",Arrays.copyOfRange(msg,1,msg.length));
        htmlfeed="https://duckduckgo.com/html/?q="+urlend;
        doc =  Jsoup.connect(htmlfeed).get();
        Elements links = doc.select("div.result");
        for (Element link : links) {
           if(counter<5){ 
           title = link.select("h2.result__title>a").first().text();
           url = link.select("div.result__extras__url>a.result__url").first().attr("href");
           ch.sendMessage(title).queue();
           ch.sendMessage(url).queue();
           ch.sendMessage("-----------------------------------------------------").queue();
           }
           counter++;
        }
        
    }
    //method for !rmeme
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
    //method for !rnews
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
