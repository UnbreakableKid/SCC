package war;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class RedisConnector {
    
    public Jedis createClient() {
        String RedisHostname = "scc56982.redis.cache.windows.net";
        String cacheKey = "2pHa2ub1Z7RuYiBLpD60B1sbdAG7yb6x91o6jEqoHmc=";
        JedisShardInfo shardInfo = new JedisShardInfo(RedisHostname, 6380, true);
        shardInfo.setPassword(cacheKey);

        Jedis jedis = new Jedis(shardInfo);
        return jedis;
    }
}