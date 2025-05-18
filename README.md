# 💻 🔁 💻 Distributed List

![Coverage](./badges/jacoco.svg)

In the demo video below, I start up two independent JShell sessions in two separate shells.  
You can see that each has its own unique _JShell Session ID_ which I generate in the `startup.jsh` script which is executed automatically before the session is opened.  
An `init.jsh` script is also executed after the JShell session is open, this script instantiates some classes for us.  
I then execute `make` which boots a Redis container which the application can interact with through the [RedisStoreClient.java](src/main/java/com/conorsheppard/distributedlist/RedisStoreClient.java) implementation of the [StoreClient.java](src/main/java/com/conorsheppard/distributedlist/StoreClient.java) interface.  
You then see that I can add and remove from the list and the changes are global to any client which holds a reference to the same [DistributedList.java](src/main/java/com/conorsheppard/distributedlist/DistributedList.java).  
Finally, I switch to the shell session on the left and exec into the Redis Docker container, showing that the changes can also be observed by executing commands directly on the Redis instance.

https://github.com/user-attachments/assets/afdba511-9cc7-4c3f-9ca8-0aefbdbc448e