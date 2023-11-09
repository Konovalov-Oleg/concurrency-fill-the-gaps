package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicMarkableReference<Bid> latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, Long.MIN_VALUE), false);

    public boolean propose(Bid bid) {
        boolean[] markHolder = new boolean[1];
        Bid latestValue;
        do {
            latestValue = latestBid.get(markHolder);
            if ((bid.getPrice() <= latestValue.getPrice()) || markHolder[0]) {
                return false;
            }
        } while (!latestBid.compareAndSet(latestValue, bid, false, false));

        notifier.sendOutdatedMessage(latestValue);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get(new boolean[1]);
    }

    public Bid stopAuction() {
        boolean[] markHolder = new boolean[1];
        Bid latestValue;
        do {
            latestValue = latestBid.get(markHolder);
        } while (!latestBid.attemptMark(latestValue, true));
        return latestValue;
    }
}