package edu.moravian.secretbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class SecretBot
{
    public static void main(String[] args)
    {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        String channelName = dotenv.get("CHANNEL_NAME");

        if (channelName == null)
        {
            System.out.println("CHANNEL_NAME is not set in the .env file");
            return;
        }

        if (token == null)
        {
            System.out.println("DISCORD_TOKEN is not set in the .env file");
            return;
        }

        Jedis jedis = null;
        
        try
        {
            jedis = new Jedis("localhost", 6379);
            jedis.ping(); // Try to communicate with Redis
        }
        catch (JedisConnectionException e) {
            System.out.println("Error: Unable to connect to Redis. Ensure the Redis server is running on localhost:6379.");
            return;
        } 
        Responder responder = new Responder(jedis);

        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        api.addEventListener(new ListenerAdapter()
        {
            @Override
            public void onMessageReceived(MessageReceivedEvent event)
            {
                if (event.getAuthor().isBot())
                    return;

                if (!event.getChannel().getName().equals(channelName))
                    return;

                String message = event.getMessage().getContentRaw();


                String response = responder.respond(message);
                if (!response.isEmpty())
                    event.getChannel().sendMessage(response).queue();    
            }
        });
    }
}
