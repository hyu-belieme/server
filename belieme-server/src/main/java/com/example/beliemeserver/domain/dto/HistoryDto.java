package com.example.beliemeserver.domain.dto;

import com.example.beliemeserver.domain.dto.enumeration.HistoryStatus;
import lombok.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public record HistoryDto(
        @NonNull ItemDto item, int num, UserDto requester, UserDto approveManager,
        UserDto returnManager, UserDto lostManager, UserDto cancelManager,
        long requestedAt, long approvedAt, long returnedAt,
        long lostAt, long canceledAt
) {
    public static final HistoryDto nestedEndpoint = new HistoryDto(ItemDto.nestedEndpoint, 0, null, null, null, null, null, 0, 0, 0, 0, 0);

    public HistoryDto withItem(@NonNull ItemDto item) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withNum(int num) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withRequester(UserDto requester) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withApproveManager(UserDto approveManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withReturnManager(UserDto returnManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withLostManager(UserDto lostManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withCancelManager(UserDto cancelManager) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withRequestedAt(long requestedAt) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withApprovedAt(long approvedAt) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withReturnedAt(long returnedAt) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withLostAt(long lostAt) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
    }

    public HistoryDto withCanceledAt(long canceledAt) {
        return new HistoryDto(item, num, requester, approveManager,
                returnManager, lostManager, cancelManager,
                requestedAt, approvedAt, returnedAt,
                lostAt, canceledAt);
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
                ", requestedAt=" + requestedAt +
                ", approvedAt=" + approvedAt +
                ", returnedAt=" + returnedAt +
                ", lostAt=" + lostAt +
                ", canceledAt=" + canceledAt +
                '}';
    }

    public HistoryStatus status() {
        if (isRequested()) return HistoryStatus.REQUESTED;
        if (isUsing()) return HistoryStatus.USING;
        if (isDelayed()) return HistoryStatus.DELAYED;
        if (isLost()) return HistoryStatus.LOST;
        if (isReturned()) return HistoryStatus.RETURNED;
        if (isFound()) return HistoryStatus.FOUND;
        if (isExpired()) return HistoryStatus.EXPIRED;
        return HistoryStatus.ERROR;
    }

    private boolean isRequested() {
        return requestedAt != 0 && approvedAt == 0
                && returnedAt == 0 && lostAt == 0
                && canceledAt == 0 && expiredTime() > currentTime();
    }

    private boolean isExpired() {
        boolean isExpired = (requestedAt != 0 && approvedAt == 0
                && returnedAt == 0 && lostAt == 0
                && canceledAt == 0 && expiredTime() <= currentTime());
        boolean isCanceled = (requestedAt != 0 && approvedAt == 0
                && returnedAt == 0 && lostAt == 0
                && canceledAt != 0);

        return (isExpired || isCanceled);
    }

    private boolean isUsing() {
        return (requestedAt != 0 && approvedAt != 0
                && returnedAt == 0 && lostAt == 0
                && canceledAt == 0 && dueTime() > currentTime());
    }

    private boolean isDelayed() {
        return (requestedAt != 0 && approvedAt != 0
                && returnedAt == 0 && lostAt == 0
                && canceledAt == 0 && dueTime() <= currentTime());
    }

    private boolean isLost() {
        boolean isLostOnStorage = (requestedAt == 0 && approvedAt == 0
                && returnedAt == 0 && lostAt != 0 && canceledAt == 0);
        boolean isLostOnRental = (requestedAt != 0 && approvedAt != 0
                && returnedAt == 0 && lostAt != 0 && canceledAt == 0);

        return (isLostOnStorage || isLostOnRental);
    }

    private boolean isReturned() {
        return (requestedAt != 0 && approvedAt != 0
                && returnedAt != 0 && lostAt == 0
                && canceledAt == 0);
    }

    private boolean isFound() {
        boolean isReturnedAfterLostOnStorage = requestedAt == 0 && approvedAt == 0
                && returnedAt != 0 && lostAt != 0 && canceledAt == 0;
        boolean isReturnedAfterLostOnRental = requestedAt != 0 && approvedAt != 0
                && returnedAt != 0 && lostAt != 0 && canceledAt == 0;

        return isReturnedAfterLostOnStorage || isReturnedAfterLostOnRental;
    }

    private long currentTime() {
        return System.currentTimeMillis() / 1000;
    }

    private long expiredTime() {
        return requestedAt + 15 * 60;
    }

    private long dueTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(approvedAt * 1000));
        calendar.setTimeZone(timeZone);

        moveTo7DayAfter17_59(calendar);
        moveToWeekDay(calendar);
        return calendar.getTime().getTime() / 1000;
    }

    private void moveToWeekDay(Calendar calendar) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
            return;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
    }

    private void moveTo7DayAfter17_59(Calendar calendar) {
        calendar.add(Calendar.DATE, 7);
        if (calendar.get(Calendar.HOUR_OF_DAY) > 18) {
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
    }
}
