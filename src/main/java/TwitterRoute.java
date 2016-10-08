// add codice copyright

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import twitter4j.Status;

public class TwitterRoute {

    // Twitter API Properties
    private static final String CONSUMER_KEY = "consumerKey";
    private static final String CONSUMER_SECRET = "consumerSecret";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";

    // Gmail Properties
    private static final String FROM_EMAIL = "fromEmail";
    private static final String FROM_EMAIL_PASSWORD = "fromEmailPassword";
    private static final String TO_EMAIL = "toEmail";

    private static final String SEARCH_KEYWORDS = "connexta";
    private static final long POLLING_RATE_MILLIS = TimeUnit.SECONDS.toMillis(30);

    private static String consumerKey;
    private static String consumerSecret;
    private static String accessToken;
    private static String accessTokenSecret;

    private static String fromEmail;
    private static String fromEmailPassword;
    private static String toEmail;

    public static void main(String args[]) throws Exception {
        System.out.println(TwitterRoute.class.getResource("TwitterRoute.class"));
        loadProperties();

        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        final String twitterEndpoint = buildTwitterEndpoint();
        final String emailEndpoint = buildEmailEndpoint();

        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                // begin consuming tweets
                from(twitterEndpoint)
                        // Log message body (tweets)
                        .to("log:" + getClass().getName() + "?showBody=true&level=ERROR")
                        // set email subject
                        .setHeader("subject", simple("${in.body.user.name}"))
                        // set email body
                        .setBody(simple(
                                "${in.body.text}\n${in.body.geoLocation}\n${in.body.createdAt}\nhttps://twitter.com/${in.body.user.id}/status/${in.body.id}"))
                        // send to email
                        .to(emailEndpoint);
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(100000);

        // stop the CamelContext
        context.stop();
    }


    /*
     * Utility methods
     */
    private static String buildTwitterEndpoint() {
        StringBuilder twitterBuilder = new StringBuilder();
        twitterBuilder.append("twitter://search?type=polling&delay=");
        twitterBuilder.append(POLLING_RATE_MILLIS);
        twitterBuilder.append("&keywords=");
        twitterBuilder.append(SEARCH_KEYWORDS);
        twitterBuilder.append("&consumerKey=");
        twitterBuilder.append(consumerKey);
        twitterBuilder.append("&consumerSecret=");
        twitterBuilder.append(consumerSecret);
        twitterBuilder.append("&accessToken=");
        twitterBuilder.append(accessToken);
        twitterBuilder.append("&accessTokenSecret=");
        twitterBuilder.append(accessTokenSecret);
        return twitterBuilder.toString();
    }

    private static String buildEmailEndpoint() {
        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append("smtps://smtp.gmail.com:465?username=");
        emailBuilder.append(fromEmail);
        emailBuilder.append("&password=");
        emailBuilder.append(fromEmailPassword);
        emailBuilder.append("&to=");
        emailBuilder.append(toEmail);
        return emailBuilder.toString();
    }

    private static void loadProperties() throws Exception {
        Properties twitterProperties = new Properties();
        try (InputStream is = TwitterRoute.class.getResourceAsStream("twitterapi.properties")) {
            twitterProperties.load(is);
        }

        consumerKey = twitterProperties.getProperty(CONSUMER_KEY);
        consumerSecret = twitterProperties.getProperty(CONSUMER_SECRET);
        accessToken = twitterProperties.getProperty(ACCESS_TOKEN);
        accessTokenSecret = twitterProperties.getProperty(ACCESS_TOKEN_SECRET);

        Properties gmailProperties = new Properties();
        try (InputStream is = TwitterRoute.class.getResourceAsStream("gmail.properties")) {
            gmailProperties.load(is);
        }

        fromEmail = gmailProperties.getProperty(FROM_EMAIL);
        fromEmailPassword = gmailProperties.getProperty(FROM_EMAIL_PASSWORD);
        toEmail = gmailProperties.getProperty(TO_EMAIL);
    }

}
