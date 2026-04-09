package edu.moravian.secretbot;

import redis.clients.jedis.Jedis;

public class Responder
{
    private Jedis jedis;

    public Responder(Jedis jedis)
    {
        this.jedis = jedis;
    }

    public String respond(String input)
    {
        if (!jedis.exists("secrets")) {
            return "I don't know any secrets.  Try again later.";
        }

        if (!jedis.hexists("secrets", input))
        {
            return "I don't know THAT secret - but I do know " + jedis.hlen("secrets") + " secrets.";
        }

        return jedis.hget("secrets", input) + " (SSSSHHHH!!! It's a secret!)";
    }
}
