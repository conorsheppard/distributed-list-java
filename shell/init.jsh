import com.conorsheppard.distributedlist.list.DistributedList;
import com.conorsheppard.distributedlist.serializers.IntegerSerializer;
import com.conorsheppard.distributedlist.serializers.StringSerializer;
import com.conorsheppard.distributedlist.store.RedisStoreClient;

    var stringSerializer = new StringSerializer();
var intSerializer = new IntegerSerializer();

var redisStoreClient = new RedisStoreClient("localhost", 6379);

var distributedList = new DistributedList<>(redisStoreClient, "jshell-list", intSerializer, stringSerializer);