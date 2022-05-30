package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(HistoryId.class)
public class HistoryEntity {
    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "stuff_id", referencedColumnName = "stuff_id"),
            @JoinColumn(name = "item_num", referencedColumnName = "num")
    })
    private ItemEntity item;

    @Id
    @Column(name = "num")
    private int num;

    @Column(name = "requester_id")
    private String requesterId;

    @Column(name = "approve_manager_id")
    private String approveManagerId;

    @Column(name = "return_manager_id")
    private String returnManagerId;

    @Column(name = "lost_manager_id")
    private String lostManagerId;

    @Column(name = "cancel_manager_id")
    private String cancelManagerId;

    @Column(name = "reserved_time_stamp")
    private long reservedTimeStamp;

    @Column(name = "approve_time_stamp")
    private long approveTimeStamp;

    @Column(name = "return_time_stamp")
    private long returnTimeStamp;

    @Column(name = "lost_time_stamp")
    private long lostTimeStamp;

    @Column(name = "cancel_time_stamp")
    private long cancelTimeStamp;
}