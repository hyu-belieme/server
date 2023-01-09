package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.catalina.User;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class HistoryDto {
    @NonNull
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ItemDto item;

    private int num;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserDto requester;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserDto approveManager;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserDto returnManager;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserDto lostManager;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private UserDto cancelManager;

    private long reservedTimeStamp;
    private long approveTimeStamp;
    private long returnTimeStamp;
    private long lostTimeStamp;
    private long cancelTimeStamp;

    public HistoryDto(@NonNull HistoryDto historyDto) {
        this.item = historyDto.getItem();
        this.num = historyDto.getNum();
        this.requester = historyDto.getRequester();
        this.approveManager = historyDto.getApproveManager();
        this.returnManager = historyDto.getReturnManager();
        this.lostManager = historyDto.getLostManager();
        this.cancelManager = historyDto.getCancelManager();
        this.reservedTimeStamp = historyDto.getReservedTimeStamp();
        this.approveTimeStamp = historyDto.getApproveTimeStamp();
        this.returnTimeStamp = historyDto.getReturnTimeStamp();
        this.lostTimeStamp = historyDto.getLostTimeStamp();
        this.cancelTimeStamp = historyDto.getCancelTimeStamp();
    }

    public HistoryDto(@NonNull ItemDto item, int num, UserDto requester, UserDto approveManager, UserDto returnManager, UserDto lostManager, UserDto cancelManager, long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp, long lostTimeStamp, long cancelTimeStamp) {
        setItem(item);
        setNum(num);
        setRequester(requester);
        setApproveManager(approveManager);
        setReturnManager(returnManager);
        setLostManager(lostManager);
        setCancelManager(cancelManager);
        setReservedTimeStamp(reservedTimeStamp);
        setApproveTimeStamp(approveTimeStamp);
        setReturnTimeStamp(returnTimeStamp);
        setLostTimeStamp(lostTimeStamp);
        setCancelTimeStamp(cancelTimeStamp);
    }

    public ItemDto getItem() {
        return new ItemDto(item);
    }

    public UserDto getRequester() {
        return copyUserDtoOrNull(requester);
    }

    public UserDto getApproveManager() {
        return copyUserDtoOrNull(approveManager);
    }

    public UserDto getReturnManager() {
        return copyUserDtoOrNull(returnManager);
    }

    public UserDto getLostManager() {
        return copyUserDtoOrNull(lostManager);
    }

    public UserDto getCancelManager() {
        return copyUserDtoOrNull(cancelManager);
    }

    public HistoryDto setItem(@NonNull ItemDto item) {
        this.item = new ItemDto(item);
        return this;
    }

    public HistoryDto setRequester(UserDto requester) {
        this.requester = copyUserDtoOrNull(requester);
        return this;
    }

    public HistoryDto setApproveManager(UserDto approveManager) {
        this.approveManager = copyUserDtoOrNull(approveManager);
        return this;
    }

    public HistoryDto setReturnManager(UserDto returnManager) {
        this.returnManager = copyUserDtoOrNull(returnManager);
        return this;
    }

    public HistoryDto setLostManager(UserDto lostManager) {
        this.lostManager = copyUserDtoOrNull(lostManager);
        return this;
    }

    public HistoryDto setCancelManager(UserDto cancelManager) {
        this.cancelManager = copyUserDtoOrNull(cancelManager);
        return this;
    }

    public String getRequesterId() {
        return getStudentIdOrNull(requester);
    }

    public String getApproveManagerId() {
        return getStudentIdOrNull(approveManager);
    }

    public String getReturnManagerId() {
        return getStudentIdOrNull(returnManager);
    }

    public String getLostManagerId() {
        return getStudentIdOrNull(lostManager);
    }

    public String getCancelManagerId() {
        return getStudentIdOrNull(cancelManager);
    }

    public HistoryDto.HistoryStatus getStatus() {
        //TODO 여기 분기 다시 깔끔하게 짜기
        //TODO ERROR인 조건들 추가하기 ex)item이 널이거나 그런경우?...
        if(reservedTimeStamp != 0) {
            if(returnTimeStamp != 0) {
                if(lostTimeStamp != 0) {
                    return HistoryDto.HistoryStatus.FOUND;
                }
                return HistoryDto.HistoryStatus.RETURNED;
            }
            else if(cancelTimeStamp != 0) {
                return HistoryDto.HistoryStatus.EXPIRED;
            }
            else if(approveTimeStamp != 0) {
                if(lostTimeStamp != 0) {
                    return HistoryDto.HistoryStatus.LOST;
                }
                else if(dueTime() > System.currentTimeMillis()/1000) {
                    return HistoryDto.HistoryStatus.USING;
                }
                else {
                    return HistoryDto.HistoryStatus.DELAYED;
                }
            }
            else if(expiredTime() > System.currentTimeMillis()/1000) {
                return HistoryDto.HistoryStatus.REQUESTED;
            }
            else {
                return HistoryDto.HistoryStatus.EXPIRED;
            }
        }
        else {
            if(lostTimeStamp != 0) {
                if(returnTimeStamp != 0) {
                    return HistoryDto.HistoryStatus.FOUND;
                } else {
                    return HistoryDto.HistoryStatus.LOST;
                }
            }
            return HistoryDto.HistoryStatus.ERROR;
        }
    }

    public long expiredTime() {
        return reservedTimeStamp + 15*60;
    }

    public long dueTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar tmp = Calendar.getInstance();

        tmp.setTime(new Date(approveTimeStamp*1000));
        tmp.setTimeZone(timeZone);
        tmp.add(Calendar.DATE, 7);
        if(tmp.get(Calendar.HOUR_OF_DAY) > 18 ) {
            tmp.add(Calendar.DATE, 1);
        }
        tmp.set(Calendar.HOUR_OF_DAY, 17);
        tmp.set(Calendar.MINUTE, 59);
        tmp.set(Calendar.SECOND, 59);
        if(tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            tmp.add(Calendar.DATE, 2);
        }
        else if(tmp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            tmp.add(Calendar.DATE, 1);
        }
        return tmp.getTime().getTime()/1000;
    }

    private UserDto copyUserDtoOrNull(UserDto userDto) {
        if(userDto == null) return null;
        return new UserDto(userDto);
    }

    private String getStudentIdOrNull(UserDto userDto) {
        if(userDto == null) return null;
        return userDto.getStudentId();
    }
    public enum HistoryStatus {
        REQUESTED, USING, DELAYED, LOST, EXPIRED, RETURNED, FOUND, ERROR
    }
}
