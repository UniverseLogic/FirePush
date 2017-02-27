package com.universe.logic;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;
import com.typesafe.config.ConfigFactory;
import com.universe.logic.actors.sharding.PersonActor;
import com.universe.logic.message.RouteMessage;
import com.universe.logic.publishers.MessagePublisher;

/**
 * Created by frank_zhao on 2017/2/26.
 */
public class AppSystem {

    private final ActorSystem actorSystem;
    private final ActorRef publisherActor;
    private final String personShardingName = "PERSON";


    public AppSystem() {
        actorSystem = ActorSystem.create("pushSystem", ConfigFactory.load());
        publisherActor = actorSystem.actorOf(Props.create(MessagePublisher.class), "PUBLISHER");
        ClusterSharding.get(actorSystem).start(personShardingName, Props.create(PersonActor.class),
                ClusterShardingSettings.create(actorSystem), personExtractor);

    }


    public ActorSystem actorSystem() {
        return this.actorSystem;
    }

    public ActorRef personActor() {
        return ClusterSharding.get(this.actorSystem).shardRegion(personShardingName);
    }

    public ActorRef publisherActor() {
        return this.publisherActor;
    }

    ShardRegion.MessageExtractor personExtractor = new ShardRegion.MessageExtractor() {
        @Override
        public String entityId(Object message) {
            if (message instanceof RouteMessage) {
                return ((RouteMessage) message).getUserId().toString();
            }
            return null;
        }

        @Override
        public Object entityMessage(Object message) {
            return message;
        }

        @Override
        public String shardId(Object message) {
            if (message instanceof RouteMessage) {
                return String.valueOf(((RouteMessage) message).getUserId().toString().hashCode() % 10);
            }
            return null;
        }
    };

    public static AppSystem getAppServer() {
        return AppSystemSingleton.APP_SERVER;
    }

    private static class AppSystemSingleton {
        private static final AppSystem APP_SERVER = new AppSystem();
    }

}
