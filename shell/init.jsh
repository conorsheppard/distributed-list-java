import com.conorsheppard.distributedlist.*;

var stringSerializer = new StringSerializer();
var intSerializer = new IntegerSerializer();

var redisStoreClient = new RedisStoreClient("localhost", 6379);

var distributedList = new DistributedList<>(redisStoreClient, "jshell-list", intSerializer, stringSerializer);