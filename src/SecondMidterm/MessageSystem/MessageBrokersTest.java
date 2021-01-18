package SecondMidterm.MessageSystem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PartitionDoesNotExistException extends Exception {
    PartitionDoesNotExistException (String topic, int partition) {
        super(String.format("The topic %s does not have a partition with number %d", topic, partition));
    }
}

class UnsupportedOperationException extends Exception {

    public UnsupportedOperationException(String s) {
        super(s);
    }
}

class Message implements Comparable<Message>{
    LocalDateTime timestamp;
    String message;
    Integer partition;
    String key;

    public Message(LocalDateTime timestamp, String message, Integer partition, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.partition = partition;
        this.key = key;
    }

    public Message(LocalDateTime timestamp, String message, String key) {
        this.timestamp = timestamp;
        this.message = message;
        this.key = key;
    }


    @Override
    public int compareTo(Message message) {
        return this.timestamp.compareTo(message.timestamp);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPartition() {
        return partition;
    }

    public String getKey() {
        return key;
    }
}

class Topic {
    private String topicName;
    private int partitionsCount;
    private TreeMap<Integer, TreeSet<Message>> messagesByPartition;

    public Topic(String topicName, int partitionsCount) {
        this.topicName = topicName;
        this.partitionsCount = partitionsCount;
        messagesByPartition = new TreeMap<>();
        fillPartitions();
    }

    public void fillPartitions() {
        IntStream.range(1, partitionsCount+1)
                .forEach(i -> this.messagesByPartition.put(i, new TreeSet<>()));
    }

    public void addMessage (Message message) throws PartitionDoesNotExistException {
        Integer messagePartition = message.getPartition();
        if(messagePartition == null){
            messagePartition = PartitionAssigner.assignPartition(message, partitionsCount);
        }
        if (!this.messagesByPartition.containsKey(messagePartition))
            throw new PartitionDoesNotExistException(this.topicName, messagePartition);
        this.messagesByPartition.computeIfPresent(messagePartition, (key, value) -> {
            if (value.size() == MessageBroker.capacityPerTopic)
                value.remove(value.first());
            value.add(message);
            return value;
        });
    }

    public void changeNumberOfPartitions(int newPartitionsNumber) throws UnsupportedOperationException {
        if (newPartitionsNumber < this.partitionsCount)
            throw new UnsupportedOperationException("Partitions number cannot be decreased!");

        int difference = newPartitionsNumber - this.partitionsCount;
        int size = this.messagesByPartition.size();
        IntStream.range(1, difference + 1)
                .forEach(i -> this.messagesByPartition.putIfAbsent(size + i, new TreeSet<>()));
        this.partitionsCount = newPartitionsNumber;
    }

    public String toString() {
        return String.format("Topic: %10s Partitions: %5d\n%s",
                topicName,
                partitionsCount,
                messagesByPartition.entrySet().stream().map(entry -> String.format("%2d : Count of messages: %5d\n%s",
                        entry.getKey(),
                        entry.getValue().size(),
                        !entry.getValue().isEmpty() ?
                                "Messages:\n" + entry.getValue().stream().map(Message::toString).collect(Collectors.joining("\n")) : "")
                ).collect(Collectors.joining("\n"))
        );
    }

}

class MessageBroker {
    Map<String, Topic> topicMap;
    static LocalDateTime minimumDate;
    static Integer capacityPerTopic;

    public MessageBroker(LocalDateTime minimumDate, Integer capacityPerTopic) {
        topicMap = new TreeMap<>();
        MessageBroker.minimumDate = minimumDate;
        MessageBroker.capacityPerTopic = capacityPerTopic;

    }

    public void addTopic (String topic, int partitionsCount) {
        topicMap.put(topic, new Topic(topic, partitionsCount));
    }

    public void addMessage (String topic, Message message) throws UnsupportedOperationException, PartitionDoesNotExistException {
        if (message.getTimestamp().isBefore(minimumDate))
            return ;

        topicMap.get(topic).addMessage(message);
    }

    public void changeTopicSettings (String topic, int partitionsCount) throws UnsupportedOperationException {
        topicMap.get(topic).changeNumberOfPartitions(partitionsCount);
    }

    public String toString() {
        return String.format("Broker with %2d topics:\n%s",
                topicMap.size(),
                topicMap.values().stream().map(Topic::toString).collect(Collectors.joining("\n"))
        );
    }
}



class PartitionAssigner {
    public static Integer assignPartition (Message message, int partitionsCount) {
        return (Math.abs(message.getKey().hashCode())  % partitionsCount) + 1;
    }
}

public class MessageBrokersTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String date = sc.nextLine();
        LocalDateTime localDateTime =LocalDateTime.parse(date);
        Integer partitionsLimit = Integer.parseInt(sc.nextLine());
        MessageBroker broker = new MessageBroker(localDateTime, partitionsLimit);
        int topicsCount = Integer.parseInt(sc.nextLine());

        //Adding topics
        for (int i=0;i<topicsCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            int partitionsCount = Integer.parseInt(parts[1]);
            broker.addTopic(topicName, partitionsCount);
        }

        //Reading messages
        int messagesCount = Integer.parseInt(sc.nextLine());

        System.out.println("===ADDING MESSAGES TO TOPICS===");
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER ADDITION OF MESSAGES===");
        System.out.println(broker);

        System.out.println("===CHANGE OF TOPICS CONFIGURATION===");
        //topics changes
        int changesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<changesCount;i++){
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topicName = parts[0];
            Integer partitions = Integer.parseInt(parts[1]);
            try {
                broker.changeTopicSettings(topicName, partitions);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("===ADDING NEW MESSAGES TO TOPICS===");
        messagesCount = Integer.parseInt(sc.nextLine());
        for (int i=0;i<messagesCount;i++) {
            String line = sc.nextLine();
            String [] parts = line.split(";");
            String topic = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1]);
            String message = parts[2];
            if (parts.length==4) {
                String key = parts[3];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                Integer partition = Integer.parseInt(parts[3]);
                String key = parts[4];
                try {
                    broker.addMessage(topic, new Message(timestamp,message,partition,key));
                } catch (UnsupportedOperationException | PartitionDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.println("===BROKER STATE AFTER CONFIGURATION CHANGE===");
        System.out.println(broker);


    }
}
