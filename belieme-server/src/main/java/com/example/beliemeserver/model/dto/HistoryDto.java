package com.example.beliemeserver.model.dto;

import lombok.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public record HistoryDto(
        @NonNull ItemDto item, int num, UserDto requester, UserDto approveManager,
        UserDto returnManager, UserDto lostManager, UserDto cancelManager,
        long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp,
        long lostTimeStamp, long cancelTimeStamp
) {
    public static final HistoryDto nestedEndpoint = new HistoryDto(ItemDto.nestedEndpoint, 0, null, null, null, null, null, 0, 0, 0, 0, 0);

    public HistoryDto withItem(@NonNull ItemDto item) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withNum(int num) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withRequester(UserDto requester) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withApproveManager(UserDto approveManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withReturnManager(UserDto returnManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withLostManager(UserDto lostManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withCancelManager(UserDto cancelManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withReservedTimeStamp(long reservedTimeStamp) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withApproveTimeStamp(long approveTimeStamp) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withReturnTimeStamp(long returnTimeStamp) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withLostTimeStamp(long lostTimeStamp) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public HistoryDto withCancelTimeStamp(long cancelTimeStamp) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                reservedTimeStamp, approveTimeStamp, returnTimeStamp,
                lostTimeStamp, cancelTimeStamp);
    }

    public boolean matchUniqueKey(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, int num
    ) {
        return this.item().matchUniqueKey(
                universityCode, departmentCode, stuffName, itemNum)
                && this.num() == num;
    }

    public boolean matchUniqueKey(HistoryDto oth) {
        if (oth == null) return false;
        return this.item().matchUniqueKey(oth.item())
                && this.num == oth.num;
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "HistoryDto{" +
                "item=" + item +
                ", num=" + num +
                ", requester=" + requester +
                ", approveManager=" + approveManager +
                ", returnManager=" + returnManager +
                ", lostManager=" + lostManager +
                ", cancelManager=" + cancelManager +
                ", reservedTimeStamp=" + reservedTimeStamp +
                ", approveTimeStamp=" + approveTimeStamp +
                ", returnTimeStamp=" + returnTimeStamp +
                ", lostTimeStamp=" + lostTimeStamp +
                ", cancelTimeStamp=" + cancelTimeStamp +
                '}';
    }

    public HistoryDto.HistoryStatus status() {
        if(isRequested()) return HistoryStatus.REQUESTED;
        if(isUsing()) return HistoryStatus.USING;
        if(isDelayed()) return HistoryStatus.DELAYED;
        if(isLost()) return HistoryStatus.LOST;
        if(isReturned()) return HistoryStatus.RETURNED;
        if(isFound()) return HistoryStatus.FOUND;
        if(isExpired()) return HistoryStatus.EXPIRED;
        return HistoryStatus.ERROR;
    }

    private boolean isRequested() {
        return reservedTimeStamp != 0 && approveTimeStamp == 0
                && returnTimeStamp == 0 && lostTimeStamp == 0
                && cancelTimeStamp == 0 && expiredTime() > currentTime();
    }

    private boolean isExpired() {
        boolean isExpired = (reservedTimeStamp != 0 && approveTimeStamp == 0
                && returnTimeStamp == 0 && lostTimeStamp == 0
                && cancelTimeStamp == 0 && expiredTime() <= currentTime());
        boolean isCanceled = (reservedTimeStamp != 0 && approveTimeStamp == 0
                && returnTimeStamp == 0 && lostTimeStamp == 0
                && cancelTimeStamp != 0);

        return (isExpired || isCanceled);
    }

    private boolean isUsing() {
        return (reservedTimeStamp != 0 && approveTimeStamp != 0
                && returnTimeStamp == 0 && lostTimeStamp == 0
                && cancelTimeStamp == 0 && dueTime() > currentTime());
    }

    private boolean isDelayed() {
        return (reservedTimeStamp != 0 && approveTimeStamp != 0
                && returnTimeStamp == 0 && lostTimeStamp == 0
                && cancelTimeStamp == 0 && dueTime() <= currentTime());
    }

    private boolean isLost() {
        boolean isLostOnStorage = (reservedTimeStamp == 0 && approveTimeStamp == 0
                && returnTimeStamp == 0 && lostTimeStamp != 0 && cancelTimeStamp == 0);
        boolean isLostOnRental = (reservedTimeStamp != 0 && approveTimeStamp != 0
                && returnTimeStamp == 0 && lostTimeStamp != 0 && cancelTimeStamp == 0);

        return (isLostOnStorage || isLostOnRental);
    }

    private boolean isReturned() {
        return (reservedTimeStamp != 0 && approveTimeStamp != 0
                && returnTimeStamp != 0 && lostTimeStamp == 0
                && cancelTimeStamp == 0);
    }

    private boolean isFound() {
        boolean isReturnedAfterLostOnStorage = reservedTimeStamp == 0 && approveTimeStamp == 0
                && returnTimeStamp != 0 && lostTimeStamp != 0 && cancelTimeStamp == 0;
        boolean isReturnedAfterLostOnRental =  reservedTimeStamp != 0 && approveTimeStamp != 0
                && returnTimeStamp != 0 && lostTimeStamp != 0 && cancelTimeStamp == 0;

        return isReturnedAfterLostOnStorage || isReturnedAfterLostOnRental;
    }

    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public long expiredTime() {
        return reservedTimeStamp + 15 * 60;
    }

    public long dueTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar tmp = Calendar.getInstance();

        tmp.setTime(new Date(approveTimeStamp * 1000));
        tmp.setTimeZone(timeZone);
        tmp.add(Calendar.DATE, 7);
        if (tmp.get(Calendar.HOUR_OF_DAY) > 18) {
            tmp.add(Calendar.DATE, 1);
        }
        tmp.set(Calendar.HOUR_OF_DAY, 17);
        tmp.set(Calendar.MINUTE, 59);
        tmp.set(Calendar.SECOND, 59);
        if (tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tmp.add(Calendar.DATE, 2);
        } else if (tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tmp.add(Calendar.DATE, 1);
        }
        return tmp.getTime().getTime() / 1000;
    }

    public enum HistoryStatus {
        REQUESTED, USING, DELAYED, LOST, EXPIRED, RETURNED, FOUND, ERROR;

        public boolean isClosed() {
            return (this == EXPIRED) || (this == RETURNED) || (this == FOUND);
        }

        public boolean isOpen() {
            return !isClosed() && (this != ERROR);
        }
    }
}
