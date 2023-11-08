package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Object lock = new Object();

    private final Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(0L, 0L, Long.MIN_VALUE);

    private volatile boolean isStop;

    public boolean propose(Bid bid) {
        if ((bid.getPrice() <= latestBid.getPrice()) || (isStop)) {
            return false;
        }
        Bid latestValue;
        synchronized (lock) {
            if ((bid.getPrice() <= latestBid.getPrice()) || (isStop)) {
                return false;
            }
            latestValue = latestBid = bid;
        }

        if (isStop) {
            return false;
        } else {
            notifier.sendOutdatedMessage(latestValue);
            return true;
        }
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
            isStop = true;
            return latestBid;
    }
}
