import com.conorsheppard.distributedlist.*;
import redis.clients.jedis.Jedis;

var storeClient = new RedisStoreClient("localhost", 6379);
var distributedList = new DistributedList<String>(storeClient, "jshell-list");

