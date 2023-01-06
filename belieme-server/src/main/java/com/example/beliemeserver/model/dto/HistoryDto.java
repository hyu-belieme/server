package com.example.beliemeserver.model.dto;

import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class HistoryDto {
    public enum HistoryStatus {
        REQUESTED, USING, DELAYED, LOST, EXPIRED, RETURNED, FOUND, ERROR
    }

    private ItemDto item;
    private int num;

    private UserDto requester;
    private UserDto approveManager;
    private UserDto returnManager;
    private UserDto lostManager;
    private UserDto cancelManager;

    private long reservedTimeStamp;
    private long approveTimeStamp;
    private long returnTimeStamp;
    private long lostTimeStamp;
    private long cancelTimeStamp;

    public String getRequesterId() {
        if(requester == null) {
            return null;
        }
        else {
            return requester.getStudentId();
        }
    }

    public String getApproveManagerId() {
        if(approveManager == null) {
            return null;
        }
        else {
            return approveManager.getStudentId();
        }
    }

    public String getReturnManagerId() {
        if(returnManager == null) {
            return null;
        }
        else {
            return returnManager.getStudentId();
        }
    }

    public String getLostManagerId() {
        if(lostManager == null) {
            return null;
        }
        else {
            return lostManager.getStudentId();
        }
    }

    public String getCancelManagerId() {
        if(cancelManager == null) {
            return null;
        }
        else {
            return cancelManager.getStudentId();
        }
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
}
