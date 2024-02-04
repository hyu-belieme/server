package com.belieme.apiserver.domain.dto;

import java.text.DecimalFormat;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HistoryCursorDto implements CursorDto {

  private final long requestedAt;

  private final long lostAt;

  private final UUID id;

  private HistoryCursorDto(long requestedAt, long lostAt, UUID id) {
    this.requestedAt = requestedAt;
    this.lostAt = lostAt;
    this.id = id;
  }

  public static HistoryCursorDto fromString(String str) {
    try {
      long requestedAt = Long.parseLong(str.substring(0, 10));
      long lostAt = Long.parseLong(str.substring(10, 20));
      UUID id = UUID.fromString(str.substring(20));
      return new HistoryCursorDto(requestedAt, lostAt, id);
    } catch (Exception e) {
      return null;
    }
  }

  public static HistoryCursorDto nextCursor(HistoryDto history) {
    long nextRequestAt = history.requestedAt();
    long nextLostAt = history.lostAt();
    UUID nextId = getNextUUID(history.id());

    if(nextId.getLeastSignificantBits() == Long.MIN_VALUE && nextId.getMostSignificantBits() == Long.MIN_VALUE) {
      if (nextLostAt <= 0) {
        nextLostAt = Long.MAX_VALUE;
        if (nextRequestAt <= 0) {
          return null;
        }
        nextRequestAt -= 1;
      } else {
        nextLostAt -= 1;
      }
    }
    return new HistoryCursorDto(nextRequestAt, nextLostAt, nextId);
  }

  private static UUID getNextUUID(UUID inputUUID) {
    long mostSigBits = inputUUID.getMostSignificantBits();
    long leastSigBits = inputUUID.getLeastSignificantBits();

    if (leastSigBits == Long.MAX_VALUE) {
      mostSigBits += 1;
    }
    leastSigBits += 1;

    return new UUID(mostSigBits, leastSigBits);
  }

  @Override
  public String getCursorString() {
    String requestedAtString = new DecimalFormat("0000000000").format(requestedAt);
    String lostAtString = new DecimalFormat("0000000000").format(lostAt);
    String idString = id.toString();

    return requestedAtString + lostAtString + idString;
  }
}
