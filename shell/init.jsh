import com.conorsheppard.distributedlist.*;
import redis.clients.jedis.Jedis;

var stringSerializer = new StringSerializer();
var intSerializer = new IntegerSerializer();

var redisStoreClient = new RedisStoreClient("localhost", 6379);

var distributedList = new DistributedList<Integer, String>(redisStoreClient, "jshell-list", intSerializer, stringSerializer);

