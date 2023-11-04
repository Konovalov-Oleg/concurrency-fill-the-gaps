package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(null);

    public boolean propose(Bid bid) {
        Bid latestValue = null;
        while ((latestBid.get() == null) || (bid.getPrice() > (latestValue = latestBid.get()).getPrice())) {
            if (latestBid.compareAndSet(latestValue, bid)) {
                notifier.sendOutdatedMessage(latestValue);
                return true;
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
