package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(new Bid(0L, 0L, Long.MIN_VALUE));

    private volatile boolean isStop;

    public boolean propose(Bid bid) {
        Bid latestValue;
        do {
            latestValue = latestBid.get();
            if ((bid.getPrice() <= latestValue.getPrice()) || isStop) {
                return false;
            }
        } while (isStop || !latestBid.compareAndSet(latestValue, bid));
        notifier.sendOutdatedMessage(latestValue);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }

    public Bid stopAuction() {
        isStop = true;
        return latestBid.get();
    }
}
