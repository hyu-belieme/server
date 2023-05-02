package com.example.beliemeserver.util;

import com.example.beliemeserver.data.entity._new.*;

public class ArgumentMatchHelper {
    public static boolean canMatch(NewUniversityEntity a, NewUniversityEntity b) {
        return a.equals(b);
    }

    public static boolean canMatch(NewDepartmentEntity a, NewDepartmentEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getUniversity(), b.getUniversity())
                && a.getId().equals(b.getId())
                && a.getName().equals(b.getName());
    }

    public static boolean canMatch(NewMajorEntity a, NewMajorEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getUniversity(), b.getUniversity())
                && a.getId().equals(b.getId())
                && a.getCode().equals(b.getCode());
    }

    public static boolean canMatch(NewMajorDepartmentJoinEntity a, NewMajorDepartmentJoinEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getMajor(), b.getMajor())
                && canMatch(a.getDepartment(), b.getDepartment());
    }

    public static boolean canMatch(NewUserEntity a, NewUserEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getUniversity(), b.getUniversity())
                && a.getId().equals(b.getId())
                && a.getStudentId().equals(b.getStudentId())
                && a.getName().equals(b.getName())
                && a.getEntranceYear() == b.getEntranceYear();
    }

    public static boolean canMatch(NewAuthorityEntity a, NewAuthorityEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getDepartment(), b.getDepartment())
                && a.getPermission().equals(b.getPermission());
    }

    public static boolean canMatch(NewAuthorityUserJoinEntity a, NewAuthorityUserJoinEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getUser(), b.getUser())
                && canMatch(a.getAuthority(), b.getAuthority());
    }

    public static boolean canMatch(NewStuffEntity a, NewStuffEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getDepartment(), b.getDepartment())
                && a.getId().equals(b.getId())
                && a.getName().equals(b.getName())
                && a.getThumbnail().equals(b.getThumbnail());
    }

    public static boolean canMatch(NewItemEntity a, NewItemEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getStuff(), b.getStuff())
                && a.getId().equals(b.getId())
                && a.getNum() == b.getNum()
                && a.getLastHistoryId().equals(b.getLastHistoryId());
    }

    public static boolean canMatch(NewHistoryEntity a, NewHistoryEntity b) {
        if(a == null && b == null) return true;
        if(a == null || b == null) return false;
        return canMatch(a.getItem(), b.getItem())
                && a.getId().equals(b.getId())
                && a.getNum() == b.getNum()
                && canMatch(a.getRequester(), b.getRequester())
                && canMatch(a.getApproveManager(), b.getApproveManager())
                && canMatch(a.getLostManager(), b.getLostManager())
                && canMatch(a.getReturnManager(), b.getReturnManager())
                && canMatch(a.getCancelManager(), b.getCancelManager())
                && a.getRequestedAt() == b.getRequestedAt()
                && a.getApprovedAt() == b.getApprovedAt()
                && a.getLostAt() == b.getLostAt()
                && a.getReturnedAt() == b.getReturnedAt()
                && a.getCanceledAt() == b.getCanceledAt();
    }
}
