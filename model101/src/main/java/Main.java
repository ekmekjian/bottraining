import events.SocialEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[]args) throws LoginException, InterruptedException {
        final String token = "NzI2ODMwMDIzMjgxMDgyNTIw.XvkOMw.UBn3fFUA7-LM3-rVoknIMIhsmPE";
        JDA jda = new JDABuilder(token).addEventListeners(new SocialEvent()).build();

        jda.awaitReady();
    }
}
