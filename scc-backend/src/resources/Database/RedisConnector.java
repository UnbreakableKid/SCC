package resources.Database;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class RedisConnector {
    
    public Jedis createClient() {
        String RedisHostname = "scc-backend-56982-redis";
        JedisShardInfo shardInfo = new JedisShardInfo(RedisHostname, 6379, true);

        Jedis jedis = new Jedis(shardInfo);
        return jedis;
    }
}