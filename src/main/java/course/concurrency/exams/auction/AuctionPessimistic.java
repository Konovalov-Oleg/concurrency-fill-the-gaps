package course.concurrency.exams.auction;

public class AuctionPessimistic implements Auction {

    private final Object lock = new Object();

    private final Notifier notifier;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(0L, 0L, Long.MIN_VALUE);

    public boolean propose(Bid bid) {
        if (bid.getPrice() <= latestBid.getPrice()) {
            return false;
        }
        Bid latestValue;
        synchronized (lock) {
            if (bid.getPrice() <= latestBid.getPrice()) {
                return false;
            }
            latestValue = latestBid = bid;
        }
        notifier.sendOutdatedMessage(latestValue);
        return true;
    }

//    public boolean propose(Bid bid) {
//        if (bid.getPrice() <= latestBid.getPrice()) {
//            return false;
//        }
//        synchronized (lock) {
//            if (bid.getPrice() > latestBid.getPrice()) {
//                notifier.sendOutdatedMessage(latestBid);
//                latestBid = bid;
//                return true;
//            }
//        }
//        return false;
//    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
