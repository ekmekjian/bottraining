import events.RedditEvent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws LoginException
    {
        final String token = "NzI2ODMwMDIzMjgxMDgyNTIw.Xvi_NQ.H_cF6AzuukTZezM-Mcbi6r6vAJw";
        new JDABuilder(token)
                .addEventListeners(new RedditEvent())
                .setActivity(Activity.playing("Searching for John Conner"))
                .build();
    }



}
