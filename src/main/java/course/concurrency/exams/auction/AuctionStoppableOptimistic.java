package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicMarkableReference<Bid> latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, Long.MIN_VALUE), false);

    public boolean propose(Bid bid) {
        Bid latestValue;
        do {
            latestValue = latestBid.getReference();
            if ((bid.getPrice() <= latestValue.getPrice()) || latestBid.isMarked()) {
                return false;
            }
        } while (!latestBid.compareAndSet(latestValue, bid, false, false));

        notifier.sendOutdatedMessage(latestValue);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        Bid latestValue;
        do {
            latestValue = latestBid.getReference();
        } while (!latestBid.attemptMark(latestValue, true));
        return latestValue;
    }
}