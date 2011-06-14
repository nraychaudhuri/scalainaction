package countwords;

import akka.actor.UntypedActor;

public class WordCountWorker extends UntypedActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof FileToCount) {
            FileToCount c = (FileToCount)message;
            Integer count = c.countWords();
            getContext().replyUnsafe(new WordCount(c.url(), count));
        }
        else {
            throw new IllegalArgumentException("Unknown message: " + message);
        }
    }
}
