package team.redrock.running.dto;

import lombok.Data;
import team.redrock.running.vo.InviteInfo;

import java.io.Serializable;

@Data
public class InvitationSend implements Serializable {
    private String invited_id;
    private String date;
    private String nickname;

    public InvitationSend(InviteInfo inviteInfo) {
        this.invited_id = inviteInfo.getInvited_id();
        this.date = inviteInfo.getDate();
        this.nickname = inviteInfo.getNickname();
    }

    @Override
    public String toString() {
        return "InvitationSend{" +
                "invited_id='" + invited_id + '\'' +
                ", date='" + date + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public InvitationSend() {
    }
}
