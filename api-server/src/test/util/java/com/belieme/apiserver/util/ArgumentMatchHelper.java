package com.belieme.apiserver.util;

import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.entity.AuthorityUserJoinEntity;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.MajorDepartmentJoinEntity;
import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.data.entity.UserEntity;

public class ArgumentMatchHelper {

  public static boolean canMatch(UniversityEntity a, UniversityEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }

    return a.getId().equals(b.getId()) && a.getName().equals(b.getName()) && compareNullable(
        a.getApiUrl(), b.getApiUrl());
  }

  public static boolean canMatch(DepartmentEntity a, DepartmentEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getUniversity(), b.getUniversity()) && a.getId().equals(b.getId())
        && a.getName().equals(b.getName());
  }

  public static boolean canMatch(MajorEntity a, MajorEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getUniversity(), b.getUniversity()) && a.getId().equals(b.getId())
        && a.getCode().equals(b.getCode());
  }

  public static boolean canMatch(MajorDepartmentJoinEntity a, MajorDepartmentJoinEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getMajor(), b.getMajor()) && canMatch(a.getDepartment(), b.getDepartment());
  }

  public static boolean canMatch(UserEntity a, UserEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getUniversity(), b.getUniversity()) && a.getId().equals(b.getId())
        && a.getStudentId().equals(b.getStudentId()) && a.getName().equals(b.getName())
        && a.getEntranceYear() == b.getEntranceYear();
  }

  public static boolean canMatch(AuthorityEntity a, AuthorityEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getDepartment(), b.getDepartment()) && a.getPermission()
        .equals(b.getPermission());
  }

  public static boolean canMatch(AuthorityUserJoinEntity a, AuthorityUserJoinEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getUser(), b.getUser()) && canMatch(a.getAuthority(), b.getAuthority());
  }

  public static boolean canMatch(StuffEntity a, StuffEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getDepartment(), b.getDepartment()) && a.getId().equals(b.getId())
        && a.getName().equals(b.getName()) && a.getThumbnail().equals(b.getThumbnail());
  }

  public static boolean canMatch(ItemEntity a, ItemEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getStuff(), b.getStuff()) && a.getId().equals(b.getId())
        && a.getNum() == b.getNum() && canMatch(a.getLastHistory(), b.getLastHistory());
  }

  public static boolean canMatch(HistoryEntity a, HistoryEntity b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return canMatch(a.getItem(), b.getItem()) && a.getId().equals(b.getId())
        && a.getNum() == b.getNum() && canMatch(a.getRequester(), b.getRequester()) && canMatch(
        a.getApproveManager(), b.getApproveManager()) && canMatch(a.getLostManager(),
        b.getLostManager()) && canMatch(a.getReturnManager(), b.getReturnManager()) && canMatch(
        a.getCancelManager(), b.getCancelManager()) && a.getRequestedAt() == b.getRequestedAt()
        && a.getApprovedAt() == b.getApprovedAt() && a.getLostAt() == b.getLostAt()
        && a.getReturnedAt() == b.getReturnedAt() && a.getCanceledAt() == b.getCanceledAt();
  }

  private static boolean compareNullable(Object a, Object b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }
}
